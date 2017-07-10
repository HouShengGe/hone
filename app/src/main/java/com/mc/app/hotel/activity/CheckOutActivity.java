package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.mc.app.hotel.R;
import com.mc.app.hotel.adapter.CheckOutAdapter;
import com.mc.app.hotel.bean.CustomerBean;
import com.mc.app.hotel.bean.CustomerInfo;
import com.mc.app.hotel.bean.CustomerParamsInfo;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.util.ToastUtils;
import com.mc.app.hotel.common.view.pulltoreflushgrid.ILoadingLayout;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshBase;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/7.
 */

public class CheckOutActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.lv_customer_list)
    PullToRefreshListView mPullRefreshListView;
    @BindView(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @BindView(R.id.btn_check_out)
    Button btnCheckOut;


    List<CustomerInfo> custList = new ArrayList<>();

    private int roomType = 1;//0：全部；1：在住；2：历史
    private int pageNo = 1;

    CheckOutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        ButterKnife.bind(this);
        // 初始化数据和数据源
        init();
        initIndicator();
        buckButton(true);
        cbSelectAll.setOnCheckedChangeListener(this);
    }

    private void init() {
        setTitle("申报离开");
        loadData();
        checkOut();
//        checkChange();
        initListView();
    }

    private void initListView() {
        adapter = new CheckOutAdapter(this, custList);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(this);
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerInfo squadron = custList.get(position);
                adapter.toggleItemSeleted(squadron);

            }
        });
    }

    private static final String TAG = "SearchCustomerActivity";

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
                subscribe(new RxSubscribeProgress<CustomerBean>(CheckOutActivity.this, false) {
                    @Override
                    protected void onOverNext(CustomerBean t) {
                        pageNo++;
                        custList.addAll(t.getCusts());
                        if (t.getRecordNums() == custList.size())
                            mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                        else
                            mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        mPullRefreshListView.onRefreshComplete();
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

    private void flushList() {
        pageNo = 1;
        custList.clear();
        loadData();
    }

    private void checkOut() {

        RxView.clicks(btnCheckOut)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Api.getInstance().mApiService.checkOut(Params.getCheckOutParams(adapter.getSelectMasterid()))
                                .compose(RxSubscribeThread.<String>ioAndMain()).
                                subscribe(new RxSubscribeProgress<String>(CheckOutActivity.this) {
                                    @Override
                                    protected void onOverNext(String t) {
                                        flushList();
                                        cbSelectAll.setChecked(false);
                                        adapter.toggleAllItemsSelected(false);
                                    }

                                    @Override
                                    protected void onOverError(String message) {
                                        ToastUtils.show(CheckOutActivity.this, message, Toast.LENGTH_SHORT);
                                    }
                                });
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        if (view.getId() == R.id.cb_select_all) {
            adapter.toggleAllItemsSelected(isChecked);
        }

    }

}
