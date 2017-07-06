package com.miaolian.facead.util;

import com.miaolian.facead.model.FaceAlignmentResponse;
import com.miaolian.facead.model.Photo;
import com.miaolian.facead.retrofit.RetrofitService;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gaofeng on 2017-02-22.
 */

public class FaceAlignmentUtil {
    public enum PhotoType {
        JPG("image/jpg"), PNG("image/png");
        private final String type;

        PhotoType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

    public static final String APP_KEY = "jiangsumiaolian";
    public static final String SELFIE_FILE = "image2";
    public static final String HISTORICAL_SELFIE_FILE = "image1";


    public static Retrofit retrofit;
    public static RetrofitService retrofitService;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://218.94.149.27:20175/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }


    public static double doFaceAlignment(Photo selfieFile, Photo historicalSelfieFile) throws IOException {
        RequestBody requestSelfieFile = RequestBody.create(MediaType.parse(selfieFile.photoType.getType()), selfieFile.photoBytes);
        RequestBody requestBodyHistoricalSelfieFile = RequestBody.create(MediaType.parse(historicalSelfieFile.photoType.getType()), historicalSelfieFile.photoBytes);
        String selfieFileName = selfieFile.photoName;
        String historicalSelfieFileName = historicalSelfieFile.photoName;
        if (selfieFileName == null) {
            selfieFileName = SELFIE_FILE;
        } else if (selfieFileName.equals("")) {
            selfieFileName = SELFIE_FILE;
        }else{
            selfieFileName=selfieFileName.trim();
        }
        if (historicalSelfieFileName == null) {
            historicalSelfieFileName = HISTORICAL_SELFIE_FILE;
        } else if (historicalSelfieFileName.equals("")) {
            historicalSelfieFileName = HISTORICAL_SELFIE_FILE;
        }else{
            historicalSelfieFileName=historicalSelfieFileName.trim();
        }


        Call<FaceAlignmentResponse> call = retrofitService.faceAlignment(
                RequestBody.create(MediaType.parse("text/plain"), APP_KEY),
                MultipartBody.Part.createFormData(SELFIE_FILE, selfieFileName, requestSelfieFile),
                MultipartBody.Part.createFormData(HISTORICAL_SELFIE_FILE, historicalSelfieFileName, requestBodyHistoricalSelfieFile)
        );
        return Double.parseDouble(call.execute().body().getSimilarity());
    }
}
