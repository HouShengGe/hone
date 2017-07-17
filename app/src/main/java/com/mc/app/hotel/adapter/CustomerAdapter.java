package com.mc.app.hotel.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.activity.ComparedFaceCardActivity;
import com.mc.app.hotel.bean.CustomerInfo;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.util.Identity;
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
    private int type;

    public CustomerAdapter(Activity a, List<CustomerInfo> data, int type) {
        this.a = a;
        this.data = data;
        this.type = type;
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
        LinearLayout rlBg;
        @BindView(R.id.tv_room_price)
        TextView tvRoomPrice;
        @BindView(R.id.tv_birthday)
        TextView tvBirthday;

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
            tvName.setText(StringUtil.getString(info.getCustomer()));
            tvPhoneNo.setText(StringUtil.getString(info.getMobile()));
            tvRoomNo.setText(StringUtil.getString(info.getRoomNo()));
            tvSex.setText(StringUtil.getString(info.getSex()));
            tvNation.setText(StringUtil.getString(info.getNation()));
            tvIdNumber.setText(StringUtil.getString(info.getIdCard()));
            tvAddress.setText(StringUtil.getString(info.getAddress()));
            tvRoomPrice.setText(StringUtil.getString(info.getRoomPrice()));
            tvBirthday.setText(Identity.getBirthday(info.getIdCard()));
            tvArriveDay.setText("来期：" + info.getArriveDate());
            String leaveDay = type == 1 ? "预离：" : "离期：";
            tvLeaveDay.setText(leaveDay + info.getLeaveDate());
            btnCheck.setOnClickListener(new OnClickEvent(info));
            if (position % 2 == 0) {
                rlBg.setBackgroundResource(R.color.custom_list_bg);
            } else {
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
