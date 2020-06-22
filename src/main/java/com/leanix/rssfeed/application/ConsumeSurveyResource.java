package com.leanix.rssfeed.application;

import com.leanix.rssfeed.services.CustomSurveyService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.json.JSONObject;
import com.leanix.rssfeed.utilities.Constant;

@Path("/poll")
public class ConsumeSurveyResource {

    private String cloudServiceQuery = "{"
        + "  \"filter\": {"
        + "    \"facetFilter\": ["
        + "      {\n"
        + "        \"facetKey\": \"FactSheetTypes\","
        + "        \"operator\": \"OR\","
        + "        \"keys\": ["
        + "          \"CloudService\""
        + "        ]"
        + "      }"
        + "    ]"
        + "  }"
        + "}";
    private String userQuery = "{"
        + "  \"roles\": ["
        + "    {"
        + "      \"subscriptionType\": \"OBSERVER\","
        + "      \"roleDetails\": []"
        + "    },"
        + "    {"
        + "      \"subscriptionType\": \"RESPONSIBLE\","
        + "      \"roleDetails\": []"
        + "    }"
        + "  ]"
        + "}";

    private String pollId;

    private String apiToken;

    private  CustomSurveyService obj;

    public ConsumeSurveyResource(String apiToken){
        this.apiToken = apiToken;
        this.obj = new CustomSurveyService(this.apiToken);
    }

    @POST
    @Produces("application/json")
    public String createSurvey(String request){

        System.out.println("Initialising new survey");

        JSONObject surveyForm = new JSONObject(request);

        try {

            String accessToken = obj.getAccessToken();

            System.out.println("Access Token : " + accessToken);

            //System.out.println("Initializing AccessToken : ");
            JSONObject workspaceInfo = obj.getWorkspaceInfo(accessToken);

            JSONObject surveyApiToken = obj.getSurveyToken(accessToken,workspaceInfo.getJSONObject("principal").getString("id"));

            System.out.println("Survey API Token = " + surveyApiToken.toString());

            surveyForm.put("workspaceId" , workspaceInfo.getJSONObject("principal").getJSONObject("permission").getString("workspaceId"));

            //String surveyForm = readFile("src/main/resources/surveyForm.json");

            String surveyCreatedResponse = obj.createPoll(surveyForm.toString(), accessToken);

            System.out.println("Initial Run Preview = " + surveyCreatedResponse);

            JSONObject jsonObject = new JSONObject(surveyCreatedResponse);

            //adding cloudQuery and Factsheetquery to the survey.
            jsonObject.getJSONObject("data").put("userQuery", new JSONObject(userQuery));
            jsonObject.getJSONObject("data").put("factSheetQuery",new JSONObject(cloudServiceQuery));


            //adding message strings to survey object.
            jsonObject.getJSONObject("data").put("additionalFactSheetSubject", Constant.ADDITIONAL_FACTSHEET_SUBJECT);
            jsonObject.getJSONObject("data").put("additionalFactSheetText", Constant.ADDITIONAL_FACTSHEET_TEXT);
            jsonObject.getJSONObject("data").put("introductionText", Constant.INTRODUCTION_TEXT);
            jsonObject.getJSONObject("data").put("introductionSubject", Constant.INTRODUCTION_SUBJECT);


            //updating the survey poll
           String updateResponse =  obj.updateFactSheetQuery(jsonObject.getJSONObject("data"),accessToken);

            System.out.println("Poll Run Preview After adding cloud query : " + updateResponse);

            //Start the poll
            JSONObject pollIdObj = new JSONObject();
            pollIdObj.put("id", jsonObject.getJSONObject("data").getString("id"));

            JSONObject pollObj = new JSONObject();
            pollObj.put("poll", pollIdObj);

            String startPollResponse = obj.startPoll(pollObj, accessToken, jsonObject.getJSONObject("data").getString("workspaceId"));

            //Set up the fact sheet query to the survey.
            //FileWriter file = new FileWriter("src/main/resources/createPollUpdate.json");
            //file.write(startPollResponse);
            //file.close();

            System.out.println("Poll is started successfully : ");

            return startPollResponse;


        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("IO Exception occured ");

            return null;
        }
       // 49wMDmUcUhv2DXphNSNuOy55PMnwAYaK6urRxAmu
    }


    @GET
    @Produces("application/json")
    @Path("/result/{pollId}")
    public String pollResult(@PathParam("pollId") String pollId) throws IOException {

        String result = obj.getPollResult(pollId,obj.getAccessToken());

        return result;

        // 49wMDmUcUhv2DXphNSNuOy55PMnwAYaK6urRxAmu
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
}
