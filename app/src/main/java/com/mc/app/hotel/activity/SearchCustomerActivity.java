package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.view.View;

import com.mc.app.hotel.R;
import com.mc.app.hotel.adapter.CustomerAdapter;
import com.mc.app.hotel.bean.CustomerBean;
import com.mc.app.hotel.bean.CustomerInfo;
import com.mc.app.hotel.bean.CustomerParamsInfo;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.view.SearchDialog;
import com.mc.app.hotel.common.view.pulltoreflushgrid.ILoadingLayout;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshBase;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/7.
 */

public class SearchCustomerActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {
    @BindView(R.id.lv_customer_list)
    PullToRefreshListView mPullRefreshListView;
    List<CustomerInfo> custList = new ArrayList<>();

    private int roomType = 0;//0：全部；1：在住；2：历史
    private int pageNo = 1;

    CustomerAdapter adapter;
    SearchDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);
        ButterKnife.bind(this);
        // 初始化数据和数据源
        init();
        initIndicator();
        buckButton(true);
        adapter = new CustomerAdapter(this, custList);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(this);
    }

    private void init() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            roomType = getIntent().getIntExtra(Constants.CUSTOMER_STATUS, 0);
        }
        rightTitle(R.drawable.find);
        if (roomType == 0)
            setTitle("全部住客");
        else if (roomType == 1)
            setTitle("在住列表");
        else
            setTitle("历史住客");
        loadData();
    }

    private static final String TAG = "SearchCustomerActivity";

    @Override
    public void rightClick() {
        showDialog();
    }

    private void showDialog() {
        dialog = new SearchDialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDialogListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData(dialog.getSearchInfo());
            }
        });

    }

    private void initIndicator() {

        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载更多...");
        endLabels.setRefreshingLabel("正在加载...");
        endLabels.setReleaseLabel("放手加载更多...");
    }


    private void getCustomerList(CustomerParamsInfo info) {
        Api.getInstance().mApiService.getCustomerList(Params.getCustomerListParams(info))
                .compose(RxSubscribeThread.<CustomerBean>ioAndMain()).
                subscribe(new RxSubscribeProgress<CustomerBean>(SearchCustomerActivity.this, false) {
                    @Override
                    protected void onOverNext(CustomerBean t) {
                        pageNo++;
                        custList.addAll(t.getCusts());
                        if (t.getRecordNums() == custList.size())
                            mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                        else
                            mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        mPullRefreshListView.onRefreshComplete();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        adapter.setData(custList);
                    }

                    @Override
                    protected void onOverError(String message) {
                        mPullRefreshListView.onRefreshComplete();

                    }
                });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        loadData();
    }

    CustomerParamsInfo paramsInfo = new CustomerParamsInfo();

    private void loadData() {
        paramsInfo.setQtype(roomType);
        paramsInfo.setPageIndex(pageNo);
        getCustomerList(paramsInfo);
    }

    private void searchData(CustomerParamsInfo info) {
        paramsInfo.setCustomerParamsInfo(info);
        custList.clear();
        pageNo = 1;
        loadData();
    }

}