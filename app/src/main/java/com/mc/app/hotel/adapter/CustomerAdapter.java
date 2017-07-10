package com.mc.app.hotel.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.activity.ComparedFaceCardActivity;
import com.mc.app.hotel.bean.CustomerInfo;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/8.
 */

public class CustomerAdapter extends BaseAdapter {
    private Activity a;
    private List<CustomerInfo> data;

    public CustomerAdapter(Activity a, List<CustomerInfo> data) {
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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_phone_no)
        TextView tvPhoneNo;
        @BindView(R.id.tv_room_no)
        TextView tvRoomNo;
        @BindView(R.id.tv_sex)
        TextView tvSex;
        @BindView(R.id.tv_nation)
        TextView tvNation;
        @BindView(R.id.tv_id_number)
        TextView tvIdNumber;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_arrive_day)
        TextView tvArriveDay;
        @BindView(R.id.tv_leave_day)
        TextView tvLeaveDay;
        @BindView(R.id.btn_check)
        Button btnCheck;
        @BindView(R.id.rl_bg)
        RelativeLayout rlBg;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setView(CustomerInfo info, int position) {
            if (StringUtil.isBlank(info.getCustomer()))
                tvName.setVisibility(View.GONE);
            if (StringUtil.isBlank(info.getMobile()))
                tvPhoneNo.setVisibility(View.GONE);
            if (StringUtil.isBlank(info.getRoomNo()))
                tvRoomNo.setVisibility(View.GONE);
            if (StringUtil.isBlank(info.getSex()))
                tvSex.setVisibility(View.GONE);
            if (StringUtil.isBlank(info.getNation()))
                tvNation.setVisibility(View.GONE);
            if (StringUtil.isBlank(info.getIdCard()))
                tvIdNumber.setVisibility(View.GONE);
            if (StringUtil.isBlank(info.getAddress()))
                tvAddress.setVisibility(View.GONE);
            if (StringUtil.isBlank(info.getArriveDate()))
                tvArriveDay.setVisibility(View.GONE);
            if (StringUtil.isBlank(info.getLeaveDate()))
                tvLeaveDay.setVisibility(View.GONE);
            tvName.setText(info.getCustomer());
            tvPhoneNo.setText(info.getMobile());
            tvRoomNo.setText(info.getRoomNo());
            tvSex.setText(info.getSex());
            tvNation.setText(info.getNation());
            tvIdNumber.setText(info.getIdCard());
            tvAddress.setText(info.getAddress());
            tvArriveDay.setText("来期：" + info.getArriveDate());
            tvLeaveDay.setText("离期：" + info.getLeaveDate());
            btnCheck.setOnClickListener(new OnClickEvent(info));
            if (position % 2 == 0) {
                rlBg.setBackgroundResource(R.color.custom_list_bg);
            }else{
                rlBg.setBackgroundResource(R.color.white);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        CustomerInfo info = data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(a).inflate(R.layout.layout_custome_list, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.setView(info, position);
        return convertView;
    }

    class OnClickEvent implements View.OnClickListener {
        CustomerInfo info;

        public OnClickEvent(CustomerInfo info) {
            this.info = info;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(a, ComparedFaceCardActivity.class);
            Bundle b = new Bundle();
            b.putInt(Constants.MASTER_ID, info.getMasterId());
            i.putExtras(b);
            a.startActivity(i);
        }
    }
}
