import com.leanix.rssfeed.utilities.Constant;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

public class ConsumeSurveyApiDriver {

    private static String cloudServiceQuery = "{"
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
    private static String userQuery = "{"
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

    private static String pollId;

    public static void main(String[] args) {
        System.out.println("Hello from the custom API survey.");

        ConsumeSurveyApi obj = new ConsumeSurveyApi("49wMDmUcUhv2DXphNSNuOy55PMnwAYaK6urRxAmu");

        /*
        Step 1 : Use integration ApI to fetch the required information.
        Like Workspace Id, Access_token.
         */
        try {

            String accessToken = obj.getAccessToken();
            JSONObject workspaceInfo = obj.getWorkspaceInfo(accessToken);

            JSONObject surveyApiToken = obj.getSurveyToken(accessToken,workspaceInfo.getJSONObject("principal").getString("id"));

            String surveyForm = readFile("src/main/resources/surveyForm.json");

            String surveyCreatedResponse = obj.createPoll(surveyForm, accessToken);

            JSONObject jsonObject = new JSONObject(surveyCreatedResponse);

            //adding cloudQuery and Factsheetquery to the survey.
            jsonObject.getJSONObject("data").put("userQuery", new JSONObject(userQuery));
            jsonObject.getJSONObject("data").put("factSheetQuery",new JSONObject(cloudServiceQuery));


            //adding message strings to survey object.
            jsonObject.getJSONObject("data").put("additionalFactSheetSubject", Constant.ADDITIONAL_FACTSHEET_SUBJECT);
            jsonObject.getJSONObject("data").put("additionalFactSheetText",Constant.ADDITIONAL_FACTSHEET_TEXT);
            jsonObject.getJSONObject("data").put("introductionText",Constant.INTRODUCTION_TEXT);
            jsonObject.getJSONObject("data").put("introductionSubject",Constant.INTRODUCTION_SUBJECT);


            //updating the survey poll
           String updateResponse =  obj.updateFactSheetQuery(jsonObject.getJSONObject("data"),accessToken);

            //Start the poll
            JSONObject pollIdObj = new JSONObject();
            pollIdObj.put("id", jsonObject.getJSONObject("data").getString("id"));

            JSONObject pollObj = new JSONObject();
            pollObj.put("poll", pollIdObj);

            String startPollResponse = obj.startPoll(pollObj, accessToken, jsonObject.getJSONObject("data").getString("workspaceId"));

            //Set up the fact sheet query to the survey.
            FileWriter file = new FileWriter("src/main/resources/createPollUpdate.json");
            file.write(startPollResponse);
            file.close();

        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("IO Exception occured ");
        }
       // 49wMDmUcUhv2DXphNSNuOy55PMnwAYaK6urRxAmu
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
