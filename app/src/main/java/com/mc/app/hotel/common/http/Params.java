package com.mc.app.hotel.common.http;

import com.google.gson.Gson;
import com.mc.app.hotel.bean.CheckInInfo;
import com.mc.app.hotel.bean.CustomerParamsInfo;
import com.mc.app.hotel.bean.ReqBaseBean;
import com.mc.app.hotel.common.util.SPerfUtil;
import com.mc.app.hotel.common.util.StringUtil;

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

    public static Map<String, String> getCommitParams(CheckInInfo info) {
        Map<String, String> map = new HashMap<>();
        map.put("roomNo", info.getRoomNo());
        map.put("roomPrice", info.getRoomPrice());
        map.put("customer", info.getCustomer());
        map.put("sex", info.getSex());
        map.put("mobile", info.getMobile());
        map.put("nation", info.getNation());
        map.put("address", info.getAddress());
        map.put("birthDate", info.getBirthDate());
        map.put("exprDate", info.getExprDate());
        map.put("arriveDate", info.getArriveDate());
        map.put("leaveDate", info.getLeaveDate());
        map.put("idCardPhoto", info.getIdCardPhoto());
        map.put("scanPhoto", info.getScanPhoto());
        map.put("faceResult", info.getFaceResult());
        map.put("faceDegree", info.getFaceDegree());
        map.put("idCard", info.getIdCard());
        return getParams(map);
    }

    public static Map<String, String> getCustomerListParams(CustomerParamsInfo info) {

        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", info.getPageIndex() + "");
        map.put("pageSize", info.getPageSize() + "");
        map.put("qtype", info.getQtype() + "");
        if (!StringUtil.isBlank(info.getRoomNo()))
            map.put("roomNo", info.getRoomNo());
        if (info.getStoreId() != 0)
            map.put("storeId", info.getStoreId() + "");
        if (!StringUtil.isBlank(info.getCustName()))
            map.put("custName", info.getCustName());
        if (!StringUtil.isBlank(info.getIdCard()))
            map.put("idCard", info.getIdCard());
        if (!StringUtil.isBlank(info.getMobile()))
            map.put("mobile", info.getMobile());
        return getParams(map);
    }

    public static Map<String, String> getFaceParams(int masterID) {
        Map<String, String> map = new HashMap<>();
        map.put("masterId", masterID + "");
        return getParams(map);
    }

    public static Map<String, String> getCheckOutParams(String masterIDs) {
        Map<String, String> map = new HashMap<>();
        map.put("masterIds", masterIDs);
        return getParams(map);
    }

    public static Map<String, String> getRoomDetialParams(String roomNo, int storeID) {
        Map<String, String> map = new HashMap<>();
        map.put("roomNo", roomNo);
        map.put("storeId", storeID + "");
        return getParams(map);
    }

    public static Map<String, String> getHotelListParams(String storeName, int pageIndex) {
        Map<String, String> map = new HashMap<>();
        if (!StringUtil.isBlank(storeName))
            map.put("storeName", storeName);
        map.put("pageIndex", pageIndex + "");
        map.put("pageSize", 15 + "");
        return getParams(map);
    }

}
