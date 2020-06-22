package com.leanix.rssfeed.services;

import com.leanix.rssfeed.services.implementation.RssFeedInterfaceImpl;
import com.leanix.rssfeed.utilities.TestLoadPropertyFile;
import com.leanix.rssfeed.utilities.TestRssFeedConstants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class RssFeedInterfaceImplTest {

    public static RssFeedInterfaceImpl rssFeedInterface = new RssFeedInterfaceImpl();

    @Test
    public void testGetRssFeed() throws Exception {
        JSONObject expectedJSON = generateJSONObject(TestRssFeedConstants.TEST_SUCCESS_JSON_PATH);
        JSONObject actualJSON = rssFeedInterface.getRssFeed(TestRssFeedConstants.TEST_ENCODED_URL);
        Assert.assertEquals(expectedJSON.toString(), actualJSON.toString());
    }

    @Test
    public void testAddServiceAndKeyword() throws Exception {
        JSONObject item = generateJSONObject(TestRssFeedConstants.JSON_ITEM_PATH);
        rssFeedInterface.addServiceAndKeyword(item);
        String actualServices = item.getString("services");
        String[] actualKeywords = (String[]) item.get("keywords");
        String expectedServices = TestRssFeedConstants.EXPECTED_SERVICES;
        String[] expectedKeywords = new TestLoadPropertyFile()
            .getProperty(TestRssFeedConstants.EXPECTED_KEYWORDS_PATH)
            .getProperty(TestRssFeedConstants.JSON_KEYWORDS)
            .split(TestRssFeedConstants.KEYWORD_DELIMITER);
        Assert.assertEquals(expectedServices, actualServices);
        for (int i = 0; i < actualKeywords.length; i++) {
            Assert.assertEquals(expectedKeywords[i], actualKeywords[i]);
        }
    }

    public JSONObject generateJSONObject(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line + "\n");
        }
        String expectedJSONString = content.toString();
        return new JSONObject(expectedJSONString);
    }
}
