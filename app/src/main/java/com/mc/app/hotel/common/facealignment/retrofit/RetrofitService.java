package com.mc.app.hotel.common.facealignment.retrofit;


import com.mc.app.hotel.common.facealignment.model.CheckUpdateResponse;
import com.mc.app.hotel.common.facealignment.model.FaceAlignmentResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

/**
 * Created by gaofeng on 2017-02-22.
 */

public interface RetrofitService {
    @Multipart
    @POST("Facedetection")
    Call<FaceAlignmentResponse> faceAlignment(
            @Part("app_key") RequestBody appKey,
            @Part MultipartBody.Part photo1,
            @Part MultipartBody.Part photo2
    );
    @FormUrlEncoded
    @POST("CommonManager/version/latestVersion")
    Call<CheckUpdateResponse> checkUpdate(@Field("terminalType") String terminalType, @Field("appType") int appType);

    @FormUrlEncoded
    @Streaming
    @POST("CommonManager/version/down")
    Call<ResponseBody> downloadNewApp(@Field("terminalType") String terminalType, @Field("appType") int appType);

}
