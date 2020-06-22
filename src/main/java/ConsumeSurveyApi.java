
import java.io.IOException;
import java.util.Base64;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class ConsumeSurveyApi {

    private String apiToken;

    private OkHttpClient client;

    public ConsumeSurveyApi(String apiToken) {
        this.apiToken = "apiToken:" + apiToken;
        this.client = new OkHttpClient().newBuilder()
            .build();
    }

    public String getAccessToken() throws IOException {

        String encoded = new String(Base64.getEncoder().encode(this.apiToken.getBytes()));

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");

        Request request = new Request.Builder()
            .url("https://app.leanix.net/services/mtm/v1/oauth2/token")
            .method("POST", body)
            .addHeader("authorization", "Basic "+encoded)
            .addHeader("Media-type", "application/x-www-form-urlencoded")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build();
        Response response = this.client.newCall(request).execute();
        JSONObject jsonObj = new JSONObject(response.body().string());
        return jsonObj.getString("access_token");
    }

    public JSONObject getWorkspaceInfo(String accessToken){

        String[] payload_part = accessToken.split("\\.");
        String payloadP = payload_part[1];

        if(payloadP.length() % 4 != 0)
            payloadP += '='* (4 - payloadP.length() % 4);

        String decoded = new String(Base64.getDecoder().decode(payloadP.getBytes()));
        //payload = json.loads(base64.b64decode(payload_part))
        return new JSONObject(decoded);

    }

    public JSONObject getSurveyToken(String accessToken, String userId) throws IOException {

        this.client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://demo-eu.leanix.net/services/mtm/v1/apiTokens?identifier=SURVEY_API_TOKEN_" + userId)
            .method("GET", null)
            .addHeader("Authorization", "Bearer " + accessToken )
            .build();
        Response response = client.newCall(request).execute();

        return new JSONObject(response.body().string());

    }

    public String createPoll(String surveyForm, String accessToken) throws IOException {

        String workspaceId = new JSONObject(surveyForm).getString("workspaceId");

        this.client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,surveyForm);
        Request request = new Request.Builder()
            .url("https://demo-eu.leanix.net/services/poll/v2/polls?workspaceId=" + workspaceId)
            .method("POST", body)
            .addHeader("Authorization", "Bearer " + accessToken)
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = this.client.newCall(request).execute();

        return response.body().string();
    }

    public String updateFactSheetQuery(JSONObject data, String accessToken) throws IOException {

        String pollId = data.getString("id");

        String workspaceId = data.getString("workspaceId");

        this.client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,data.toString());
        Request request = new Request.Builder()
            .url("https://demo-eu.leanix.net/services/poll/v2/polls/" + pollId + "?workspaceId=" + workspaceId)
            .method("PUT", body)
            .addHeader("Authorization", "Bearer " + accessToken)
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = this.client.newCall(request).execute();

        return response.body().string();
    }

    public String startPoll(JSONObject poll, String accessToken, String workspaceId) throws IOException {

        this.client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,poll.toString());
        Request request = new Request.Builder()
            .url("https://demo-eu.leanix.net/services/poll/v2/pollRuns?workspaceId=" + workspaceId)
            .method("POST", body)
            .addHeader("Authorization", "Bearer " + accessToken)
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = this.client.newCall(request).execute();

        return response.body().string();
    }

    public JSONObject getPollResult(String pollId , String accessToken) throws IOException {


        this.client = new OkHttpClient().newBuilder()
            .build();

        MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request.Builder()
            .url("https://demo-eu.leanix.net/services/poll/v2/pollRuns/" + pollId + "/pollResults")
            .method("GET", null)
            .addHeader("Authorization", "Bearer " + accessToken)
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = this.client.newCall(request).execute();

        return new JSONObject(response.body().string());

    }

    public String getApiToken(){
        return this.apiToken;
    }
}
