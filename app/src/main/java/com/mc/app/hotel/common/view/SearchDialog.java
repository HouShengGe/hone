package com.mc.app.hotel.common.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.CustomerParamsInfo;

public class SearchDialog {
    private Activity a;
    private AlertDialog dialog;
    public EditText etName;
    public EditText etIDCard;
    public EditText etRoomNO;
    public EditText etPhoneNO;
    public Button btnSearch;

    public SearchDialog(Activity a) {
        try {
            if (a != null && !a.isFinishing()) {
                this.a = a;
                View view = LayoutInflater.from(a).inflate(R.layout.dialog_search_cust, null);
                dialog = new TransparentDialog.Builder(a).setView(view).create();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_search_cust);
                window.setBackgroundDrawable(a.getResources().getDrawable(R.drawable.tr));
                etName = (EditText) window.findViewById(R.id.et_name);
                etIDCard = (EditText) window.findViewById(R.id.et_id_card);
                etRoomNO = (EditText) window.findViewById(R.id.et_room_no);
                etPhoneNO = (EditText) window.findViewById(R.id.et_phone_no);
                btnSearch = (Button) window.findViewById(R.id.btn_search);
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


    public void setOnDialogListeners(View.OnClickListener listener) {
        try {
            if (a != null && !a.isFinishing()) {
                btnSearch.setOnClickListener(listener);
            }
        } catch (Exception e) {
        }
    }

    public CustomerParamsInfo getSearchInfo() {
        CustomerParamsInfo info = new CustomerParamsInfo();
        String name = etName.getText().toString().trim();
        info.setCustName(name);
        String idCard = etIDCard.getText().toString().trim();
        info.setIdCard(idCard);
        String roomNo = etRoomNO.getText().toString().trim();
        info.setRoomNo(roomNo);
        String phoneNo = etPhoneNO.getText().toString().trim();
        info.setMobile(phoneNo);

        return info;
    }
}
