package com.mc.app.hotel.common.facealignment.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.SettingActivity;
import com.mc.app.hotel.common.facealignment.event.EventDataSaveRequest;
import com.mc.app.hotel.common.facealignment.event.EventTakePhotoRequest;
import com.mc.app.hotel.common.facealignment.event.EventTakePhotoResponse;
import com.mc.app.hotel.common.facealignment.model.FaceRecord;
import com.mc.app.hotel.common.facealignment.model.Photo;
import com.mc.app.hotel.common.facealignment.util.BitmapUtil;
import com.mc.app.hotel.common.facealignment.util.FaceAlignmentUtil;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

import static com.mc.app.hotel.common.facealignment.view.CameraFaceAlignmentFragment.PhotoType.FACE;
import static com.mc.app.hotel.common.facealignment.view.CameraFaceAlignmentFragment.PhotoType.IDCARD;


public class CameraFaceAlignmentFragment extends Fragment {
    public static final int PREFER_BMP_WIDTH = 300;
    public static final int PREFER_BMP_HEIGHT = 300;

    public enum PhotoType {IDCARD, FACE}

    private static final String STATE_KEY_IDCARD_PHOTO = "STATE_KEY_IDCARD_PHOTO";
    private static final String STATE_KEY_FACE_PHOTO = "STATE_KEY_FACE_PHOTO";
    private static final String STATE_KEY_IDCARD_PHOTO_READY = "STATE_KEY_IDCARD_PHOTO_READY";
    private static final String STATE_KEY_FACE_PHOTO_READY = "STATE_KEY_FACE_PHOTO_READY";

    @BindView(R.id.idCardPhotoIBtn)
    ImageButton idCardPhotoIBtn;
    @BindView(R.id.faceAlignmentResultIv1)
    ImageView faceAlignmentResultIv1;
    @BindView(R.id.facePhotoIBtn)
    ImageButton facePhotoIBtn;
    @BindView(R.id.faceAlignmentResultIv2)
    ImageView faceAlignmentResultIv2;
    @BindView(R.id.hintTv)
    TextView hintTv;
    @BindView(R.id.actionBtn)
    Button actionBtn;
    Unbinder unbinder;
    FaceAlignmentDialogFragment faceAlignmentDialogFragment;
    boolean idCardPhotoReady = false;
    boolean facePhotoReady = false;
    Bitmap idCardPhoto = null;
    Bitmap facePhoto = null;
    Handler mainHandler;

    public CameraFaceAlignmentFragment() {
        // Required empty public constructor
    }

    public static CameraFaceAlignmentFragment newInstance() {
        CameraFaceAlignmentFragment fragment = new CameraFaceAlignmentFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_face_alignment, container, false);
        unbinder = ButterKnife.bind(this, view);
        faceAlignmentDialogFragment = FaceAlignmentDialogFragment.newInstance();
        mainHandler = new Handler(Looper.getMainLooper());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_KEY_FACE_PHOTO_READY, facePhotoReady);
        outState.putBoolean(STATE_KEY_IDCARD_PHOTO_READY, idCardPhotoReady);
        outState.putParcelable(STATE_KEY_IDCARD_PHOTO, idCardPhoto);
        outState.putParcelable(STATE_KEY_FACE_PHOTO, facePhoto);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTakePhotoResponse(EventTakePhotoResponse response) {
        switch (response.getType()) {
            case IDCARD:
                idCardPhoto = response.getBitmap();
                idCardPhotoIBtn.setImageBitmap(idCardPhoto);
                idCardPhotoIBtn.setScaleType(ImageView.ScaleType.FIT_XY);
                idCardPhotoReady = true;
                break;
            case FACE:
                facePhoto = response.getBitmap();
                facePhotoIBtn.setImageBitmap(facePhoto);
                facePhotoIBtn.setScaleType(ImageView.ScaleType.FIT_XY);
                facePhotoReady = true;
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            idCardPhotoReady = savedInstanceState.getBoolean(STATE_KEY_IDCARD_PHOTO_READY);
            facePhotoReady = savedInstanceState.getBoolean(STATE_KEY_FACE_PHOTO_READY);
            idCardPhoto = savedInstanceState.getParcelable(STATE_KEY_IDCARD_PHOTO);
            facePhoto = savedInstanceState.getParcelable(STATE_KEY_FACE_PHOTO);
        } else {
            idCardPhotoReady = false;
            facePhotoReady = false;
            idCardPhoto = null;
            facePhoto = null;
        }
        if (idCardPhoto == null) {
            idCardPhotoIBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            idCardPhotoIBtn.setImageResource(R.drawable.ic_camera);
        } else {
            idCardPhotoIBtn.setScaleType(ImageView.ScaleType.FIT_XY);
            idCardPhotoIBtn.setImageBitmap(idCardPhoto);
        }
        if (facePhoto == null) {
            facePhotoIBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            facePhotoIBtn.setImageResource(R.drawable.ic_camera);
        } else {
            facePhotoIBtn.setScaleType(ImageView.ScaleType.FIT_XY);
            facePhotoIBtn.setImageBitmap(facePhoto);
        }
    }

    int contrasttimes = 0;

    @SuppressLint("StaticFieldLeak")
    @OnClick({R.id.idCardPhotoIBtn, R.id.facePhotoIBtn, R.id.actionBtn, R.id.settingIBtn, R.id.close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.idCardPhotoIBtn:
                EventBus.getDefault().post(new EventTakePhotoRequest(IDCARD));
                break;
            case R.id.facePhotoIBtn:
                EventBus.getDefault().post(new EventTakePhotoRequest(FACE));
                break;
            case R.id.actionBtn:
                if (facePhotoReady && idCardPhotoReady) {
                    hintTv.setText(R.string.FACE_ALIGNMENTING_AND_WAIT);
                    new AsyncTask<Bitmap, Object, Object>() {
                        @Override
                        protected void onPreExecute() {
                            freezeUI();
                            faceAlignmentDialogFragment.showFragment(getChildFragmentManager(), FaceAlignmentDialogFragment.WAIT_FRAGMENT);
                        }

                        @Override
                        protected Object doInBackground(Bitmap... params) {
                            double confidence;
                            try {
                                byte[] idCardPhotoBuffer = BitmapUtil.compress(BitmapUtil.resize(params[0], PREFER_BMP_WIDTH, PREFER_BMP_HEIGHT), Bitmap.CompressFormat.PNG, 80);
                                byte[] facePhotoBuffer = BitmapUtil.compress(BitmapUtil.resize(params[1], PREFER_BMP_WIDTH, PREFER_BMP_HEIGHT), Bitmap.CompressFormat.JPEG, 80);
                                confidence = FaceAlignmentUtil.doFaceAlignment(new Photo(idCardPhotoBuffer, FaceAlignmentUtil.PhotoType.JPG), new Photo(facePhotoBuffer, FaceAlignmentUtil.PhotoType.JPG));
                                if (confidence < PrefUtil.getMinConfidence()) {
                                    contrasttimes++;
                                    if (contrasttimes == 3) {
                                        FaceRecord faceRecord = new FaceRecord();
                                        faceRecord.setRecordTime(System.currentTimeMillis());
                                        faceRecord.setSimilarity(confidence);
                                        faceRecord.setIdPhoto(idCardPhotoBuffer);
                                        faceRecord.setCamPhoto(facePhotoBuffer);
                                        return faceRecord;
                                    }
                                    return new Exception(getString(R.string.CONFIDENCE_TOO_LOW) + confidence);
                                } else {
                                    FaceRecord faceRecord = new FaceRecord();
                                    faceRecord.setRecordTime(System.currentTimeMillis());
                                    faceRecord.setSimilarity(confidence);
                                    faceRecord.setIdPhoto(idCardPhotoBuffer);
                                    faceRecord.setCamPhoto(facePhotoBuffer);
                                    return faceRecord;
                                }
                            } catch (Exception e) {
                                Timber.e("doInBackground:" + Log.getStackTraceString(e));
                                return e;
                            }
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            if (o instanceof Exception) {
                                hintTv.setText(((Exception) o).getMessage());
                                faceAlignmentDialogFragment.showFragment(getChildFragmentManager(), FaceAlignmentDialogFragment.FAILED_FRAGMENT);
                            } else {

                                String res = ((FaceRecord) o).getSimilarity() < PrefUtil.getMinConfidence() ? "对比失败" : "对比成功";
                                hintTv.setText(res + "," + getString(R.string.SIMILARITY) + ":" + ((FaceRecord) o).getSimilarity());
                                EventBus.getDefault().post(new EventDataSaveRequest((FaceRecord) o, 2));
                                faceAlignmentDialogFragment.showFragment(getChildFragmentManager(), FaceAlignmentDialogFragment.SUCCESS_FRAGMENT);
                            }
                            mainHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    faceAlignmentDialogFragment.dismiss();
                                    unFreezeUI();
                                }
                            }, 0);
                        }
                    }.execute(idCardPhoto, facePhoto);
                } else {
                    hintTv.setText(R.string.PLEASE_TAKE_PHOTO);
                }
                break;
            case R.id.settingIBtn:
                getContext().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.close:
                getActivity().finish();
                break;
        }
    }

    private void freezeUI() {
        actionBtn.setEnabled(false);
        idCardPhotoIBtn.setEnabled(false);
        facePhotoIBtn.setEnabled(false);
    }

    private void unFreezeUI() {
        actionBtn.setEnabled(true);
        idCardPhotoIBtn.setEnabled(true);
        facePhotoIBtn.setEnabled(true);
    }

}
