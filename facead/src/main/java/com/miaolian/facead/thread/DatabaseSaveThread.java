package com.miaolian.facead.thread;

import android.util.Log;

import com.miaolian.facead.collection.DataPipe;
import com.miaolian.facead.database.FaceRecordRepository;
import com.miaolian.facead.model.FaceRecord;

import timber.log.Timber;

/**
 * Created by gaofeng on 2017-04-24.
 */

public class DatabaseSaveThread extends Thread {
    private static final String TAG = DatabaseSaveThread.class.getSimpleName();
    private static final int DATA_PIPE_SIZE = 20;
    DataPipe dataPipe;
    volatile boolean stopWork;

    public DatabaseSaveThread() {
        dataPipe = new DataPipe(DATA_PIPE_SIZE);
        stopWork = false;
    }

    @Override
    public void run() {
        while (stopWork == false) {
            Object obj = dataPipe.get();
            if (obj == null) return;
            if (obj instanceof DataPipe.DummyData) continue;
            FaceRecord faceRecord = (FaceRecord) obj;
            try {
                FaceRecordRepository.getInstance().insertRecord(faceRecord);
            } catch (Exception e) {
                Timber.e("run:" + Log.getStackTraceString(e));
                return;
            }
        }
    }

    public void submit(FaceRecord faceRecord) {
        dataPipe.submit(faceRecord);
    }

    public void destroyThread() {
        stopWork = true;
        dataPipe.clear();
        dataPipe.submit(new DataPipe.DummyData());
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
