package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.text.format.DateUtils;

import com.mc.app.hotel.R;
import com.mc.app.hotel.adapter.RoomStatusAdapter;
import com.mc.app.hotel.bean.RoomStatusBean;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.view.pulltoreflushgrid.ILoadingLayout;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshBase;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshGridView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/2.
 */

public class RoomStatusActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {
    @BindView(R.id.pull_refresh_grid)
    PullToRefreshGridView mPullRefreshListView;

    private RoomStatusAdapter adapter;
    private RoomStatusBean statusBean = new RoomStatusBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_srtatus);
        ButterKnife.bind(this);
        // 初始化数据和数据源
        getRoomStatus();
        initIndicator();
        buckButton(true);
        setTitle("房态图");
        adapter = new RoomStatusAdapter(this, statusBean.getRooms());
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(this);
    }

    private void initIndicator() {
        ILoadingLayout startLabels = mPullRefreshListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放手刷新数据...");// 下来达到一定距离时，显示的提示

//        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(
//                false, true);
//        endLabels.setPullLabel("你可劲拉，拉2...");// 刚下拉时，显示的提示
//        endLabels.setRefreshingLabel("好嘞，正在刷新2...");// 刷新时
//        endLabels.setReleaseLabel("你敢放，我就敢刷新2...");// 下来达到一定距离时，显示的提示
    }


    private void getRoomStatus() {

        Api.getInstance().mApiService.getRoomStatus(Params.getParams())
                .compose(RxSubscribeThread.<RoomStatusBean>ioAndMain()).
                subscribe(new RxSubscribeProgress<RoomStatusBean>(RoomStatusActivity.this, false) {
                    @Override
                    protected void onOverNext(RoomStatusBean t) {
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
}
