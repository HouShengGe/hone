package com.mc.app.hotel.common.http;

import com.google.gson.Gson;
import com.mc.app.hotel.bean.ReqBaseBean;
import com.mc.app.hotel.common.util.SPerfUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/7/4.
 */

public class Params {

    public static Map<String, String> getParams(Map<String, String> map) {
        ReqBaseBean baseBean = SPerfUtil.getReqBaseInfo();
        Map<String, String> paramMap = new HashMap<>();
        Gson gson = new Gson();
        if (map != null) {
            String reqData = gson.toJson(map);
            paramMap.put("reqData", reqData);
        }
        paramMap.put("appId", baseBean.getAppId());
        paramMap.put("key", baseBean.getKey());
        paramMap.put("token", baseBean.getToken());
        paramMap.put("userId", baseBean.getUserId() + "");
        return paramMap;
    }

    public static Map<String, String> getParams() {
        return getParams(null);
    }


    public static Map<String, String> getLoginParams(String mobile, String vCode) {
        Map<String, String> map = new HashMap<>();
        map.put("strMobile", mobile);
        map.put("strCode", vCode);
        return getParams(map);
    }

    public static Map<String, String> getVCodeParams(String mobile) {
        Map<String, String> map = new HashMap<>();
        map.put("strMobile", mobile);
        return getParams(map);
    }
}
