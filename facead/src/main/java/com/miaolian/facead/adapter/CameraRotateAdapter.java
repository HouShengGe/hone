package com.miaolian.facead.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miaolian.facead.R;
import com.miaolian.facead.event.EventSelectCameraRotate;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user1 on 2017/5/16.
 */

public class CameraRotateAdapter extends RecyclerView.Adapter<CameraRotateAdapter.CameraRotateViewHolder> {
    public static final int ROTATE_0 = 0;
    public static final int ROTATE_90 = 90;
    public static final int ROTATE_180 = 180;
    public static final int ROTATE_270 = 270;
    int[] data = {ROTATE_0, ROTATE_90, ROTATE_180, ROTATE_270};

    @Override
    public CameraRotateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CameraRotateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rotate_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CameraRotateViewHolder holder, int position) {
        holder.update(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class CameraRotateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTv)
        TextView itemTv;
        int degree = 0;

        public CameraRotateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, this.itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventSelectCameraRotate(degree));
                }
            });
        }

        public void update(int degree) {
            this.degree = degree;
            itemTv.setText(degree + " °");
        }
    }
}
