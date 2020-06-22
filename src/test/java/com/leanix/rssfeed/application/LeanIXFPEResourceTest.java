package com.leanix.rssfeed.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leanix.rssfeed.api.ResponseBundle;
import com.leanix.rssfeed.utilities.TestRssFeedConstants;
import java.io.FileReader;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

public class LeanIXFPEResourceTest {

    private LeanIXFPEResource leanIXFPEResource;

    private ResponseBundle responseBundle;

    private ObjectMapper mapper;

    private ResponseBundle expectedResponseBundle;

    @Test
    public void testResponseBundle() throws Exception {
        leanIXFPEResource = new LeanIXFPEResource();
        responseBundle = leanIXFPEResource.get_URL(TestRssFeedConstants.TEST_ENCODED_URL);
        mapper = new ObjectMapper();
        expectedResponseBundle = mapper.readValue(mapper.writeValueAsString(new JSONParser()
            .parse(new FileReader(TestRssFeedConstants.TEST_SUCCESS_RESPONSE_JSON_PATH))), ResponseBundle.class);
        Assert.assertEquals(expectedResponseBundle.toString(), responseBundle.toString());
    }

    @Test
    public void testInvalidURLResponse() throws Exception {
        leanIXFPEResource = new LeanIXFPEResource();
        responseBundle = leanIXFPEResource.get_URL(TestRssFeedConstants.TEST_INVALID_URL);
        mapper = new ObjectMapper();
        expectedResponseBundle = mapper.readValue(mapper.writeValueAsString(new JSONParser()
            .parse(new FileReader(TestRssFeedConstants.TEST_ERROR_RESPONSE_JSON_PATH))), ResponseBundle.class);
        Assert.assertEquals(expectedResponseBundle.toString(), responseBundle.toString());
    }
}
