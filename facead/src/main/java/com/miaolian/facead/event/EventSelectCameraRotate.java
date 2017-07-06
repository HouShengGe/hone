package com.miaolian.facead.event;

/**
 * Created by user1 on 2017/5/16.
 */

public class EventSelectCameraRotate {
    int degree;

    public EventSelectCameraRotate(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
