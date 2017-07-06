package com.mc.app.hotel.common.facealignment.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.caihua.cloud.common.entity.PersonInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.event.EventOCRFinished;
import com.mc.app.hotel.common.facealignment.model.OCRResponse;
import com.yunmai.cc.idcard.controler.OcrConstant;
import com.yunmai.cc.idcard.controler.OcrManager;
import com.yunmai.cc.idcard.vo.IdCardInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import timber.log.Timber;

/**
 * Created by gaofeng on 2017-03-06.
 */

public class OCRUtil {
    private static Context context;
    private static Handler ocrHandler;
    private static OcrManager ocrManager;
    private static PersonInfo frontPersonInfo;
    private static PersonInfo backPersonInfo;
    private static String ocrHeadPhotoPath;
    private static String originalOCRPhotoPath;
    private static final int MAX_ERROR_COUNT = 100;
    private static int errorCount;

    public static void init(Context context) {
        OCRUtil.context = context;
        ocrHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case OcrConstant.RECOGN_OK:
                        IdCardInfo idCardInfo = ocrManager.getResult(originalOCRPhotoPath, ocrHeadPhotoPath);
                        Gson gson = new GsonBuilder().setLenient().create();

                        OCRResponse ocrResponse = gson.fromJson(new String(idCardInfo.getCharInfo(), Charset.forName("gbk")).trim(), OCRResponse.class);
                        Timber.e("handleMessage:" + "ocrResponse=" + ocrResponse);
                        if (isFront(ocrResponse)) {
                            if (frontPersonInfo != null) {
                                Timber.e("handleMessage:"+"here");
                                errorCount++;
                            } else {
                                try {
                                    frontPersonInfo = generateFrontPersonInfo(ocrResponse);
                                    if (backPersonInfo == null) {
                                        Timber.e("handleMessage:"+"here");
                                        PlaySoundUtil.play(OCRUtil.context, R.raw.ocr_changeside);
                                    }
                                } catch (Exception e) {
                                    Timber.e("handleMessage:" + Log.getStackTraceString(e));
                                    frontPersonInfo = null;
                                    errorCount++;
                                }
                            }
                        } else if (isBack(ocrResponse)) {
                            if (backPersonInfo != null) {
                                errorCount++;
                            } else {
                                try {
                                    backPersonInfo = generateBackPersonInfo(ocrResponse);
                                    if (frontPersonInfo == null) {
                                        PlaySoundUtil.play(OCRUtil.context, R.raw.ocr_changeside);
                                    }
                                } catch (Exception e) {
                                    Timber.e("handleMessage:" + Log.getStackTraceString(e));
                                    backPersonInfo = null;
                                    errorCount++;
                                }
                            }
                        } else {
                            errorCount++;
                        }
                        break;
                    default:
                        if (frontPersonInfo != null || backPersonInfo != null) {
                            Timber.e("OCR增加错误计数:" + msg.what);
                            errorCount++;
                        } else {
                            Timber.e("未检测到身份证:" + msg.what);
                        }
                        break;
                }

                if (errorCount >= MAX_ERROR_COUNT) {
                    errorCount = 0;
                    frontPersonInfo = null;
                    backPersonInfo = null;
                    Timber.e("OCR识别失败:错误次数太多");
                    PlaySoundUtil.play(OCRUtil.context, R.raw.ocr_recognize_failed);
                    EventBus.getDefault().post(new EventOCRFinished(false, "OCR身份证识别失败", null));
                    return;
                }

                if (frontPersonInfo != null && backPersonInfo != null) {
                    PersonInfo personInfo = merge(frontPersonInfo, backPersonInfo);
                    frontPersonInfo = null;
                    backPersonInfo = null;
                    errorCount = 0;
                    EventBus.getDefault().post(new EventOCRFinished(true, null, personInfo));
                    return;
                }
            }
        };
        ocrManager = new OcrManager(ocrHandler, context);
        ocrHeadPhotoPath = context.getFilesDir() + "/headPhoto.jpg";
        originalOCRPhotoPath = context.getFilesDir() + "/originalPhoto.jpg";
        frontPersonInfo = null;
        backPersonInfo = null;
        errorCount = 0;
    }

    public static void recognize(byte[] imgBytes, int width, int height) {
        ocrManager.recognBC(imgBytes, width, height, new Rect(0, 0, width, height));
    }

    private static boolean isFront(OCRResponse ocrResponse) {
        Timber.e("isFront:"+(ocrResponse.getName().getValue().equals("") == false && ocrResponse.getSex().getValue().equals("") == false && ocrResponse.getFolk().getValue().equals("") == false && ocrResponse.getBirt().getValue().equals("") == false && ocrResponse.getAddr().getValue().equals("") == false && ocrResponse.getNum().getValue().equals("") == false));
        return ocrResponse.getName().getValue().equals("") == false && ocrResponse.getSex().getValue().equals("") == false && ocrResponse.getFolk().getValue().equals("") == false && ocrResponse.getBirt().getValue().equals("") == false && ocrResponse.getAddr().getValue().equals("") == false && ocrResponse.getNum().getValue().equals("") == false;
    }

    private static PersonInfo generateFrontPersonInfo(OCRResponse ocrResponse) throws Exception {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(ocrResponse.getName().getValue());
        personInfo.setSex(ocrResponse.getSex().getValue());
        personInfo.setNation(ocrResponse.getFolk().getValue());
        personInfo.setBirthday(ocrResponse.getBirt().getValue());
        personInfo.setIdNumber(ocrResponse.getNum().getValue());
        personInfo.setAddress(ocrResponse.getAddr().getValue());
        personInfo.setGuid(ocrResponse.getNum().getValue());
        Bitmap jpegBitmap = BitmapFactory.decodeFile(ocrHeadPhotoPath);
        ByteArrayOutputStream pngBaos = new ByteArrayOutputStream();
        jpegBitmap.compress(Bitmap.CompressFormat.PNG, 100, pngBaos);
        personInfo.setPhoto(pngBaos.toByteArray());
        return personInfo;
    }


    private static boolean isBack(OCRResponse ocrResponse) {
        return ocrResponse.getIssue().getValue().equals("") == false && ocrResponse.getValid().getValue().equals("") == false;
    }

    private static PersonInfo generateBackPersonInfo(OCRResponse ocrResponse) throws Exception {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setIssueAuthority(ocrResponse.getIssue().getValue());
        String[] terms = ocrResponse.getValid().getValue().split("-");
        personInfo.setTermBegin(terms[0]);
        personInfo.setTermEnd(terms[1]);
        return personInfo;
    }

    private static PersonInfo merge(PersonInfo frontPersonInfo, PersonInfo backPersonInfo) {
        PersonInfo personInfo = new PersonInfo(frontPersonInfo);
        personInfo.setIssueAuthority(backPersonInfo.getIssueAuthority());
        personInfo.setTermBegin(backPersonInfo.getTermBegin());
        personInfo.setTermEnd(backPersonInfo.getTermEnd());
        return personInfo;
    }
}
