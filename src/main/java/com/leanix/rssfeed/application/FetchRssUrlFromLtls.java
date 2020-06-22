package com.leanix.rssfeed.application;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.leanix.rssfeed.api.RssResponseBundle;
import com.leanix.rssfeed.beans.ResponseBean;
import com.leanix.rssfeed.core.MasterRssEntity;
import com.leanix.rssfeed.dao.FpeDao;
import com.leanix.rssfeed.utilities.GetRssUrlFromCSConstants;
import com.leanix.rssfeed.utilities.LoadPropertyFile;
import com.leanix.rssfeed.utilities.ResponseCodeConstants;
import io.dropwizard.hibernate.UnitOfWork;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.xpath.XPath;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;


@Path("/getRssUrls")
public class FetchRssUrlFromLtls {

    private String apiToken;

    private String workspaceId;

    private LoadPropertyFile propertyUtility;

    private Properties urlProperty;

    private OkHttpClient client;

    private final FpeDao fpeDao;

    public String accessToken;

    public String synchronizationId;

    public ResponseBean result;


    public FetchRssUrlFromLtls(String apiToken, String workspaceId, FpeDao fpeDao) {

        this.apiToken = apiToken;

        this.workspaceId = workspaceId;

        this.propertyUtility = new LoadPropertyFile();

        this.client = new OkHttpClient().newBuilder().build();

        this.fpeDao = fpeDao;

    }

    @GET
    @Path("/mode")
    @Produces("application/json")
    @UnitOfWork
    public String createRow(){

        MasterRssEntity row = new MasterRssEntity("AmazonEC4" , "dfhefhdf", "amazon");

        fpeDao.create(row);

        return "success";
    }

    @GET
    @Produces("application/json")
    public RssResponseBundle geRssURL() throws IOException, InterruptedException {
        try {
            return fetchUrl();

          //  updateRssMaster(result);
        }
        catch(Exception e){
            e.printStackTrace();
            RssResponseBundle response = new RssResponseBundle();
            response.setResponseCode(ResponseCodeConstants.IOEXCEPTION_RESPONSE_CODE);
            return response;
        }
    }

    public RssResponseBundle fetchUrl() throws IOException, InterruptedException {

        this.getAllUrls(GetRssUrlFromCSConstants.API_TOKEN + this.apiToken);

        RssResponseBundle responseBundle = new RssResponseBundle();

        responseBundle.setResponseCode(ResponseCodeConstants.SUCCESS_RESPONSE_CODE);

        responseBundle.setResponseBean(this.result);

        return responseBundle;
    }

    public void getAllUrls(String apiToken) throws IOException, InterruptedException {

        String encoded = new String(Base64.getEncoder().encode(apiToken.getBytes()));
        this.urlProperty = this.propertyUtility.getProperty(GetRssUrlFromCSConstants.URL_CONFIG_PROPERTY);

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();

        MediaType mediaType = MediaType.parse(GetRssUrlFromCSConstants.MEDIA_TYPE_FORM_URLENCODED);

        RequestBody body = RequestBody.create(mediaType,GetRssUrlFromCSConstants.CLIENT_CREDENTIALS);

        Request request = new Request.Builder()
            .url(urlProperty.getProperty(GetRssUrlFromCSConstants.AUTHETICATION_URL))
            .method(urlProperty.getProperty("method.post"), body)
            .addHeader("authorization", "Basic "+encoded)
            .addHeader("Media-type", GetRssUrlFromCSConstants.MEDIA_TYPE_FORM_URLENCODED)
            .addHeader("Content-Type", GetRssUrlFromCSConstants.MEDIA_TYPE_FORM_URLENCODED)
            .build();

        Response response = client.newCall(request).execute();

        JSONObject jsonObj = new JSONObject(response.body().string());

        System.out.println(jsonObj.toString());

        accessToken = jsonObj.getString("access_token");

        synchronizationRun();

    }

    public void synchronizationRun() throws IOException, InterruptedException {

        MediaType mediaType = MediaType.parse(GetRssUrlFromCSConstants.CONTENT_TYPE_APPLICATION_JSON);

        String outboudConnecter = this.readFile("src/main/resources/outbound_processor.json");

        RequestBody body = RequestBody.create(mediaType, outboudConnecter);

        Request request = new Request.Builder()
            .url(urlProperty.getProperty("demo.synchronizationrun"))
            .method(urlProperty.getProperty("method.post"), body)
            .addHeader("Authorization", "Bearer "+ accessToken)
            .addHeader("Content-Type", GetRssUrlFromCSConstants.CONTENT_TYPE_APPLICATION_JSON)
            .build();

        Response response = client.newCall(request).execute();

        JSONObject jsonObj = new JSONObject(response.body().string());

        synchronizationId = jsonObj.getString("id");

        startRun();

    }

    public void startRun() throws IOException, InterruptedException {

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
            .url(urlProperty.getProperty("demo.synchronizationrun") + "/" + synchronizationId + "/start" )
            .method(urlProperty.getProperty("method.post"), body)
            .addHeader("Authorization", "Bearer "+accessToken)
            .addHeader("Content-Type", GetRssUrlFromCSConstants.CONTENT_TYPE_APPLICATION_JSON)
            .build();
        Response response = client.newCall(request).execute();
        boolean flag = true;
        while(flag) {

            response = statusCheck();

            JSONObject jsonObj = new JSONObject(response.body().string());

            String status = jsonObj.getString("status");

            System.out.println(status);

            flag = status.equals("FINISHED") ? false : true;

            if(flag)
                Thread.sleep(1000);

        }
        getResults();
    }

    public Response statusCheck() throws IOException {

        Request request = new Request.Builder()
            .url(urlProperty.getProperty("demo.synchronizationrun") + "/" + synchronizationId + "/status")
            .method(urlProperty.getProperty("method.get"), null)
            .addHeader("Authorization", "Bearer "+accessToken)
            .addHeader("Content-Type", GetRssUrlFromCSConstants.CONTENT_TYPE_APPLICATION_JSON)
            .build();

        Response response = this.client.newCall(request).execute();

        return response;
    }
    public void getResults() throws IOException {

        Request request = new Request.Builder()
            .url(urlProperty.getProperty("demo.synchronizationrun") + "/" + synchronizationId+"/results")
            .method(urlProperty.getProperty("method.get"), null)
            .addHeader("Authorization", "Bearer "+accessToken)
            .addHeader("Content-Type", GetRssUrlFromCSConstants.CONTENT_TYPE_APPLICATION_JSON)
            .build();

        Response response = this.client.newCall(request).execute();

        ObjectMapper mapper = new ObjectMapper();

        ResponseBean readValue = mapper.readValue(response.body().string(), ResponseBean.class);

        result = readValue;
    }

    public String readFile(String filename) {
        String result = "";

        try {

            BufferedReader br = new BufferedReader(new FileReader(filename));

            StringBuilder sb = new StringBuilder();

            String line = br.readLine();

            while (line != null) {

                sb.append(line);

                line = br.readLine();

            }

            result = sb.toString();

        } catch(Exception e) {

            e.printStackTrace();

        }

        return result;

    }

    /*public void updateRssMaster(RssResponseBundle response){

    }*/

}
