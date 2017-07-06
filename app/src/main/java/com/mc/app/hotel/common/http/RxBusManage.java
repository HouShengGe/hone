package com.mc.app.hotel.common.http;

import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by shouqingcao on 16/12/23.
 */

public class RxBusManage {

    private static RxBusManage mInstance;
    private ConcurrentHashMap<Object, Subject<Object, Object>> mSubjectMapper;
    private static Subject<Object, Object> mBus;

    /*PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者*/
    public RxBusManage() {
        mBus = new SerializedSubject<>(PublishSubject.create());
        mSubjectMapper = new ConcurrentHashMap<>();
    }

    /*单例RxBus*/
    public static RxBusManage getDefault() {
        if (mInstance == null) {
            synchronized (RxBusManage.class) {
                if (mInstance == null) {
                    mInstance = new RxBusManage();
                }
            }
        }
        return mInstance;
    }

    /*发送一个新的事件*/
    public void post (Object tag, Object object) {
        Subject<Object, Object> subjectPost = mSubjectMapper.get(tag);
        if (subjectPost != null) {
            subjectPost.onNext(object);
        }
    }

    /*根据传递的 eventType 类型返回特定类型(eventType)的 被观察者*/
    public <T> Observable<T> toObservable (Object tag, Class<T> eventType) {
        Subject<Object, Object> subjectRegister = mSubjectMapper.get(tag);
        if (subjectRegister == null) {
            subjectRegister = new SerializedSubject<>(PublishSubject.create());
            mSubjectMapper.put(tag, subjectRegister);
        }
        return subjectRegister.ofType(eventType);
    }

}
