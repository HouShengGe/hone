package com.miaolian.facead.view;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miaolian.facead.R;
import com.miaolian.facead.database.FaceRecordExporter;
import com.miaolian.facead.database.FaceRecordPager;
import com.miaolian.facead.database.FaceRecordRepository;
import com.miaolian.facead.event.EventDismissExcelExportDialog;
import com.miaolian.facead.model.FaceRecord;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class ExportRecordProcessFragment extends Fragment {


    @BindView(R.id.exportRecordPB)
    ProgressBar exportRecordPB;
    @BindView(R.id.exportRecordTv)
    TextView exportRecordTv;
    Unbinder unbinder;
    AsyncTask exportRecordAsyncTask;
    @BindView(R.id.actionBtn)
    Button actionBtn;
    Handler mainHandler;

    public ExportRecordProcessFragment() {
        mainHandler = new Handler(Looper.getMainLooper());
    }


    public static ExportRecordProcessFragment newInstance() {
        ExportRecordProcessFragment fragment = new ExportRecordProcessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_record_process, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        exportRecordAsyncTask = new AsyncTask() {
            FaceRecordExporter exporter;

            @Override
            protected void onPreExecute() {
                Timber.e("onPreExecute:" + "here");
                exporter = new FaceRecordExporter(getContext());
                exportRecordTv.setText("0%");
                exportRecordPB.setProgress(0);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                FaceRecordPager faceRecordPager = new FaceRecordPager();
                List<FaceRecord> faceRecords = new LinkedList<>();
                long totalRecordNum = 0;
                long processedRecordNum = 0;
                Exception exception = null;
                try {
                    totalRecordNum = FaceRecordRepository.getInstance().getRecordNum();
                    faceRecordPager.prepare();
                    exporter.beginTransaction();
                    mainLoop:
                    while (isCancelled() == false) {
                        try {
                            faceRecordPager.pageDown(faceRecords);
                        } catch (FaceRecordPager.PageDownException e) {
                            exporter.setTransactionSuccessful();
                            break;
                        }
                        for (FaceRecord faceRecord : faceRecords) {
                            if (isCancelled()) break mainLoop;
                            exporter.export(faceRecord);
                            Timber.e("doInBackground:" + processedRecordNum + "/" + totalRecordNum);
                            ++processedRecordNum;
                            publishProgress(100 * processedRecordNum / totalRecordNum);
                        }
                    }
                } catch (Exception e) {
                    exception = e;
                } finally {
                    exporter.endTransaction();
                }

                if (isCancelled()) {
                    Timber.e("doInBackground:" + "isCancelled");
                    return new Exception("导出被取消");
                } else {
                    return exception;
                }
            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                Long progress = (Long) values[0];
                try {
                    exportRecordTv.setText(progress + "%");
                    exportRecordPB.setProgress(progress.intValue());
                } catch (Exception e) {

                }
            }

            @Override
            protected void onCancelled() {
                Timber.e("Export cancelled");
            }

            @Override
            protected void onPostExecute(Object o) {
                Timber.e("onPostExecute:" + "");
                if (o != null && o instanceof Exception) {
                    Timber.e("Export excel failed:" + Log.getStackTraceString((Exception) o));
                    try {
                        exportRecordTv.setText(R.string.EXPORT_RECORD_FAILED);
                        exportRecordPB.setVisibility(View.INVISIBLE);
                        actionBtn.setText(R.string.CLOSE);
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        exportRecordTv.setText(R.string.EXPORT_RECORD_SUCCESS);
                        exportRecordPB.setVisibility(View.INVISIBLE);
                        actionBtn.setText(R.string.OK);
                    } catch (Exception e) {
                    }
                }

            }
        };
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exportRecordAsyncTask.execute();
            }
        }, 200);
    }

    @Override
    public void onPause() {
        Timber.e("onPause:" + "here");
        exportRecordAsyncTask.cancel(true);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.actionBtn)
    public void onViewClicked() {
        EventBus.getDefault().post(new EventDismissExcelExportDialog());
    }
}
