package com.mc.app.hotel.common.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.adapter.StringAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class DialogListView {
    private Activity a;
    private AlertDialog dialog;
    private ListView lv;

    private AdapterView.OnItemClickListener itemCilckListener;
    private List<String> sourceData;

    public DialogListView(Activity a, List<String> data, AdapterView.OnItemClickListener itemCilckListener) {
        try {
            if (a != null && !a.isFinishing()) {
                this.a = a;
                sourceData = data;
                this.itemCilckListener = itemCilckListener;
                View view = LayoutInflater.from(a).inflate(R.layout.dialog_listview, null);
                dialog = new AlertDialog.Builder(a).setView(view).create();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_listview);
                lv = (ListView) window.findViewById(R.id.dia_lv);
                StringAdapter adapter = new StringAdapter(a, sourceData);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(itemCilckListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
