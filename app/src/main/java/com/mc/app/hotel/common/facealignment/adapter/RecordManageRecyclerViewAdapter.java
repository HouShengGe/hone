package com.mc.app.hotel.common.facealignment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.event.EventSelectRecord;
import com.mc.app.hotel.common.facealignment.model.FaceRecord;
import com.mc.app.hotel.common.facealignment.util.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaofeng on 2017-04-25.
 */

public class RecordManageRecyclerViewAdapter extends RecyclerView.Adapter<RecordManageRecyclerViewAdapter.RecordManageRecyclerViewHolder> {
    List<DataWrapper<FaceRecord>> dataWrappers = new LinkedList<>();
    DataWrapper<?> selectedItem = null;

    @Override
    public RecordManageRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordManageRecyclerViewHolder(new WeakReference<RecordManageRecyclerViewAdapter>(this), LayoutInflater.from(parent.getContext()).inflate(R.layout.record_manage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecordManageRecyclerViewHolder holder, int position) {
        holder.bind(dataWrappers.get(position));
    }

    @Override
    public int getItemCount() {
        return dataWrappers.size();
    }

    public void clear() {
        dataWrappers.clear();
        notifyDataSetChanged();
    }

    public void add(List<FaceRecord> faceRecords) {
        dataWrappers.clear();
        int upLimit = faceRecords.size();
        for (int i = 0; i < upLimit; ++i) {
            FaceRecord faceRecord = faceRecords.get(i);
            DataWrapper<FaceRecord> dataWrapper = new DataWrapper<>(i, faceRecord, false);
            dataWrappers.add(dataWrapper);
        }
        notifyDataSetChanged();
    }

    public void selectItem(DataWrapper<?> dataWrapper) {
        if (selectedItem != null) {
            selectedItem.setSelected(false);
            notifyItemChanged(selectedItem.getPosition());
        }
        dataWrapper.setSelected(true);
        selectedItem = dataWrapper;
        notifyItemChanged(selectedItem.getPosition());
    }


    public static class RecordManageRecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recordTimeTv)
        TextView recordTimeTv;
        @BindView(R.id.similarityTv)
        TextView similarityTv;
        @BindView(R.id.idNumberTv)
        TextView idNumberTv;
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.sexTv)
        TextView sexTv;
        @BindView(R.id.birthdayTv)
        TextView birthdayTv;
        @BindView(R.id.nationTv)
        TextView nationTv;
        @BindView(R.id.addressTv)
        TextView addressTv;
        @BindView(R.id.issueAuthorityTv)
        TextView issueAuthorityTv;
        @BindView(R.id.termBeginTv)
        TextView termBeginTv;
        @BindView(R.id.termEndTv)
        TextView termEndTv;
        DataWrapper<FaceRecord> dataWrapper;
        WeakReference<RecordManageRecyclerViewAdapter> adapterWeakReference;


        public RecordManageRecyclerViewHolder(WeakReference<RecordManageRecyclerViewAdapter> reference, View itemView) {
            super(itemView);
            ButterKnife.bind(this, this.itemView);
            adapterWeakReference = reference;
            dataWrapper = null;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataWrapper == null) return;
                    RecordManageRecyclerViewAdapter adapter = adapterWeakReference.get();
                    if (adapter != null) {
                        adapter.selectItem(dataWrapper);
                    }
                    EventBus.getDefault().post(new EventSelectRecord(dataWrapper.getData()));
                }
            });
        }

        public void bind(DataWrapper<FaceRecord> dataWrapper) {
            this.dataWrapper = dataWrapper;
            if (dataWrapper.isSelected()) {
                itemView.setBackgroundResource(android.R.color.holo_orange_light);
            } else {
                itemView.setBackgroundResource(dataWrapper.getPosition()%2==0? R.color.colorTableRowEven: R.color.colorTableRowOdd);
            }
            recordTimeTv.setText(TimeUtil.getDateStringFrom(dataWrapper.getData().getRecordTime()));
            similarityTv.setText(String.valueOf(dataWrapper.getData().getSimilarity()));
            idNumberTv.setText(dataWrapper.getData().getIdNumber());
            nameTv.setText(dataWrapper.getData().getName());
            sexTv.setText(dataWrapper.getData().getSex());
            birthdayTv.setText(dataWrapper.getData().getBirthday());
            nationTv.setText(dataWrapper.getData().getNation());
            addressTv.setText(dataWrapper.getData().getAddress());
            issueAuthorityTv.setText(dataWrapper.getData().getIssueAuthority());
            termBeginTv.setText(dataWrapper.getData().getTermBegin());
            termEndTv.setText(dataWrapper.getData().getTermEnd());
        }
    }

    public static class DataWrapper<T> {
        public T data;
        public boolean selected;
        public int position;

        public DataWrapper(int position, T data, boolean selected) {
            this.data = data;
            this.selected = selected;
            this.position = position;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DataWrapper) {
                return position == ((DataWrapper<?>) obj).getPosition();
            } else {
                return false;
            }
        }
    }
}
