package com.mc.app.hotel.common.http;


import com.mc.app.hotel.bean.HttpResBaseBean;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2016/10/31.
 */
public class RxSubscribeThread {
    public static <T> Observable.Transformer<HttpResBaseBean<T>, T> ioAndMain() {
        return new Observable.Transformer<HttpResBaseBean<T>, T>() {
            @Override
            public Observable<T> call(Observable<HttpResBaseBean<T>> observable) {
                return observable.flatMap(new Func1<HttpResBaseBean<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(HttpResBaseBean<T> result) {
                        if (result.getFlag() == 0||result.getFlag() == 1) {
                            return createData(result.getData());
                        } else {
                            return Observable.error(new ServerException(result.getFlag(),result.getMsg()));
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
