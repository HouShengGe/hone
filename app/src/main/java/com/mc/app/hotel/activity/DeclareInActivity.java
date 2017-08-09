package com.mc.app.hotel.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.CheckInInfo;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.facealignment.model.FaceRecord;
import com.mc.app.hotel.common.facealignment.util.DateUtils;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.util.Identity;
import com.mc.app.hotel.common.util.SPerfUtil;
import com.mc.app.hotel.common.util.StringUtil;
import com.mc.app.hotel.common.util.WidgetUtils;
import com.mc.app.hotel.common.view.DialogListView;
import com.mc.app.hotel.common.view.MyTimeSelect;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/10.
 */

public class DeclareInActivity extends BaseActivity {

    @BindView(R.id.et_room_no)
    EditText tvRoomNo;

    @BindView(R.id.et_room_price)
    EditText etRoomPrice;

    @BindView(R.id.et_room_name)
    EditText etRoomName;

    @BindView(R.id.et_room_sex)
    EditText etRoomSex;
    @BindView(R.id.et_nation)
    EditText etNation;

    @BindView(R.id.et_id_card)
    EditText etIdCard;

    @BindView(R.id.et_in_fromday)
    EditText etFromDay;
    @BindView(R.id.et_in_today)
    EditText etToDay;
    @BindView(R.id.et_id_addr)
    EditText etAddress;

    @BindView(R.id.et_birthday)
    EditText etBirthday;

    @BindView(R.id.et_phone_no)
    EditText etPhoneNo;

    @BindView(R.id.et_arrive_day)
    TextView etArriveDay;

    @BindView(R.id.et_leave_day)
    TextView etLeaveDay;

    @BindView(R.id.imgv_id_pic)
    ImageView imgvIDPic;

    @BindView(R.id.imgv_face_pic)
    ImageView imgvFacePic;

    @BindView(R.id.tv_end)
    TextView tvEnd;

    @BindView(R.id.btn_commit)
    Button btnCommit;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    FaceRecord record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_in);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    String roomNo = "";

    private void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            record = (FaceRecord) getIntent().getSerializableExtra(Constants.READ_ID_CARD);
            roomNo = (getIntent().getStringExtra(Constants.ROOM_NO));
        }
    }

    private void initView() {
        setTitle("申报入住");
        buckButton(true);
        etRoomSex.setFocusable(false);
        etNation.setFocusable(false);
        tvRoomNo.setText(StringUtil.getString(roomNo));
        etRoomName.setText(StringUtil.getString(record.getName()));
        etRoomSex.setText(StringUtil.getString(record.getSex()));
        etIdCard.setText(StringUtil.getString(record.getIdNumber()));
        etFromDay.setText(StringUtil.getString(record.getTermBegin()));
        etToDay.setText(StringUtil.getString(record.getTermEnd()));
        etAddress.setText(StringUtil.getString(record.getAddress()));
        etBirthday.setText(StringUtil.getString(record.getBirthday()));
        etNation.setText(StringUtil.getString(record.getNation()));
        imgvIDPic.setImageBitmap(BitmapFactory.decodeByteArray(record.getIdPhoto(), 0, record.getIdPhoto().length));
        imgvFacePic.setImageBitmap(BitmapFactory.decodeByteArray(record.getCamPhoto(), 0, record.getCamPhoto().length));
        etArriveDay.setText(DateUtils.getCurrentFormateTime());
        etLeaveDay.setText(DateUtils.getNextDay());
        etIdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etIdCard.removeTextChangedListener(this);//解除文字改变事件
                etIdCard.setText(s.toString().toUpperCase());//转换
                etIdCard.setSelection(start + 1 - before);//重新设置光标位置
                etIdCard.addTextChangedListener(this);//重新绑
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 18) {
                    etBirthday.setText(Identity.getIDDate(s.toString()));
                }
            }
        });

        tvEnd.setText(record.getSimilarity() > PrefUtil.getMinConfidence() ? "通过" : "未通过");
        tvEnd.setTextColor(getResources().getColor(record.getSimilarity() > PrefUtil.getMinConfidence() ? R.color.pass : R.color.red));
        commit();
        setArriveDay();
        setLeaveDay();
        selectNation();
        selectSex();
        cancel();
    }

    private static final String TAG = "DeclareInActivity";

    private CheckInInfo getInfo() {
        CheckInInfo info = new CheckInInfo();
        String roomNum = tvRoomNo.getText().toString().trim();
        if (roomNum == null || roomNum.equals("")) {
            tvRoomNo.requestFocus();
            tvRoomNo.setError(Html.fromHtml("<font color=#000000>请填写房间号</font>"));
            return null;
        }
        String name = etRoomName.getText().toString().trim();
        if (name == null || name.equals("")) {
            etRoomName.requestFocus();
            etRoomName.setError(Html.fromHtml("<font color=#000000>请填写姓名</font>"));
            return null;
        }
        String sex = etRoomSex.getText().toString().trim();
        if (sex == null || sex.equals("")) {
            etRoomSex.setFocusable(true);
            etRoomSex.setFocusableInTouchMode(true);
            WidgetUtils.hideKeyBoard(this, etRoomSex);
            etRoomSex.requestFocus();
            etRoomSex.setError(Html.fromHtml("<font color=#000000>请填写性别</font>"));
            return null;
        }
        String nations = etNation.getText().toString().trim();
        if (nations == null || nations.equals("")) {
            etNation.setFocusable(true);
            etNation.setFocusableInTouchMode(true);
            WidgetUtils.hideKeyBoard(this, etNation);
            etNation.requestFocus();
            etNation.setError(Html.fromHtml("<font color=#000000>请填写民族</font>"));
            return null;
        }
        String roomPay = etRoomPrice.getText().toString().trim();
        String phoneNum = etPhoneNo.getText().toString().trim();
        String arriveDay = etArriveDay.getText().toString().trim();
        if (arriveDay == null || arriveDay.equals("")) {
            etArriveDay.requestFocus();
            etArriveDay.setError(Html.fromHtml("<font color=#000000>请填写到店日期</font>"));
            return null;
        }
        String leaveDay = etLeaveDay.getText().toString().trim();
        if (leaveDay == null || leaveDay.equals("")) {
            etLeaveDay.requestFocus();
            etLeaveDay.setError(Html.fromHtml("<font color=#000000>请填写离店日期</font>"));
            return null;
        }
        String idCard = etIdCard.getText().toString().trim();
        if (!Identity.checkIDCard(idCard)) {
            etIdCard.requestFocus();
            etIdCard.setError(Html.fromHtml("<font color=#000000>请填写正确身份证号码</font>"));
            return null;
        }
        String birthday = etBirthday.getText().toString().trim();
        if (birthday == null || birthday.equals("")) {
            etBirthday.requestFocus();
            etBirthday.setError(Html.fromHtml("<font color=#000000>请填写生日</font>"));
            return null;
        } else if (!DateUtils.dateFormatRight(birthday)) {
            etBirthday.requestFocus();
            etBirthday.setError(Html.fromHtml("<font color=#000000>请填写正确生日</font>"));
            return null;
        }
        String address = etAddress.getText().toString().trim();
        if (address == null || address.equals("")) {
            etAddress.requestFocus();
            etAddress.setError(Html.fromHtml("<font color=#000000>请填写地址</font>"));
            return null;
        }
        String exprFDate = etFromDay.getText().toString().trim();
        if (exprFDate == null || exprFDate.equals("")) {
            etFromDay.requestFocus();
            etFromDay.setError(Html.fromHtml("<font color=#000000>请填写身份证有效日期</font>"));
            return null;
        } else if (!DateUtils.dateFormatRight(exprFDate)) {
            etFromDay.requestFocus();
            etFromDay.setError(Html.fromHtml("<font color=#000000>请填写正确身份证有效日期</font>"));
            return null;
        }
        String exprTDate = etToDay.getText().toString().trim();
        if (exprTDate == null || exprTDate.equals("")) {
            etToDay.requestFocus();
            etToDay.setError(Html.fromHtml("<font color=#000000>请填写身份证有效日期</font>"));
            return null;
        } else if (!DateUtils.dateFormatRight(exprTDate) && !exprTDate.equals("永久")) {
            etToDay.requestFocus();
            etToDay.setError(Html.fromHtml("<font color=#000000>请填写正确身份证有效日期</font>"));
            return null;
        }
        String exprDate = exprFDate + "-" + exprTDate;


        info.setIdCard(idCard);
        info.setBirthDate(birthday);
        info.setCustomer(name);
        info.setExprDate(exprDate);
        info.setAddress(address);
        info.setFaceDegree(record.getSimilarity() + "");
        info.setFaceResult(record.getSimilarity() < PrefUtil.getMinConfidence() ? "未通过" : "通过");
        info.setNation(nations);
        info.setSex(sex);
        info.setArriveDate(arriveDay);
        info.setLeaveDate(leaveDay);
        info.setMobile(phoneNum);
        info.setRoomNo(roomNum);
        info.setRoomPrice(roomPay);
        info.setIdCardPhoto(Base64.encodeToString(record.getIdPhoto(), Base64.DEFAULT));
        info.setScanPhoto(Base64.encodeToString(record.getCamPhoto(), Base64.DEFAULT));
        return info;
    }


    DialogListView dialog;

    private void selectNation() {
        RxView.clicks(etNation)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dialog = new DialogListView(DeclareInActivity.this, SPerfUtil.readNation(), new OnItemCilckEvent());
                        dialog.setCanceledOnTouchOutside(true);
                    }
                });
    }

    class OnItemCilckEvent implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            etNation.setText(SPerfUtil.readNation().get(position));
            etNation.setError(null);
            etNation.setFocusable(false);
            if (dialog != null)
                dialog.dismiss();
        }
    }

    private void selectSex() {
        RxView.clicks(etRoomSex)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dialog = new DialogListView(DeclareInActivity.this, Constants.getSexLset(), new OnItemSexCilckEvent());
                        dialog.setCanceledOnTouchOutside(true);
                    }
                });
    }

    class OnItemSexCilckEvent implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            etRoomSex.setText(Constants.getSexLset().get(position));
            etRoomSex.setError(null);
            etRoomSex.setFocusable(false);
            if (dialog != null)
                dialog.dismiss();
        }
    }

    private void commit() {

        RxView.clicks(btnCommit)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        CheckInInfo info = getInfo();
                        if (info == null)
                            return;
                        Map<String, String> map = Params.getCommitParams(info);
                        Api.getInstance().mApiService.commitCheckInMsg(map)
                                .compose(RxSubscribeThread.<String>ioAndMain()).
                                subscribe(new RxSubscribeProgress<String>(DeclareInActivity.this) {
                                    @Override
                                    protected void onOverNext(String t) {
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.CUSTOMER_STATUS, 1);
                                        toNextActivity(SearchCustomerActivity.class, b);
                                        finish();
                                    }

                                    @Override
                                    protected void onOverError(String message) {
                                        showToast(message);
                                    }
                                });
                    }
                });
    }

    private void cancel() {
        RxView.clicks(btnCancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
    }

    private void setArriveDay() {

        RxView.clicks(etArriveDay)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        MyTimeSelect timeSelector = new MyTimeSelect(DeclareInActivity.this, new MyTimeSelect.ResultHandler() {
                            @Override
                            public void handle(String time) {
                                etArriveDay.setText(time);
                            }
                        }, DateUtils.getCurrentFormateTime(), "2030-12-30 23:59");
                        timeSelector.show();
                    }
                });
    }

    private void setLeaveDay() {

        RxView.clicks(etLeaveDay)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        MyTimeSelect timeSelector = new MyTimeSelect(DeclareInActivity.this, new MyTimeSelect.ResultHandler() {
                            @Override
                            public void handle(String time) {
                                etLeaveDay.setText(time);
                            }
                        }, DateUtils.getNextDay(), "2030-12-30 23:59");
                        timeSelector.show();
                    }
                });
    }


}
