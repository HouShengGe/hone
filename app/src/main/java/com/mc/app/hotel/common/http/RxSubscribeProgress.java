package com.mc.app.hotel.common.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.mc.app.hotel.bean.Download;
import com.mc.app.hotel.common.App;
import com.mc.app.hotel.common.util.NetWorkUtil;
import com.mc.app.hotel.common.util.Util;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/10/27.
 */
public abstract class RxSubscribeProgress<T> extends Subscriber<T> {

    public static final String DIALOG_DOWNLOAD = "dialog_download";

    private Context mContext;
    private ProgressDialog dialog;
    private String mDialogStyle;
    private Subscription mRxSbscription;
    private boolean showDialog = true;

    protected abstract void onOverNext(T t);

    protected abstract void onOverError(String message);

    public RxSubscribeProgress(Context context, String dialogType) {
        this.mContext = context;
        this.mDialogStyle = dialogType;
    }

    public RxSubscribeProgress(Context context) {
        this.mContext = context;
        this.mDialogStyle = "";
    }

    public RxSubscribeProgress(Context context, boolean showDialog) {
        this.mContext = context;
        this.mDialogStyle = "";
        this.showDialog = showDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerRxBus();
        dialog = new ProgressDialog(mContext);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setMessage("加载中...");
        initDialogStyle(mDialogStyle);
        //点击取消的时候取消订阅
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!isUnsubscribed()) {
                    /*取消绑定  感觉可以放在 Activity的生命周期里面*/
                    //unsubscribe();
                }
            }
        });
        if (showDialog)
            dialog.show();
    }

    @Override
    public void onNext(T t) {
        onOverNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (!NetWorkUtil.isNetConnected(App.getAppContext())) { //这里自行替换判断网络的代码
            onOverError("网络不可用");
        } else if (e instanceof ServerException) {
            onOverError(e.getMessage());
        } else {
            onOverError("请求失败，请稍后再试...");
        }
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onCompleted() {
        if (dialog != null)
            dialog.dismiss();
        if (mRxSbscription != null && !mRxSbscription.isUnsubscribed()) {
            mRxSbscription.unsubscribe();
        }
    }

    /*判断是下载的dialog，还是加载的dialog*/
    private void initDialogStyle(String dialogStyle) {
        if (TextUtils.equals(dialogStyle, DIALOG_DOWNLOAD)) {
            dialog.setCancelable(false);
            dialog.setProgressNumberFormat(String.format("%s", ""));
            dialog.setTitle("下载");
            dialog.setMessage("正在下载，请稍后...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }
    }

    private void registerRxBus() {
        mRxSbscription = RxBusManage.getDefault()
                .toObservable(HttpInterceptor.DOWNLOAD_TAG, Download.class)
                .subscribe(new Action1<Download>() {
                               @Override
                               public void call(Download download) {
                                   dialog.setProgress(download.getProgress());
                                   if (download.getProgress() == 100 && download.getCurrentFileSize() == download.getTotalFileSize()) {
                                       dialog.setMessage("下载完成！");
                                   } else {
                                       String str = Util.getDataSize(download.getCurrentFileSize())
                                               + "/"
                                               + Util.getDataSize(download.getTotalFileSize());
                                       dialog.setProgressNumberFormat(String.format("%s", str));
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        });
    }

}
