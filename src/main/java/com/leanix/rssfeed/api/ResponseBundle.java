package com.leanix.rssfeed.api;

public class ResponseBundle {

    String responseCode;

    String errorCode;

    String errorMessage;

    String response;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "{"
            + "\tresponseCode: " + this.responseCode + ",\n"
            + "\terrorCode: " + this.errorCode + ",\n"
            + "\terrorMessage: " + this.errorMessage + ",\n"
            + "\tresponse: " + this.response + ",\n"
            + "}";
    }
}

