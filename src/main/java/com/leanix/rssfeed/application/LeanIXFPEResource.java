package com.leanix.rssfeed.application;


import com.leanix.rssfeed.api.ResponseBundle;
import com.leanix.rssfeed.services.implementation.RssFeedInterfaceImpl;
import com.leanix.rssfeed.services.interfaces.RssFeedInterface;
import com.leanix.rssfeed.utilities.LoadPropertyFile;
import com.leanix.rssfeed.utilities.ResponseBundleFactory;
import com.leanix.rssfeed.utilities.RssFeedConstants;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.json.JSONObject;



@Path("/getFeed")
public class LeanIXFPEResource {



    //Interface for fetching rss response in json format from the url parameter
    private RssFeedInterface rssFeedInterface;

    //This class will take property file path and provide the object of that property file.
    private LoadPropertyFile loadPropertyFile;

    //The object will hold the payload data from the rss feed.
    private JSONObject response;

    //
    private ResponseBundle responseBundle;

    private Properties prop;

    private ResponseBundleFactory responseBundleFactory;

    @GET
    @Produces("application/json")
    public ResponseBundle get_URL(@QueryParam("encoded_url") String encoded_url) throws IOException {

        StringBuilder content = new StringBuilder();

        JSONObject jsonObj;

        responseBundle = new ResponseBundle();

        loadPropertyFile = new LoadPropertyFile();

        prop = loadPropertyFile.getProperty(RssFeedConstants.SUCCESS_FAILURE_PROPERTY_PATH);

        responseBundleFactory = new ResponseBundleFactory(prop, responseBundle);

        rssFeedInterface = new RssFeedInterfaceImpl();
        response = rssFeedInterface.getRssFeed(encoded_url);
        if (!response.has(RssFeedConstants.RESPONSE_ERROR)) {
            rssFeedInterface.addTagToFeed(response);
            responseBundle = responseBundleFactory.getSuccessResponseBundle(response.toString());
        } else if (response.getString(RssFeedConstants.RESPONSE_ERROR).equals(RssFeedConstants.FILE_NOT_FOUND_ERROR_CODE)) {
            responseBundle = responseBundleFactory.getFileNotFoundExceptionResponseBundle();
        } else if (response.getString(RssFeedConstants.RESPONSE_ERROR).equals(RssFeedConstants.UNKNOWN_HOST_ERROR_CODE)) {
            responseBundle = responseBundleFactory.getUnknownHostExceptionResponseBundle();
        }
        return responseBundle;
    }
}
