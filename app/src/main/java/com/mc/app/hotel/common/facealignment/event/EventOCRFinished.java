package com.mc.app.hotel.common.facealignment.event;

import com.caihua.cloud.common.entity.PersonInfo;

/**
 * Created by gaofeng on 2017-03-06.
 */

public class EventOCRFinished {
    boolean success;
    String errorMessage;
    PersonInfo personInfo;

    public EventOCRFinished(boolean success, String errorMessage, PersonInfo personInfo) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.personInfo = personInfo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
