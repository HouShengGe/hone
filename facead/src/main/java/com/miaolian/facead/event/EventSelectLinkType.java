package com.miaolian.facead.event;

/**
 * Created by gaofeng on 2017-03-02.
 */

public class EventSelectLinkType {
    String linkType;

    public EventSelectLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }
}
