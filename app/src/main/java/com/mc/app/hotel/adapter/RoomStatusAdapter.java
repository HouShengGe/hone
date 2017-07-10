package com.mc.app.hotel.adapter;

import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.RoomStatusInfo;
import com.mc.app.hotel.common.util.DrawableUtils;
import com.mc.app.hotel.common.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */

public class RoomStatusAdapter extends BaseAdapter {

    List<RoomStatusInfo> list;
    Activity a;

    public RoomStatusAdapter(Activity a, List<RoomStatusInfo> list) {
        this.list = list;
        this.a = a;
    }

    public void setData(List<RoomStatusInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        RelativeLayout rlRoomStatus;
        TextView tvRoomNum;
        TextView tvRoomrvType;
        TextView tvRoomrvCust;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        RoomStatusInfo bean = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(a).inflate(R.layout.layout_room_status, null);
            vh = new ViewHolder();
            vh.rlRoomStatus = (RelativeLayout) convertView.findViewById(R.id.rl_room_bg);
            vh.tvRoomNum = (TextView) convertView.findViewById(R.id.tv_room_num);
            vh.tvRoomrvType = (TextView) convertView.findViewById(R.id.tv_room_type);
            vh.tvRoomrvCust = (TextView) convertView.findViewById(R.id.tv_room_cust);
            int pwidth = parent.getWidth();
            int width = (pwidth - 14) / 3;
            int hight = width * 4 / 5;
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, hight);
            vh.rlRoomStatus.setLayoutParams(param);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.rlRoomStatus.setBackground(DrawableUtils.getDrawable(bean.getRoomColor()));
        vh.tvRoomNum.setText(bean.getRoomNo());
        vh.tvRoomrvType.setText(bean.getRoomTypeName());
        vh.tvRoomrvCust.setText(bean.getCustName_1() + "\n" + bean.getCustName_2());
        if (!StringUtil.isBlank(bean.getCustName_2())) {
            vh.tvRoomrvCust.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
        return convertView;
    }
}
