package com.miaolian.facead.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaolian.facead.R;
import com.miaolian.facead.event.EventSelectLinkType;
import com.miaolian.facead.util.ServiceUtil;
import com.miaolian.facead.util.StateUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaofeng on 2017-03-02.
 */

public class SelectLinkTypeAdapter extends RecyclerView.Adapter<SelectLinkTypeAdapter.SelectLinkTypeViewHolder> {
    List<String> linkTypes = new ArrayList<>();

    public SelectLinkTypeAdapter(Context context) {
        linkTypes.add(ServiceUtil.OTG);
        linkTypes.add(ServiceUtil.OCR);
        linkTypes.add(ServiceUtil.CAMERA);
        linkTypes.add(ServiceUtil.SERIALPORT);
        if (StateUtil.SupportNFC) {
            linkTypes.add(ServiceUtil.NFC);
        }
    }

    @Override
    public SelectLinkTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectLinkTypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_linktype_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectLinkTypeViewHolder holder, int position) {
        holder.update(linkTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return linkTypes.size();
    }

    public class SelectLinkTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIv;
        TextView itemTv;
        String linkType = null;

        public SelectLinkTypeViewHolder(View itemView) {
            super(itemView);
            itemIv = (ImageView) itemView.findViewById(R.id.itemIv);
            itemTv = (TextView) itemView.findViewById(R.id.itemTv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventSelectLinkType(linkType));
                }
            });
        }

        public void update(String type) {
            linkType = type;
            switch (linkType) {
                case ServiceUtil.OTG:
                    itemIv.setImageResource(R.drawable.ic_otg);
                    itemTv.setText(R.string.OTG_READ_CARD);
                    break;
                case ServiceUtil.OCR:
                    itemIv.setImageResource(R.drawable.ic_ocr);
                    itemTv.setText(R.string.OCR_READ_CARD);
                    break;
                case ServiceUtil.NFC:
                    itemIv.setImageResource(R.drawable.ic_nfc);
                    itemTv.setText(R.string.NFC_READ_CARD);
                    break;
                case ServiceUtil.SERIALPORT:
                    itemIv.setImageResource(R.drawable.ic_serialport);
                    itemTv.setText(R.string.SERIALPORT_READ_CARD);
                    break;
                case ServiceUtil.CAMERA:
                    itemIv.setImageResource(R.drawable.ic_camera);
                    itemTv.setText(R.string.CAMERA_READ_CARD);
                    break;
                default:
                    break;
            }
        }
    }
}
