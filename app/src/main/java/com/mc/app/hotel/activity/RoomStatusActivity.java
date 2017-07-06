package com.mc.app.hotel.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.view.pulltoreflushgrid.ILoadingLayout;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshBase;
import com.mc.app.hotel.common.view.pulltoreflushgrid.PullToRefreshGridView;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/2.
 */

public class RoomStatusActivity extends BaseActivity {
    @BindView(R.id.pull_refresh_grid)
    PullToRefreshGridView mPullRefreshListView;
    private LinkedList<String> mListItems;
    private ArrayAdapter<String> mAdapter;

    private int mItemCount = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_srtatus);
        ButterKnife.bind(this);
        // 初始化数据和数据源
        initDatas();

        initIndicator();

        mAdapter = new ArrayAdapter<String>(this, R.layout.grid_item,
                R.id.id_grid_item_text, mListItems);
        mPullRefreshListView.setAdapter(mAdapter);

        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<GridView> refreshView)
                    {
                        Log.e("TAG", "onPullDownToRefresh"); // Do work to
                        String label = DateUtils.formatDateTime(
                                getApplicationContext(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);

                        // Update the LastUpdatedLabel
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);

                        new GetDataTask().execute();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<GridView> refreshView)
                    {
                        Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                        // the list here.
                        new GetDataTask().execute();
                    }
                });
    }

    private void initIndicator()
    {
        ILoadingLayout startLabels = mPullRefreshListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("你可劲拉，拉...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("好嘞，正在刷新...");// 刷新时
        startLabels.setReleaseLabel("你敢放，我就敢刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("你可劲拉，拉2...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("好嘞，正在刷新2...");// 刷新时
        endLabels.setReleaseLabel("你敢放，我就敢刷新2...");// 下来达到一定距离时，显示的提示
    }

    private void initDatas()
    {
        mListItems = new LinkedList<String>();

        for (int i = 0; i < mItemCount; i++)
        {
            mListItems.add(i + "");
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            mListItems.add("" + mItemCount++);
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
        }
    }
}