package com.mc.app.hotel.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.CustomerInfo;
import com.mc.app.hotel.common.facealignment.util.DateUtils;
import com.mc.app.hotel.common.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/8.
 */

public class CheckOutAdapter extends BaseAdapter {
    private Activity a;
    private List<CustomerInfo> data;
    Map<String, CustomerInfo> seletedItems = new HashMap<>();

    boolean isAllSelected = false;


    public CheckOutAdapter(Activity a, List<CustomerInfo> data) {
        this.a = a;
        this.data = data;
    }


    public void setData(List<CustomerInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        @BindView(R.id.checkbox)
        ImageView checkbox;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_phone_no)
        TextView tvPhoneNo;
        @BindView(R.id.tv_room_no)
        TextView tvRoomNo;
        @BindView(R.id.tv_arrive_day)
        TextView tvArriceDay;
        @BindView(R.id.tv_id_card)
        TextView tvIDCard;
        @BindView(R.id.tv_room_price)
        TextView tvRoomPrice;
        @BindView(R.id.ll_bg)
        LinearLayout llBG;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setView(CustomerInfo info, int position) {
            tvName.setText("姓名：" + StringUtil.getString(info.getCustomer()));
            tvPhoneNo.setText("电话：" + StringUtil.getString(info.getMobile()));
            tvRoomNo.setText(StringUtil.getString(info.getRoomNo()));
            tvIDCard.setText("身份证号：" + StringUtil.getString(info.getIdCard()));
            tvArriceDay.setText("来期：" + StringUtil.getString(DateUtils.string2stringNoS(info.getArriveDate())));
            if (!StringUtil.isBlank(info.getRoomPrice()) && !info.getRoomPrice().equals("0"))
                tvRoomPrice.setVisibility(View.VISIBLE);
            else
                tvRoomPrice.setVisibility(View.GONE);
            tvRoomPrice.setText("￥" + StringUtil.getString(info.getRoomPrice()));
            if (position % 2 == 0) {
                llBG.setBackgroundResource(R.color.custom_list_bg);
            } else {
                llBG.setBackgroundResource(R.color.white);
            }
            checkbox.setSelected(info.isChecked());
            checkbox.setSelected(seletedItems.containsKey(info.getCustomer()));
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        CustomerInfo info = data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(a).inflate(R.layout.layout_check_out, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.setView(info, position);
        return convertView;
    }


    public void addSeletedItem(CustomerInfo info) {
        seletedItems.put(info.getCustomer(), info);
        notifyDataSetChanged();

    }

    public void removeSeletedItem(CustomerInfo squadron) {
        seletedItems.remove(squadron.getCustomer());
        notifyDataSetChanged();
    }

    public void selectAllItems() {
        if (data == null) {
            return;
        }

        for (int i = 0; i < data.size(); i++) {
            CustomerInfo squadron = data.get(i);
            seletedItems.put(squadron.getCustomer(), squadron);
        }

        notifyDataSetChanged();
    }

    public void removeAllSeletedItems() {
        seletedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItemSeleted(CustomerInfo info) {
        if (seletedItems.containsKey(info.getCustomer())) {
            removeSeletedItem(info);
        } else {
            addSeletedItem(info);
        }
    }

    public void toggleAllItemsSelected(boolean selectAll) {
        this.isAllSelected = selectAll;
        if (isAllSelected) {
            selectAllItems();
        } else {
            removeAllSeletedItems();
        }
    }

    public List<CustomerInfo> getSelectedItems() {
        return seletedItems.values() == null ? null : new ArrayList<CustomerInfo>(seletedItems.values());
    }

    public String getSelectMasterid() {
        if (getSelectedItems() == null)
            return "";
        String mastreID = "";
        StringBuffer sb = new StringBuffer(mastreID);
        for (CustomerInfo info : getSelectedItems()) {
            sb.append(info.getMasterId() + ",");
        }
        mastreID = sb.toString().substring(0, sb.toString().length() - 1);
        return mastreID;
    }


}
