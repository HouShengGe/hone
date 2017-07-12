package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.ComparedFaceBean;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.facealignment.util.BitmapUtil;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/8.
 */

public class ComparedFaceCardActivity extends BaseActivity {

    int masterId;
    @BindView(R.id.imgv_id_card)
    ImageView imgvIDCard;
    @BindView(R.id.imgv_face)
    ImageView imgvFace;
    @BindView(R.id.tv_face_name)
    TextView tvFace;
    @BindView(R.id.tv_compare_results)
    TextView tvCompareRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compared_face_card);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setTitle("查询结果");
        buckButton(true);
        if (getIntent() != null && getIntent().getExtras() != null) {
            masterId = getIntent().getIntExtra(Constants.MASTER_ID, 0);
            getCustomerList();
        }

    }

    private void getCustomerList() {
        Api.getInstance().mApiService.getGetFace(Params.getFaceParams(masterId))
                .compose(RxSubscribeThread.<ComparedFaceBean>ioAndMain()).
                subscribe(new RxSubscribeProgress<ComparedFaceBean>(ComparedFaceCardActivity.this, false) {
                    @Override
                    protected void onOverNext(ComparedFaceBean t) {
                        imgvIDCard.setImageBitmap(BitmapUtil.getBase64Pic(t.getIdCardPhoto()));
                        imgvFace.setImageBitmap(BitmapUtil.getBase64Pic(t.getScanPhoto()));
                        tvFace.setText("姓名：" + t.getCustomer());
                        tvCompareRes.setText("对比结果：" + t.getFaceResult());
                    }

                    @Override
                    protected void onOverError(String message) {
                        showToast(message);
                    }
                });
    }
}
