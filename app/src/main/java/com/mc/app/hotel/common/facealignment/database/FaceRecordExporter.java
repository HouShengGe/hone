package com.mc.app.hotel.common.facealignment.database;

import android.content.Context;
import android.os.Environment;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.model.FaceRecord;
import com.mc.app.hotel.common.facealignment.util.FileUtil;
import com.mc.app.hotel.common.facealignment.util.TimeUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 * Created by gaofeng on 2017-04-26.
 */

public class FaceRecordExporter {
    public static final String EXPORT_ROOT_DIR = Environment.getExternalStorageDirectory() + "/MiaoLian/Export";
    public static final String EXPORT_ID_PHOTO_DIR = EXPORT_ROOT_DIR + "/身份证照片";
    public static final String EXPORT_CAM_PHOTO_DIR = EXPORT_ROOT_DIR + "/抓拍照片";
    public static final String EXPORT_FILE_PATH = EXPORT_ROOT_DIR + "/人证比对.csv";
    volatile boolean transactionSuccessful = false;
    File exportRootDir;
    File exportIdPhotoDir;
    File exportCamPhotoDir;
    File exportFile;
    FileWriter exportFileWriter;
    Context context;

    public FaceRecordExporter(Context context) {
        exportRootDir = new File(EXPORT_ROOT_DIR);
        exportIdPhotoDir = new File(EXPORT_ID_PHOTO_DIR);
        exportCamPhotoDir = new File(EXPORT_CAM_PHOTO_DIR);
        exportFile = new File(EXPORT_FILE_PATH);
        exportFileWriter = null;
        this.context = context;
    }

    public void beginTransaction() throws Exception {
        tidy();
        exportFileWriter = new FileWriter(exportFile, false);
        exportFileWriter.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
        exportFileWriter.flush();
        populateHeaderRow(exportFileWriter);
    }

    public void export(FaceRecord record) throws Exception {
        exportPhoto(record);
        populateDataRow(exportFileWriter, record);
    }

    private void populateHeaderRow(FileWriter w) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(context.getString(R.string.RECORD_TIME)).append(",\t")
                .append(context.getString(R.string.SIMILARITY)).append(",\t")
                .append(context.getString(R.string.ID_NUMBER)).append(",\t")
                .append(context.getString(R.string.NAME)).append(",\t")
                .append(context.getString(R.string.SEX)).append(",\t")
                .append(context.getString(R.string.BIRTHDAY)).append(",\t")
                .append(context.getString(R.string.NATION)).append(",\t")
                .append(context.getString(R.string.ADDRESS)).append(",\t")
                .append(context.getString(R.string.ISSUE_AUTHORITY)).append(",\t")
                .append(context.getString(R.string.TERM_BEGIN)).append(",\t")
                .append(context.getString(R.string.TERM_END)).append("\n");
        w.write(strBuilder.toString());
        w.flush();
    }

    private void populateDataRow(FileWriter w, FaceRecord record) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TimeUtil.getDateStringFrom(record.getRecordTime())).append(",\t")
                .append(record.getSimilarity()).append(",\t")
                .append(record.getIdNumber()).append(",\t")
                .append(record.getName()).append(",\t")
                .append(record.getSex()).append(",\t")
                .append(record.getBirthday()).append(",\t")
                .append(record.getNation()).append(",\t")
                .append(record.getAddress()).append(",\t")
                .append(record.getIssueAuthority()).append(",\t")
                .append(record.getTermBegin()).append(",\t")
                .append(record.getTermEnd()).append("\r\n");
        w.write(strBuilder.toString());
    }

    private void exportPhoto(FaceRecord record) throws Exception {
        FaceRecord recordWithPhoto = FaceRecordRepository.getInstance().getRecordByIdWithPhoto(record.getRecordId());
        if (recordWithPhoto == null) {
            throw new Exception("查询数据失败");
        }
        FileUtil.saveBytesOnDisk(recordWithPhoto.getIdPhoto(), new File(exportIdPhotoDir, recordWithPhoto.getIdNumber() + ".png"));
        FileUtil.saveBytesOnDisk(recordWithPhoto.getCamPhoto(), new File(exportCamPhotoDir, recordWithPhoto.getIdNumber() + ".jpg"));
    }


    public void export(List<FaceRecord> records) throws Exception {
        for (FaceRecord record : records) {
            export(record);
        }
    }


    public void endTransaction() {
        try {
            exportFileWriter.flush();
        } catch (Exception e) {

        } finally {
            try {
                exportFileWriter.close();
            } catch (Exception e) {

            }
        }

        if (transactionSuccessful == false) {
            try {
                tidy();
            } catch (Exception e) {
            }
        }
    }

    public void setTransactionSuccessful() {
        transactionSuccessful = true;
    }

    public void tidy() throws Exception {
        if (exportRootDir.exists() == false) {
            exportRootDir.mkdirs();
        }
        if (exportIdPhotoDir.exists() == false) {
            exportIdPhotoDir.mkdirs();
        }
        if (exportCamPhotoDir.exists() == false) {
            exportCamPhotoDir.mkdirs();
        }
        FileUtil.clearDirectory(exportRootDir);
        exportFile.createNewFile();
    }
}
