package com.miaolian.facead.event;

/**
 * Created by gaofeng on 2017-03-03.
 */

public class EventDoActive {
    String activeCode;

    public EventDoActive(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }
}
