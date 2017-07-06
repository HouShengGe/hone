package com.mc.app.hotel.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mc.app.hotel.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class StringAdapter extends BaseAdapter {
    private Activity a;
    private List<String> data;

    public StringAdapter(Activity a, List<String> data) {
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
        TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        String s = data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(a).inflate(R.layout.layout_string, null);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv.setText(s);
        return convertView;
    }
}
