package com.mc.app.hotel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/7/3.
 */

public class Test {

    @org.junit.Test
    public void main(){
        Timer startReadIdCardTimer = new Timer();
        startReadIdCardTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
               System.out.println("wow  loop ");
            }
        }, 500, 1000);


    }
}
