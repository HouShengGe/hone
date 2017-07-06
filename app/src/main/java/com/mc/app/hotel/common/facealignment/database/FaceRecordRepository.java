package com.mc.app.hotel.common.facealignment.database;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import com.mc.app.hotel.common.facealignment.model.FaceRecord;

import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by gaofeng on 2017-04-21.
 */

public class FaceRecordRepository {
    private static final String TABLE_NAME = "FaceRecord";
    private static final String DROP_TABLE = "drop table " + TABLE_NAME;
    private static final String CREATE_TABLE =
            "create table if not exists " + TABLE_NAME + "(" +
                    "_id INTEGER  PRIMARY KEY AUTOINCREMENT," +
                    "recordTime INTEGER," +
                    "name TEXT," +
                    "sex TEXT," +
                    "nation TEXT," +
                    "birthday TEXT," +
                    "idNumber TEXT," +
                    "address TEXT," +
                    "termBegin TEXT," +
                    "termEnd TEXT," +
                    "issueAuthority TEXT," +
                    "guid TEXT," +
                    "similarity REAL," +
                    "idPhoto BLOB," +
                    "camPhoto BLOB" +
                    ");";
    private static final String INSERT_RECORD = "insert into " + TABLE_NAME + "(recordTime,name,sex,nation,birthday,idNumber,address,termBegin,termEnd,issueAuthority,guid,similarity,idPhoto,camPhoto) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
    private static final String GET_RECORD_BY_ID_RANGE_WITHOUT_PHOTO = "select _id,recordTime,name,sex,nation,birthday,idNumber,address,termBegin,termEnd,issueAuthority,guid,similarity from " + TABLE_NAME + " where _id>=? and _id<=? order by _id asc;";
    private static final String GET_RECORD_BY_ID_WITH_PHOTO = "select * from " + TABLE_NAME + " where _id = ?";

    private static final String GET_RECORD_NUM = "select count(*) from " + TABLE_NAME;
    private static final String DELETE_RECORD_BY_NUM = "delete from " + TABLE_NAME + " where _id in (select _id from " + TABLE_NAME + " order by _id asc limit ?);";
    private static final String DELETE_RECORD_BY_TIME = "delete from " + TABLE_NAME + " where recordTime<?;";
    private static final String DELETE_ALL_RECORD = "delete from " + TABLE_NAME;
    private static final String VACUUM = "VACUUM";
    private static final String ALIGN_ID_STEP_0 = "update " + TABLE_NAME + " set _id=_id-(select min(_id) from " + TABLE_NAME + ")+1";
    private static final String ALIGN_ID_STEP_1 = "update sqlite_sequence set seq=(select max(_id) from " + TABLE_NAME + ") where name = '" + TABLE_NAME + "';";
    private static final String GET_RECORD_ID_RANGE = "select min(_id),max(_id) from " + TABLE_NAME;


    public static final int MAX_RECORD_NUM = 20000;
    public static final int DELETE_RECORD_NUM = 1000;

    DatabaseWrapper databaseWrapper;

    public FaceRecordRepository() throws Exception {
        databaseWrapper = new DatabaseWrapper(Environment.getExternalStorageDirectory() + "/MiaoLian/FaceDatabase.db");
        databaseWrapper.getDatabase().execSQL(CREATE_TABLE);
    }

    public void destroy() {
        databaseWrapper.destroy();
    }

    public FaceRecord getRecordByIdWithPhoto(long id) {
        FaceRecord faceRecord = null;
        Cursor cursor = null;
        try {
            cursor = databaseWrapper.getDatabase().rawQuery(GET_RECORD_BY_ID_WITH_PHOTO, new String[]{String.valueOf(id)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                faceRecord = new FaceRecord();
                faceRecord.setRecordId(cursor.getLong(0));
                faceRecord.setRecordTime(cursor.getLong(1));
                faceRecord.setName(cursor.getString(2));
                faceRecord.setSex(cursor.getString(3));
                faceRecord.setNation(cursor.getString(4));
                faceRecord.setBirthday(cursor.getString(5));
                faceRecord.setIdNumber(cursor.getString(6));
                faceRecord.setAddress(cursor.getString(7));
                faceRecord.setTermBegin(cursor.getString(8));
                faceRecord.setTermEnd(cursor.getString(9));
                faceRecord.setIssueAuthority(cursor.getString(10));
                faceRecord.setGuid(cursor.getString(11));
                faceRecord.setSimilarity(cursor.getDouble(12));
                faceRecord.setIdPhoto(cursor.getBlob(13));
                faceRecord.setCamPhoto(cursor.getBlob(14));
            }
        } catch (Exception e) {
            faceRecord = null;
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {

            }
        }
        return faceRecord;
    }

    public List<FaceRecord> getRecordByIdRangeWithoutPhoto(Pair<Long, Long> idRange) {
        List<FaceRecord> records = null;
        Cursor cursor = null;
        try {
            cursor = databaseWrapper.getDatabase().rawQuery(GET_RECORD_BY_ID_RANGE_WITHOUT_PHOTO, new String[]{String.valueOf(idRange.first), String.valueOf(idRange.second)});
            if (cursor.getCount() > 0) {
                records = new LinkedList<>();
                while (cursor.moveToNext()) {
                    FaceRecord faceRecord = new FaceRecord();
                    faceRecord.setRecordId(cursor.getLong(0));
                    faceRecord.setRecordTime(cursor.getLong(1));
                    faceRecord.setName(cursor.getString(2));
                    faceRecord.setSex(cursor.getString(3));
                    faceRecord.setNation(cursor.getString(4));
                    faceRecord.setBirthday(cursor.getString(5));
                    faceRecord.setIdNumber(cursor.getString(6));
                    faceRecord.setAddress(cursor.getString(7));
                    faceRecord.setTermBegin(cursor.getString(8));
                    faceRecord.setTermEnd(cursor.getString(9));
                    faceRecord.setIssueAuthority(cursor.getString(10));
                    faceRecord.setGuid(cursor.getString(11));
                    faceRecord.setSimilarity(cursor.getDouble(12));
                    records.add(faceRecord);
                }
            }
        } catch (Exception e) {
            records = null;
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {

            }
        }
        return records;
    }

    public Pair<Long, Long> getRecordIdRange() {
        Cursor cursor = null;
        Pair<Long, Long> pair = null;
        try {
            cursor = databaseWrapper.getDatabase().rawQuery(GET_RECORD_ID_RANGE, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                pair = new Pair<>(cursor.getLong(0), cursor.getLong(1));
            }
        } catch (Exception e) {
            pair = null;
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {

            }
        }
        return pair;
    }

    public void insertRecord(FaceRecord record) {
        databaseWrapper.getDatabase().beginTransaction();
        try {
            if (getRecordNum() > MAX_RECORD_NUM) {
                deleteRecordByNum(DELETE_RECORD_NUM);
            }
            databaseWrapper.getDatabase().execSQL(INSERT_RECORD, new Object[]{record.getRecordTime(), record.getName(), record.getSex(), record.getNation(), record.getBirthday(), record.getIdNumber(), record.getAddress(), record.getTermBegin(), record.getTermEnd(), record.getIssueAuthority(), record.getGuid(), record.getSimilarity(), record.getIdPhoto(), record.getCamPhoto()});
            databaseWrapper.getDatabase().setTransactionSuccessful();
        } catch (Exception e) {

        }
        databaseWrapper.getDatabase().endTransaction();
    }


    public void deleteRecordByNum(int num) {
        databaseWrapper.getDatabase().beginTransaction();
        try {
            databaseWrapper.getDatabase().execSQL(DELETE_RECORD_BY_NUM, new Object[]{num});
            databaseWrapper.getDatabase().setTransactionSuccessful();
        } catch (Exception e) {

        }
        databaseWrapper.getDatabase().endTransaction();
        deletePostExecute();
    }

    public void deleteRecordByTime(long time) {
        databaseWrapper.getDatabase().beginTransaction();
        try {
            databaseWrapper.getDatabase().execSQL(DELETE_RECORD_BY_TIME, new Object[]{time});
            databaseWrapper.getDatabase().setTransactionSuccessful();
        } catch (Exception e) {

        }
        databaseWrapper.getDatabase().endTransaction();
        deletePostExecute();
    }

    public void deleteAllRecord() {
        databaseWrapper.getDatabase().beginTransaction();
        try {
            databaseWrapper.getDatabase().execSQL(DELETE_ALL_RECORD);
            databaseWrapper.getDatabase().setTransactionSuccessful();
        } catch (Exception e) {
            Timber.e("deleteAllRecord error:" + Log.getStackTraceString(e));
        }
        databaseWrapper.getDatabase().endTransaction();
        deletePostExecute();
    }

    public long getRecordNum() {
        long recordNum = 0;
        Cursor cursor = null;
        try {
            cursor = databaseWrapper.getDatabase().rawQuery(GET_RECORD_NUM, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                recordNum = cursor.getLong(0);
            }
        } catch (Exception e) {
            recordNum = 0;
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {
            }
        }
        return recordNum;
    }


    public void deletePostExecute() {
        databaseWrapper.getDatabase().beginTransaction();
        try {
            databaseWrapper.getDatabase().execSQL(ALIGN_ID_STEP_0);
            databaseWrapper.getDatabase().execSQL(ALIGN_ID_STEP_1);
            databaseWrapper.getDatabase().setTransactionSuccessful();
        } catch (Exception e) {
            Timber.e("deletePostExecute error:" + Log.getStackTraceString(e));
        }
        databaseWrapper.getDatabase().endTransaction();
        databaseWrapper.getDatabase().execSQL(VACUUM);
    }


    //static part goes here
    private static FaceRecordRepository faceRecordRepository = null;

    public static FaceRecordRepository getInstance() {
        if (faceRecordRepository == null) {
            try {
                faceRecordRepository = new FaceRecordRepository();
            } catch (Exception e) {
                Timber.e("init FaceRecordRepository error:" + Log.getStackTraceString(e));
                faceRecordRepository = null;
            }
        }
        return faceRecordRepository;
    }
}
