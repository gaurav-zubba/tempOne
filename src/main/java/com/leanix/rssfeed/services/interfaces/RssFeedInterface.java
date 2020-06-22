package com.leanix.rssfeed.services.interfaces;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.json.JSONObject;

public interface RssFeedInterface {


    /*
        This method will provide the Json converted response from the rss feed link
        provided as the parameter.
     */
    public JSONObject getRssFeed(String encoded_url) throws IOException;


    /*
        This method will do regex based text analysis and apply service tag to each
        item in the response.
     */
    public void addTagToFeed(JSONObject response) throws IOException;


}
