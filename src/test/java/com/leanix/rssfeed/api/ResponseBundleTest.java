package com.leanix.rssfeed.api;

import com.leanix.rssfeed.utilities.TestRssFeedConstants;
import org.junit.Assert;
import org.junit.Test;

public class ResponseBundleTest {

    private ResponseBundle responseBundle;

    @Test
    public void testToString() throws Exception {
        responseBundle = new ResponseBundle();
        responseBundle.setResponseCode(TestRssFeedConstants.SAMPLE_RESPONSE_CODE);
        responseBundle.setErrorCode(null);
        responseBundle.setErrorMessage(null);
        responseBundle.setResponse(TestRssFeedConstants.SAMPLE_RESPONSE);
        String expectedResponseBundleString = TestRssFeedConstants.EXPECTED_RESPONSE_BUNDLE_STRING;
        Assert.assertEquals(expectedResponseBundleString, responseBundle.toString());
    }
}
