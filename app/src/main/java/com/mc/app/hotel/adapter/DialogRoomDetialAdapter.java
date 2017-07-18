package com.mc.app.hotel.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.RoomDetialInfo;
import com.mc.app.hotel.common.facealignment.util.DateUtils;
import com.mc.app.hotel.common.util.Identity;
import com.mc.app.hotel.common.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */

public class DialogRoomDetialAdapter extends BaseAdapter {
    private Activity a;
    private List<RoomDetialInfo> data;

    public DialogRoomDetialAdapter(Activity a, List<RoomDetialInfo> data) {
        this.a = a;
        this.data = data;
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
        TextView tvCustomer;
        TextView tvCuntry;
        TextView tvArrive;
        TextView tvLeave;
        TextView tvIdCard;
        TextView tvPhoneNo;
        TextView tvAddr;
        TextView tvSex;
        TextView tvBirthday;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        RoomDetialInfo info = data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(a).inflate(R.layout.layout_dialog_room_msg, null);
            vh = new ViewHolder();
            vh.tvCustomer = (TextView) convertView.findViewById(R.id.tv_room_customer);
            vh.tvCuntry = (TextView) convertView.findViewById(R.id.tv_room_cuntry);
            vh.tvArrive = (TextView) convertView.findViewById(R.id.tv_room_arrive);
            vh.tvLeave = (TextView) convertView.findViewById(R.id.tv_room_leave);
            vh.tvIdCard = (TextView) convertView.findViewById(R.id.tv_room_id_card);
            vh.tvPhoneNo = (TextView) convertView.findViewById(R.id.tv_phone_no);
            vh.tvAddr = (TextView) convertView.findViewById(R.id.tv_addr);
            vh.tvSex = (TextView) convertView.findViewById(R.id.tv_room_sex);
            vh.tvBirthday = (TextView) convertView.findViewById(R.id.tv_birthday);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvCustomer.setText(StringUtil.getString(info.getCustomer()));
        vh.tvCuntry.setText(StringUtil.getString(info.getNation()));
        vh.tvArrive.setText(StringUtil.getString(DateUtils.string2stringNoS(info.getArriveDate())));
        vh.tvLeave.setText(StringUtil.getString(DateUtils.string2stringNoS(info.getLeaveDate())));
        vh.tvIdCard.setText(StringUtil.getString(info.getIdCard()));
        vh.tvPhoneNo.setText(StringUtil.getString(info.getMobile()));
        vh.tvAddr.setText(StringUtil.getString(info.getAddress()));
        vh.tvSex.setText(StringUtil.getString(info.getSex()));
        vh.tvBirthday.setText(Identity.getBirthday(info.getIdCard()));
        return convertView;
    }
}
