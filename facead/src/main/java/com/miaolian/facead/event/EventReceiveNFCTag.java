package com.miaolian.facead.event;

import android.nfc.Tag;

/**
 * Created by gaofeng on 2017-05-11.
 */

public class EventReceiveNFCTag {
    Tag tag;

    public EventReceiveNFCTag(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
