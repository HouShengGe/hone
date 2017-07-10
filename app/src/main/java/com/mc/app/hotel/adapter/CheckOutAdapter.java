package com.mc.app.hotel.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.CustomerInfo;

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

//    OnItemsSelectedChangeListener<CustomerInfo> changeListener;

//    public CheckOutAdapter(List<CustomerInfo> dataList, Activity context,
//                           OnItemsSelectedChangeListener<CustomerInfo> listener) {
//        super();
//        this.a = context;
//        this.data = dataList;
////        this.changeListener = listener;
//    }

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


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setView(CustomerInfo info, int position) {
            tvName.setText(info.getCustomer());
            tvPhoneNo.setText(info.getMobile());
            tvRoomNo.setText(info.getRoomNo());
            tvIDCard.setText(info.getIdCard());
            tvArriceDay.setText(info.getArriveDate());
            checkbox.setSelected(info.isChecked());
            checkbox.setSelected(seletedItems.containsKey(info.getCustomer()));
            checkbox.setOnClickListener(new CheckChangeEvent(info, position));
        }
    }


    class CheckChangeEvent implements View.OnClickListener {
        int position;
        CustomerInfo info;

        public CheckChangeEvent(CustomerInfo info, int position) {
            this.info = info;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            info.setChecked(!info.isChecked());

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


    //    public interface OnItemsSelectedChangeListener<T> {
//        public void onItemsSelectedChanged(boolean selected, List<T> squadrons);
//    }
    public void addSeletedItem(CustomerInfo info) {
        seletedItems.put(info.getCustomer(), info);
        notifyDataSetChanged();
//        if (changeListener != null) {
//            changeListener.onItemsSelectedChanged(true, getSelectedItems());
//        }

    }

    public void removeSeletedItem(CustomerInfo squadron) {
        seletedItems.remove(squadron.getCustomer());
//        changeListener.onItemsSelectedChanged(false, getSelectedItems());
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
//        if (changeListener != null) {
//            changeListener.onItemsSelectedChanged(true, getSelectedItems());
//        }
    }

    public void removeAllSeletedItems() {
        seletedItems.clear();
        notifyDataSetChanged();
//        if (changeListener != null) {
//            changeListener.onItemsSelectedChanged(false, getSelectedItems());
//        }
    }

    public void toggleItemSeleted(CustomerInfo info) {
        if (seletedItems.containsKey(info.getCustomer())) {
            removeSeletedItem(info);
        } else {
            addSeletedItem(info);
        }
    }

    public void toggleAllItemsSelected(boolean selectAll) {
        Log.d(TAG, "toggleAllItemsSelected: " + selectAll);
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
        Log.d(TAG, "getSelectMasterid: " + mastreID);
        return mastreID;
    }

    private static final String TAG = "CheckOutAdapter";
//    public boolean hasMoreThanOneItemSelected() {
//        return getSeletedItemsAmount() > 1;
//    }

    public int getSeletedItemsAmount() {
        return seletedItems.keySet().size();
    }

//    public boolean hasSeletedItems() {
//        return getSeletedItemsAmount() > 0;
//    }
}
