package com.mc.app.hotel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.adapter.RoomStatusAdapter;
import com.mc.app.hotel.bean.RoomDetialBean;
import com.mc.app.hotel.bean.RoomStatusBean;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.facealignment.FaceAilgmentActivity;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.view.DialogRoomStatu;
import com.mc.app.hotel.common.view.pulltoreflushgrid.ILoadingLayout;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshBase;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshGridView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/2.
 */

public class RoomStatusActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {
    @BindView(R.id.pull_refresh_grid)
    PullToRefreshGridView mPullRefreshListView;

    private RoomStatusAdapter adapter;
    private RoomStatusBean statusBean = new RoomStatusBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_srtatus);
        ButterKnife.bind(this);
        getRoomStatus();
        initIndicator();
        buckButton(true);
        setTitle("房态图");
        adapter = new RoomStatusAdapter(this, statusBean.getRooms());
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(this);
        mPullRefreshListView.setOnItemClickListener(this);
    }

    private void initIndicator() {
        ILoadingLayout startLabels = mPullRefreshListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放手刷新数据...");// 下来达到一定距离时，显示的提示

    }


    private void getRoomStatus() {

        Api.getInstance().mApiService.getRoomStatus(Params.getParams())
                .compose(RxSubscribeThread.<RoomStatusBean>ioAndMain()).
                subscribe(new RxSubscribeProgress<RoomStatusBean>(RoomStatusActivity.this, false) {
                    @Override
                    protected void onOverNext(RoomStatusBean t) {
                        statusBean = t;
                        adapter.setData(t.getRooms());
                        mPullRefreshListView.onRefreshComplete();
                    }

                    @Override
                    protected void onOverError(String message) {
                        mPullRefreshListView.onRefreshComplete();

                    }
                });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(
                getApplicationContext(),
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        refreshView.getLoadingLayoutProxy()
                .setLastUpdatedLabel(label);
        getRoomStatus();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String roomNO = statusBean.getRooms().get(position).getRoomNo();
        int storeId = statusBean.getRooms().get(position).getStoreId();
        final String roomStr = statusBean.getRooms().get(position).getRoomSta();

        Api.getInstance().mApiService.getRoomDetial(Params.getRoomDetialParams(roomNO, storeId))
                .compose(RxSubscribeThread.<RoomDetialBean>ioAndMain()).
                subscribe(new RxSubscribeProgress<RoomDetialBean>(RoomStatusActivity.this, false) {
                    @Override
                    protected void onOverNext(RoomDetialBean t) {
                        if (roomStr.equals("VC") || roomStr.equals("VD")) {
                            Bundle b = new Bundle();
                            b.putString(Constants.ROOM_NO, roomNO);
                            toNextActivity(FaceAilgmentActivity.class, b);
                        } else {
                            showRoomDetialDialog(t);
                        }
                    }

                    @Override
                    protected void onOverError(String message) {

                    }
                });
    }

    DialogRoomStatu dialogs;

    private void showRoomDetialDialog(RoomDetialBean bean) {
        dialogs = new DialogRoomStatu(this);
        dialogs.setData(bean);
    }
}
