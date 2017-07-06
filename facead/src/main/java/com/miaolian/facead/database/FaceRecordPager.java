package com.miaolian.facead.database;

import android.util.Pair;

import com.miaolian.facead.model.FaceRecord;

import java.util.List;

/**
 * Created by gaofeng on 2017-04-25.
 */

public class FaceRecordPager {
    public static final int MAX_ROW_NUM_PER_PAGE = 100;
    private int totalPageNum;
    private int pageIndex;
    private long minId;
    private long maxId;
    private FaceRecordRepository repository;
    volatile private boolean prepared = false;

    public FaceRecordPager() {
        totalPageNum = 0;
        pageIndex = 0;
        minId = 0;
        maxId = 0;
        repository = null;
        prepared = false;
    }

    public void prepare() {
        repository = FaceRecordRepository.getInstance();
        totalPageNum = (int) Math.ceil((double) repository.getRecordNum() / MAX_ROW_NUM_PER_PAGE);
        Pair<Long, Long> idRange = repository.getRecordIdRange();
        minId = idRange.first;
        maxId = idRange.second;
        pageIndex = 0;
        prepared = true;
    }

    public int getTotalPageNum() throws NotPreparedException {
        if (prepared == false) throw new NotPreparedException();
        return totalPageNum;
    }

    public int pageUp(List<FaceRecord> faceRecords) throws NotPreparedException, PageUpException, Exception {
        if (prepared == false) throw new NotPreparedException();
        if (pageIndex <= 1) {
            throw new PageUpException();
        }
        pageIndex--;
        List<FaceRecord> records = repository.getRecordByIdRangeWithoutPhoto(new Pair<Long, Long>((pageIndex - 1) * MAX_ROW_NUM_PER_PAGE + minId, pageIndex * MAX_ROW_NUM_PER_PAGE - 1 + minId));
        if (records == null) {
            throw new Exception("empty result!");
        }
        faceRecords.clear();
        faceRecords.addAll(records);
        return pageIndex;
    }

    public int jumpTo(int index, List<FaceRecord> faceRecords) throws NotPreparedException, Exception {
        if (prepared == false) throw new NotPreparedException();
        pageIndex = Math.min(Math.max(index, 1), totalPageNum);
        List<FaceRecord> records = repository.getRecordByIdRangeWithoutPhoto(new Pair<Long, Long>((pageIndex - 1) * MAX_ROW_NUM_PER_PAGE + minId, pageIndex * MAX_ROW_NUM_PER_PAGE - 1 + minId));
        if (records == null) {
            throw new Exception("empty result!");
        }
        faceRecords.clear();
        faceRecords.addAll(records);
        return pageIndex;
    }


    public int pageDown(List<FaceRecord> faceRecords) throws NotPreparedException, PageDownException, Exception {
        if (prepared == false) throw new NotPreparedException();
        if (pageIndex >= totalPageNum) {
            throw new PageDownException();
        }
        pageIndex++;
        List<FaceRecord> records = repository.getRecordByIdRangeWithoutPhoto(new Pair<Long, Long>((pageIndex - 1) * MAX_ROW_NUM_PER_PAGE + minId, pageIndex * MAX_ROW_NUM_PER_PAGE - 1 + minId));
        if (records == null) {
            throw new Exception("empty result!");
        }
        faceRecords.clear();
        faceRecords.addAll(records);
        return pageIndex;
    }

    public static class PageUpException extends Exception {
        public PageUpException() {
            super("Page up exception");
        }
    }

    public static class PageDownException extends Exception {
        public PageDownException() {
            super("Page down exception");
        }
    }

    public static class NotPreparedException extends Exception {
        public NotPreparedException() {
            super("Not prepared exception");
        }
    }

}
