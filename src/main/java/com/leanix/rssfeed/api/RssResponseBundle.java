package com.leanix.rssfeed.api;

import com.leanix.rssfeed.beans.ResponseBean;

public class RssResponseBundle {

    String responseCode;

    String errorCode;

    String errorMessage;

    ResponseBean responseBean;

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

    public ResponseBean getResponseBean() {
        return responseBean;
    }

    public void setResponseBean(ResponseBean responseBean) {
        this.responseBean = responseBean;
    }

    @Override
    public String toString() {
        return "{"
            + "\tresponseCode: " + this.responseCode + ",\n"
            + "\terrorCode: " + this.errorCode + ",\n"
            + "\terrorMessage: " + this.errorMessage + ",\n"
            + "\tresponse: " + this.responseBean + ",\n"
            + "}";
    }
}

