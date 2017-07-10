package com.mc.app.hotel.common.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.adapter.DialogRoomDetialAdapter;
import com.mc.app.hotel.bean.RoomDetialBean;

/**
 * Created by Administrator on 2017/7/10.
 */

public class DialogRoomStatu {
    private Activity a;
    private AlertDialog dialog;
    public ListView listView;


    public DialogRoomStatu(Activity a) {
        try {
            if (a != null && !a.isFinishing()) {
                this.a = a;
                View view = LayoutInflater.from(a).inflate(R.layout.dialog_room_msg, null);
                dialog = new AlertDialog.Builder(a).setView(view).create();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_room_msg);
                listView = (ListView) window.findViewById(R.id.lv_dia_room_msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData(RoomDetialBean bean) {
        View view = LayoutInflater.from(a).inflate(R.layout.layout_dialog_room_head, null);
        TextView tvRoomNum = (TextView) view.findViewById(R.id.tv_room_no);
        TextView tvRoomType = (TextView) view.findViewById(R.id.tv_room_type);
        tvRoomNum.setText(bean.getRoomNo());
        tvRoomType.setText(bean.getRoomType());
        listView.addHeaderView(view);
        DialogRoomDetialAdapter adapter = new DialogRoomDetialAdapter(a, bean.getCusts());
        listView.setAdapter(adapter);

    }

    public void dismiss() {
        try {
            if (a != null && !a.isFinishing()) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        } catch (Exception e) {
        }
    }

    public void setCanceledOnTouchOutside(boolean flag) {
        try {
            if (a != null && !a.isFinishing()) {
                dialog.setCanceledOnTouchOutside(flag);
            }
        } catch (Exception e) {
        }
    }
}
