import com.fasterxml.jackson.databind.ObjectMapper;
import com.leanix.rssfeed.beans.ResponseBean;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class OutBoundProcessor {
    public static String accessToken;

    public static String synchronizationId;

    public static String result;

    public static void main(String[] args) throws IOException, InterruptedException {


        String apiToken = "apitoken:49wMDmUcUhv2DXphNSNuOy55PMnwAYaK6urRxAmu";
        String encoded = new String(Base64.getEncoder().encode(apiToken.getBytes()));
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
        Request request = new Request.Builder()
            .url("https://app.leanix.net/services/mtm/v1/oauth2/token")
            .method("POST", body)
            .addHeader("authorization", "Basic "+encoded)
            .addHeader("Media-type", "application/x-www-form-urlencoded")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonObj = new JSONObject(response.body().string());
        accessToken = jsonObj.getString("access_token");

        String[] payload_part = accessToken.split("\\.");
        String payloadP = payload_part[1];

        if(payloadP.length() % 4 != 0)
            payloadP += '='* (4 - payloadP.length() % 4);

        String decoded = new String(Base64.getDecoder().decode(payloadP.getBytes()));

        //payload = json.loads(base64.b64decode(payload_part))

        jsonObj = new JSONObject(decoded);


        System.out.println(decoded);

        return;

       // synchronizationRun();
        //System.out.println(result);
    }

    public static void synchronizationRun() throws IOException, InterruptedException {

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");

        String str = readFile("src/main/java/outbound_processor.json");
        RequestBody body = RequestBody.create(mediaType, str);
        Request request = new Request.Builder()
            .url("https://demo-eu.leanix.net/services/integration-api/v1/synchronizationRuns")
            .method("POST", body)
            .addHeader("Authorization", "Bearer "+accessToken)
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonObj = new JSONObject(response.body().string());
        synchronizationId = jsonObj.getString("id");
        startRun();
    }

    public static void startRun() throws IOException, InterruptedException {

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
            .url("https://demo-eu.leanix.net/services/integration-api/v1/synchronizationRuns/"+synchronizationId+"/start")
            .method("POST", body)
            .addHeader("Authorization", "Bearer "+accessToken)
            .addHeader("Content-Type", "application/json")
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

    public static Response statusCheck() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://demo-eu.leanix.net/services/integration-api/v1/synchronizationRuns/"+synchronizationId+"/status")
            .method("GET", null)
            .addHeader("Authorization", "Bearer "+accessToken)
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = client.newCall(request).execute();
       return response;
    }

    public static void getResults() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://demo-eu.leanix.net/services/integration-api/v1/synchronizationRuns/"+synchronizationId+"/results")
            .method("GET", null)
            .addHeader("Authorization", "Bearer "+accessToken)
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = client.newCall(request).execute();
       result = response.body().string();
        ObjectMapper mapper = new ObjectMapper();
        ResponseBean readValue = mapper.readValue(result, ResponseBean.class);
        System.out.println(result);
    }

    public static String readFile(String filename) {
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

}
