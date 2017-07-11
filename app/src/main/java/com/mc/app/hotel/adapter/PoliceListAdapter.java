package com.mc.app.hotel.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.activity.SearchCustomerActivity;
import com.mc.app.hotel.bean.HotelInfo;
import com.mc.app.hotel.common.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/11.
 */

public class PoliceListAdapter extends BaseAdapter {
    private Activity a;
    private List<HotelInfo> data;

    public PoliceListAdapter(Activity a, List<HotelInfo> data) {
        this.a = a;
        this.data = data;
    }

    public void setData(List<HotelInfo> data) {
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
        @BindView(R.id.tv_name_hotel)
        TextView tvNameHotel;
        @BindView(R.id.tv_customer_no)
        TextView tvCustomerNo;
        @BindView(R.id.btn_detial)
        Button tvDetial;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setView(HotelInfo info) {
            tvNameHotel.setText(info.getStoreName());
            tvCustomerNo.setText(info.getCheckInNum() + "");
            tvDetial.setOnClickListener(new OnClickEvent(info));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        HotelInfo info = data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(a).inflate(R.layout.layout_police_main, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.setView(info);
        return convertView;
    }

    class OnClickEvent implements View.OnClickListener {
        HotelInfo info;

        public OnClickEvent(HotelInfo info) {
            this.info = info;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(a, SearchCustomerActivity.class);
            Bundle b = new Bundle();
            b.putInt(Constants.STORE_ID, info.getStoreId());
            b.putInt(Constants.CUSTOMER_STATUS, 1);
            i.putExtras(b);
            a.startActivity(i);
        }
    }
}
