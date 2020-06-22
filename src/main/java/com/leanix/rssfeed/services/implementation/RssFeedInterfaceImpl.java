package com.leanix.rssfeed.services.implementation;

import com.leanix.rssfeed.services.interfaces.RssFeedInterface;
import com.leanix.rssfeed.utilities.LoadPropertyFile;
import com.leanix.rssfeed.utilities.RssFeedConstants;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class RssFeedInterfaceImpl implements RssFeedInterface {

    LoadPropertyFile loadProperty;
//    private HashMap<Integer, String> s_hm;
//    private HashMap<Integer, String> k_hm;


    public JSONObject getRssFeed(String encoded_url) throws IOException {

        JSONObject jsonObj;
        StringBuilder content = new StringBuilder();
        try {
            encoded_url = URLDecoder.decode(encoded_url, RssFeedConstants.UTF_8_ENCODING);

            URL url = new URL(encoded_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(RssFeedConstants.REQUEST_METHOD_GET);
            con.setRequestProperty(RssFeedConstants.REQUEST_PROPERTY_USER_AGENT, RssFeedConstants.REQUEST_PROPERTY_USER_BROWSER);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (UnknownHostException e) {
            jsonObj = new JSONObject();
            jsonObj.put(RssFeedConstants.RESPONSE_ERROR, RssFeedConstants.UNKNOWN_HOST_ERROR_CODE);
            return jsonObj;
        } catch (FileNotFoundException e) {
            jsonObj = new JSONObject();
            jsonObj.put(RssFeedConstants.RESPONSE_ERROR, RssFeedConstants.FILE_NOT_FOUND_ERROR_CODE);
            return jsonObj;
        } catch (MalformedURLException e) {
            jsonObj = new JSONObject();
            jsonObj.put(RssFeedConstants.RESPONSE_ERROR, RssFeedConstants.FILE_NOT_FOUND_ERROR_CODE);
            return jsonObj;
        }
        String XML_String = content.toString();
        return jsonObj = XML.toJSONObject(XML_String);


    }

    public void addServiceAndKeyword(JSONObject item) throws IOException {

        loadProperty = new LoadPropertyFile();
        Properties propRegex = loadProperty.getProperty(RssFeedConstants.REGEX_PROPERTY_PATH);
        Properties propKeyword = loadProperty.getProperty(RssFeedConstants.KEYWORD_PROPERTY_PATH);

        Enumeration<String> enums = (Enumeration<String>) propRegex.propertyNames();
        Pattern pattern;
        while (enums.hasMoreElements()) {
            String key = enums.nextElement();
            String value = propRegex.getProperty(key);
            String title = item.getString(RssFeedConstants.ITEM_TITLE);
            String desc = item.getString(RssFeedConstants.ITEM_DESCRIPTION);
            pattern = Pattern.compile(value, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(title).matches() || pattern.matcher(desc).matches()) {
                item.put(RssFeedConstants.ITEM_SERVICES, key);
                item.put(RssFeedConstants.ITEM_KEYWORDS,
                    propKeyword.getProperty(key).split((RssFeedConstants.KEYWORD_DELIMITER)));
            }
        }
    }

    public void addTagToFeed(JSONObject response) throws IOException {
        JSONArray items = response.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            this.addServiceAndKeyword(item);
        }
    }
}
