package com.mc.app.hotel.common.http;


import com.mc.app.hotel.bean.BaseBean;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2016/10/31.
 */
public class RxSubscribeThread {
    public static <T> Observable.Transformer<BaseBean<T>, T> ioAndMain() {
        return new Observable.Transformer<BaseBean<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseBean<T>> observable) {
                return observable.flatMap(new Func1<BaseBean<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseBean<T> result) {
                        if (result.success()) {
                            return createData(result.result);
                        } else {
                            return Observable.error(new ServerException(result.code));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

    }
}
