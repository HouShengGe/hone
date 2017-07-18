package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.mc.app.hotel.R;
import com.mc.app.hotel.adapter.PoliceListAdapter;
import com.mc.app.hotel.bean.HotelBean;
import com.mc.app.hotel.bean.HotelInfo;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.view.pulltoreflushgrid.ILoadingLayout;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshBase;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/11.
 */

public class PoliceMainActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.lv_customer_list)
    PullToRefreshListView mPullRefreshListView;

    PoliceListAdapter adapter;

    List<HotelInfo> hotelList = new ArrayList<>();
    private int pageNo = 1;

    private String hotelName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_main);
        ButterKnife.bind(this);
        // 初始化数据和数据源
        init();
        initIndicator();
        adapter = new PoliceListAdapter(this, hotelList);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(this);
    }

    private void init() {
        setTitle("在住查询");
        leftTitle("民宿实名申报");
        rightTitle(R.drawable.user);
        buckButton(false);
        getHotelList();
        etSearch.addTextChangedListener(new EditChangedListener());
    }

    @Override
    public void rightClick() {
        super.rightClick();
        toNextActivity(PersonCenterActivity.class);
    }

    private void initIndicator() {

        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载更多...");
        endLabels.setRefreshingLabel("正在加载...");
        endLabels.setReleaseLabel("放手加载更多...");
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    private void getHotelList() {

        Api.getInstance().mApiService.getHotelList(Params.getHotelListParams(hotelName, pageNo))
                .compose(RxSubscribeThread.<HotelBean>ioAndMain()).
                subscribe(new RxSubscribeProgress<HotelBean>(PoliceMainActivity.this, false) {
                    @Override
                    protected void onOverNext(HotelBean t) {
                        pageNo++;
                        hotelList.addAll(t.getStores());
                        if (t.getRecordNums() == hotelList.size())
                            mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                        else
                            mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        mPullRefreshListView.onRefreshComplete();
                        adapter.setData(hotelList);
                    }

                    @Override
                    protected void onOverError(String message) {
                        mPullRefreshListView.onRefreshComplete();

                    }
                });
    }

    class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            hotelName = s.toString();
            pageNo = 1;
            getHotelList();
            hotelList.clear();
        }
    }

    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            showToast("再按一次退出程序");
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序
            this.finish();
            //   System.exit(0);
        }
    }
}
