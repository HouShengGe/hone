package com.mc.app.hotel.common.facealignment.collection;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gaofeng on 2017-02-20.
 */

public class DataPipe {
    CircularFifoQueue<Object> queue;
    Lock mutex;
    Condition waitCondition;

    public DataPipe(int capacity) {
        queue = new CircularFifoQueue<>(capacity);
        mutex = new ReentrantLock();
        waitCondition = mutex.newCondition();
    }

    public void submit(Object data) {
        mutex.lock();
        queue.add(data);
        waitCondition.signalAll();
        mutex.unlock();
    }

    public Object get() {
        mutex.lock();
        while (queue.isEmpty()) {
            try {
                waitCondition.await();
            } catch (InterruptedException e) {
                mutex.unlock();
                return null;
            }
        }
        Object data = queue.poll();
        mutex.unlock();
        return data;
    }

    public Object get(Lock m) {
        mutex.lock();
        while (queue.isEmpty()) {
            try {
                m.unlock();
                waitCondition.await();
                m.lock();
            } catch (InterruptedException e) {
                mutex.unlock();
                return null;
            }
        }
        Object data = queue.poll();
        mutex.unlock();
        return data;
    }


    public void clear() {
        mutex.lock();
        queue.clear();
        mutex.unlock();
    }

    public static class DummyData {
        int value;

        public DummyData() {
            value = 0;
        }

        public DummyData(int value) {
            this.value = value;
        }
    }

    public static class DestroySignal {

    }

    public static class PauseSignal {

    }
}
