package com.leanix.rssfeed.utilities;

public class TestRssFeedConstants {

    public static final String TEST_ENCODED_URL = "https%3A%2F%2Fwww.feedforall.com%2Fsample-feed.xml";

    public static final String TEST_INVALID_URL = "www.leanix.net";

    public static final String TEST_ERROR_RESPONSE_JSON_PATH = "src/test/resources/testErrorResponse.json";

    public static final String TEST_SUCCESS_RESPONSE_JSON_PATH = "src/test/resources/testResponse.json";

    public static final String TEST_SUCCESS_JSON_PATH = "src/test/resources/testRssJSON.json";

    public static final String SAMPLE_RESPONSE_CODE = "00";

    public static final String SAMPLE_RESPONSE = "sample response";

    public static final String JSON_ITEM_PATH = "src/test/resources/testItem.json";

    public static final String JSON_KEYWORDS = "keywords";

    public static final String EXPECTED_SERVICES = "amazonRDS";

    public static final String EXPECTED_KEYWORDS_PATH = "keywords.properties";

    public static final String KEYWORD_DELIMITER = "##";

    public static final String EXPECTED_RESPONSE_BUNDLE_STRING = "{"
        + "\tresponseCode: 00,\n"
        + "\terrorCode: null,\n"
        + "\terrorMessage: null,\n"
        + "\tresponse: sample response,\n"
        + "}";

}
