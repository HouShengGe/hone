package com.miaolian.facead.database;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by gaofeng on 2017-04-21.
 */

public class DatabaseWrapper {
    public static final String DB_NAME_SUFFIX = ".db";
    SQLiteDatabase database;
    File dbFile;

    public DatabaseWrapper(String dbFilePath) throws Exception {
        if (dbFilePath.endsWith(DB_NAME_SUFFIX) == false) {
            throw new Exception("ERROR PATH");
        }
        dbFile = new File(dbFilePath);
        dbFile.getParentFile().mkdirs();
        database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void destroy() {
        try {
            database.close();
        } catch (Exception e) {

        }
    }
}
