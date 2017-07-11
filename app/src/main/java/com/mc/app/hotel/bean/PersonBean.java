package com.mc.app.hotel.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class PersonBean {
    String storeName;
    String address;
    String chargeName;
    String chargePhone;
    String policeMan;
    List<StoreNameBean> stores;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getChargePhone() {
        return chargePhone;
    }

    public void setChargePhone(String chargePhone) {
        this.chargePhone = chargePhone;
    }

    public String getPoliceMan() {
        return policeMan;
    }

    public void setPoliceMan(String policeMan) {
        this.policeMan = policeMan;
    }

    public List<StoreNameBean> getStores() {
        if (stores == null)
            stores = new ArrayList<>();
        return stores;
    }

    public void setStores(List<StoreNameBean> stores) {
        stores = stores;
    }

    public List<String> getStoresList() {
        List<String> list = new ArrayList<>();
        for (StoreNameBean bean : getStores()) {
            list.add(bean.getStoreName());
        }
        return list;
    }
}
