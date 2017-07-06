package com.mc.app.hotel.common.facealignment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.event.EventChooseCamera;
import com.mc.app.hotel.common.facealignment.util.CameraUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaofeng on 2017-03-10.
 */

public class ChooseCameraAdapter extends RecyclerView.Adapter<ChooseCameraAdapter.ChooseCameraRecyclerViewHolder> {

    List<CameraUtil.CameraType> cameraTypes;

    public ChooseCameraAdapter() {
        cameraTypes = new LinkedList<>();
        cameraTypes.add(CameraUtil.CameraType.FRONT_CAMERA);
        cameraTypes.add(CameraUtil.CameraType.BACK_CAMERA);
        cameraTypes.add(CameraUtil.CameraType.ANYONE);
    }

    @Override
    public ChooseCameraRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChooseCameraRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_camera_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChooseCameraRecyclerViewHolder holder, int position) {
        holder.update(cameraTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return cameraTypes.size();
    }

    public class ChooseCameraRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIv;
        TextView itemTv;
        CameraUtil.CameraType cameraType;

        public ChooseCameraRecyclerViewHolder(View itemView) {
            super(itemView);
            itemIv = (ImageView) itemView.findViewById(R.id.itemIv);
            itemTv = (TextView) itemView.findViewById(R.id.itemTv);
            cameraType = CameraUtil.CameraType.FRONT_CAMERA;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventChooseCamera(cameraType));
                }
            });
        }

        public void update(CameraUtil.CameraType cameraType) {
            this.cameraType = cameraType;
            switch (cameraType) {
                case FRONT_CAMERA:
                    itemIv.setImageResource(R.drawable.ic_front_camera);
                    itemTv.setText(R.string.FRONT_CAMERA);
                    break;
                case BACK_CAMERA:
                    itemIv.setImageResource(R.drawable.ic_back_camera);
                    itemTv.setText(R.string.BACK_CAMERA);
                    break;
                case ANYONE:
                    itemIv.setImageResource(R.drawable.ic_camera);
                    itemTv.setText(R.string.ANY_CAMERA);
                    break;
            }
        }


    }
}
