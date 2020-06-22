package com.leanix.rssfeed.utilities;

import com.leanix.rssfeed.api.ResponseBundle;
import java.util.Properties;

public class ResponseBundleFactory {

    private Properties prop;

    private ResponseBundle responseBundle;

    public ResponseBundleFactory(Properties prop, ResponseBundle responseBundle) {
        this.prop = prop;
        this.responseBundle = responseBundle;
    }

    public ResponseBundle getUnknownHostExceptionResponseBundle() {
        responseBundle.setResponseCode(prop.getProperty("responseCode.failure"));
        responseBundle.setErrorCode(prop.getProperty("failure.unknownHost.errorCode"));
        responseBundle.setErrorMessage(prop.getProperty("failure.unknownHost.errorMessage"));

        return responseBundle;
    }

    public ResponseBundle getFileNotFoundExceptionResponseBundle() {
        responseBundle.setResponseCode(prop.getProperty("responseCode.failure"));
        responseBundle.setErrorCode(prop.getProperty("failure.fileNotFound.errorCode"));
        responseBundle.setErrorMessage(prop.getProperty("failure.fileNotFound.errorMessage"));

        return responseBundle;
    }


    public ResponseBundle getSuccessResponseBundle(String response) {
        responseBundle.setResponseCode(prop.getProperty("responseCode.success"));
        responseBundle.setResponse(response);
        return responseBundle;
    }

}
