package com.mc.app.hotel.common.facealignment.view;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.service.ReadCardService;
import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.RecordManageActivity;
import com.mc.app.hotel.common.facealignment.SettingActivity;
import com.mc.app.hotel.common.facealignment.event.EventDataSaveRequest;
import com.mc.app.hotel.common.facealignment.event.EventReceiveNFCTag;
import com.mc.app.hotel.common.facealignment.model.FaceRecord;
import com.mc.app.hotel.common.facealignment.model.Photo;
import com.mc.app.hotel.common.facealignment.thread.DataDispatchThread;
import com.mc.app.hotel.common.facealignment.thread.DrawPreviewFrameThread;
import com.mc.app.hotel.common.facealignment.thread.FaceDetectThread;
import com.mc.app.hotel.common.facealignment.util.BitmapUtil;
import com.mc.app.hotel.common.facealignment.util.CameraUtil;
import com.mc.app.hotel.common.facealignment.util.FaceAlignmentUtil;
import com.mc.app.hotel.common.facealignment.util.PlaySoundUtil;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;
import com.mc.app.hotel.common.facealignment.util.SDCardUtils;
import com.mc.app.hotel.common.facealignment.util.ServiceUtil;
import com.mc.app.hotel.common.facealignment.util.UMengUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;


public class IDCardFaceAlignmentFragment extends Fragment implements Camera.PreviewCallback, FaceDetectThread.FaceDetectThreadListener, SurfaceHolder.Callback {
    private static final String TAG = IDCardFaceAlignmentFragment.class.getSimpleName();
    private static final int PREFER_PREVIEW_WIDTH = 300;
    private static final int PREFER_PREVIEW_HEIGHT = 300;
    @BindView(R.id.cameraTextureView)
    TextureView cameraTextureView;
    @BindView(R.id.invisibleSurfaceView)
    SurfaceView invisibleTextureView;
    @BindView(R.id.photoIv)
    ImageView photoIv;
    @BindView(R.id.nameTv)
    TextView nameTv;
    @BindView(R.id.sexTv)
    TextView sexTv;
    @BindView(R.id.idNumTv)
    TextView idNumTv;
    @BindView(R.id.termTv)
    TextView termTv;
    @BindView(R.id.birthdayTv)
    TextView birthdayTv;
    @BindView(R.id.captureTextureView)
    TextureView captureTextureView;
    @BindView(R.id.faceAlignmentResultIv)
    ImageView faceAlignmentResultIv;
    Unbinder unbinder;
    Camera camera;
    DataDispatchThread dataDispatchThread = null;
    DrawPreviewFrameThread drawPreviewFrameThread = null;
    FaceDetectThread faceDetectThread = null;
    BroadcastReceiver idCardBroadcastReceiver;
    Timer startReadIdCardTimer;
    PublishSubject<Pair<Photo, Photo>> faceAlignmentSubject = PublishSubject.create();
    PersonInfo personInfo = new PersonInfo();
    FaceAlignmentDialogFragment faceAlignmentDialogFragment;
    ReadIDCardDialogFragment readIDCardDialogFragment;
    volatile boolean faceAlignmenting = false;
    volatile boolean faceAlignmentHasSucceed = false;
    Handler mainHandler;
    VolumePopupWindow volumePopupWindow;

    public IDCardFaceAlignmentFragment() {
        faceAlignmentSubject
                .onBackpressureLatest()
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Pair<Photo, Photo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        faceAlignmentFailed(getString(R.string.EXCEPTION_OCCURRED) + e.getMessage());
                    }

                    @Override
                    public void onNext(Pair<Photo, Photo> photoPair) {
                        double confidence;
                        try {
                            confidence = FaceAlignmentUtil.doFaceAlignment(photoPair.first, photoPair.second);
                            if (confidence < PrefUtil.getMinConfidence()) {
                                faceAlignmentFailed(getString(R.string.CONFIDENCE_TOO_LOW) + confidence);
                            } else {
                                FaceRecord faceRecord = new FaceRecord();
                                faceRecord.setRecordTime(System.currentTimeMillis());
                                faceRecord.setName(personInfo.getName());
                                faceRecord.setSex(personInfo.getSex());
                                faceRecord.setNation(personInfo.getNation());
                                faceRecord.setBirthday(personInfo.getBirthday());
                                faceRecord.setIdNumber(personInfo.getIdNumber());
                                faceRecord.setAddress(personInfo.getAddress());
                                faceRecord.setTermBegin(personInfo.getTermBegin());
                                faceRecord.setTermEnd(personInfo.getTermEnd());
                                faceRecord.setIssueAuthority(personInfo.getIssueAuthority());
                                faceRecord.setGuid(personInfo.getGuid());
                                faceRecord.setSimilarity(confidence);
                                faceRecord.setIdPhoto(photoPair.second.photoBytes);
                                faceRecord.setCamPhoto(photoPair.first.photoBytes);
                                EventBus.getDefault().post(new EventDataSaveRequest(faceRecord));
                                faceAlignmentSuccess(confidence);
                            }
                        } catch (Exception e) {
                            faceAlignmentFailed(getString(R.string.EXCEPTION_OCCURRED) + e.getMessage());
                        }
                    }
                });
    }


    public static IDCardFaceAlignmentFragment newInstance() {
        IDCardFaceAlignmentFragment fragment = new IDCardFaceAlignmentFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.e("onCreateView:" + "here");
        View view = inflater.inflate(R.layout.fragment_idcard_face_alignment, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        cameraTextureView.setOpaque(false);
        captureTextureView.setOpaque(false);

        mainHandler = new Handler(Looper.getMainLooper());
        volumePopupWindow = new VolumePopupWindow(getContext());

        faceAlignmentDialogFragment = FaceAlignmentDialogFragment.newInstance();
        readIDCardDialogFragment = ReadIDCardDialogFragment.newInstance();

        invisibleTextureView.getHolder().addCallback(this);
        invisibleTextureView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        idCardBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) return;
                if (action.equals(ServiceUtil.FINISH_READ_IDCARD)) {
                    if (intent.getBooleanExtra(ServiceUtil.READ_IDCARD_RESULT, false)) {
                        readIDCardSuccess(intent);
                    } else {
                        readIDCardFailed(intent);
                    }
                }
            }
        };

        return view;
    }


    private void faceAlignmentSuccess(double confidence) {
        UMengUtil.faceAlignmentSuccess(getContext());
        faceAlignmentHasSucceed = true;
        setFaceAlignmentResultIvVisibility(View.VISIBLE, true);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                faceAlignmentDialogFragment.showFragment(getChildFragmentManager(), FaceAlignmentDialogFragment.SUCCESS_FRAGMENT);
            }
        });
        Log.e(TAG, "faceAlignmentSuccess: ");
        PlaySoundUtil.play(getContext(), R.raw.facealignment_success);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                resetToCameraPreview();
                faceAlignmenting = false;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        faceAlignmentDialogFragment.dismiss();
                    }
                });
            }
        }, 3000);
    }

    private void faceAlignmentFailed(String errorMsg) {
        Timber.e("faceAlignmentFailed:here" + errorMsg);
        UMengUtil.faceAlignemntFailed(getContext(), errorMsg);
        faceAlignmentHasSucceed = false;
        setFaceAlignmentResultIvVisibility(View.VISIBLE, false);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                faceAlignmentDialogFragment.showFragment(getChildFragmentManager(), FaceAlignmentDialogFragment.FAILED_FRAGMENT);
            }
        });
        Log.e(TAG, "faceAlignmentFailed: " + errorMsg);
        PlaySoundUtil.play(getContext(), R.raw.facealignment_failed);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                resetToCameraPreview();
                faceAlignmenting = false;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        faceAlignmentDialogFragment.dismiss();
                    }
                });
            }
        }, 3000);
    }

    private void showPersonInfo() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                nameTv.setText(personInfo.getName());
                sexTv.setText(personInfo.getSex());
                birthdayTv.setText(personInfo.getBirthday());
                photoIv.setImageBitmap(BitmapFactory.decodeByteArray(personInfo.getPhoto(), 0, personInfo.getPhoto().length));
                termTv.setText(personInfo.getTermBegin() + "~" + personInfo.getTermEnd());
                idNumTv.setText(personInfo.getIdNumber());
            }
        });
    }


    private void clearPersonInfo() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    nameTv.setText("");
                    sexTv.setText("");
                    birthdayTv.setText("");
                    photoIv.setImageResource(R.drawable.ic_face);
                    termTv.setText("");
                    idNumTv.setText("");
                } catch (Exception e) {

                }
            }
        });
    }

    private void readIDCardSuccess(Intent intent) {
        if (faceAlignmenting) return;
        if (PrefUtil.getLinkType().equals(ServiceUtil.NFC)) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    readIDCardDialogFragment.showFragment(getChildFragmentManager(), ReadIDCardDialogFragment.SUCCESS_FRAGMENT);
                }
            });
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    readIDCardDialogFragment.dismiss();
                }
            }, 2000);
        }
        PersonInfo tmpPersonInfo = ServiceUtil.generatePersonInfoFromIntent(intent);
        if (personInfo != null) {
            if (personInfo.getIdNumber().equals(tmpPersonInfo.getIdNumber()) && faceAlignmentHasSucceed)
                return;
        }

        faceAlignmenting = true;
        Log.e(TAG, "读身份证成功 姓名:" + intent.getStringExtra(ServiceUtil.NAME));
        personInfo = tmpPersonInfo;
        PlaySoundUtil.play(getContext(), R.raw.readidcard_success);
        UMengUtil.readIDCardSuccess(getContext());
        showPersonInfo();
        setFaceAlignmentResultIvVisibility(View.INVISIBLE, false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startCaptureFace();
            }
        }, 4000);
    }

    private void clearCaptureTextureView() {
        try {
            Canvas canvas = captureTextureView.lockCanvas();
            canvas.drawColor(Color.BLACK);
            captureTextureView.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
        }
    }

    private void readIDCardFailed(Intent intent) {
        if (faceAlignmenting) return;
        clearPersonInfo();
        clearCaptureTextureView();
        faceAlignmentHasSucceed = false;
        setFaceAlignmentResultIvVisibility(View.INVISIBLE, false);
        if (PrefUtil.getLinkType().equals(ServiceUtil.NFC)) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    readIDCardDialogFragment.showFragment(getChildFragmentManager(), ReadIDCardDialogFragment.FAILED_FRAGMENT);
                }
            });
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        readIDCardDialogFragment.dismiss();
                    } catch (Exception e) {

                    }
                }
            }, 2000);
        }
        Timber.e("读身份证失败 " + intent.getStringExtra(ServiceUtil.ERROR_MESSAGE));
        UMengUtil.readIDCardFailed(getContext(), intent.getStringExtra(ServiceUtil.ERROR_MESSAGE));

    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.e("onStart:");
        clearPersonInfo();
        clearCaptureTextureView();
        setFaceAlignmentResultIvVisibility(View.INVISIBLE, false);

        faceAlignmenting = false;
        faceAlignmentHasSucceed = false;
        getContext().registerReceiver(idCardBroadcastReceiver, new IntentFilter(ServiceUtil.FINISH_READ_IDCARD));
        startReadIdCardTimer = new Timer();
        startReadIdCardTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (PrefUtil.getLinkType().equals(ServiceUtil.OTG)) {
                    Intent intent = ServiceUtil.buildOtgReadIDCardIntent(getContext(), PrefUtil.getUseSpecificServer(), PrefUtil.getSpecificServerAddress(), PrefUtil.getSpecificServerPort());
                    getContext().startService(intent);
                } else if (PrefUtil.getLinkType().equals(ServiceUtil.SERIALPORT)) {
                    Intent intent = ServiceUtil.buildSerialPortReadIDCardIntent(getContext(), PrefUtil.getUseSpecificServer(), PrefUtil.getSpecificServerAddress(), PrefUtil.getSpecificServerPort(), PrefUtil.getSerialPortDevPath(), PrefUtil.getSerialPortBaudRate(), PrefUtil.getSerialPortFlag());
                    getContext().startService(intent);
                }
            }
        }, 500, 1000);
    }

    @Override
    public void onStop() {
        try {
            drawPreviewFrameThread.destroyThread();
        } catch (Exception e) {

        }
        try {
            faceDetectThread.destroyThread();
        } catch (Exception e) {

        }
        try {
            dataDispatchThread.destroyThread();
        } catch (Exception e) {

        }
        try {
            getContext().unregisterReceiver(idCardBroadcastReceiver);
        } catch (Exception e) {
        }
        getContext().stopService(new Intent(getContext(), ReadCardService.class));
        startReadIdCardTimer.cancel();
        super.onStop();
    }


    private void setFaceAlignmentResultIvVisibility(final int visibility, final boolean success) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (success) {
                        faceAlignmentResultIv.setImageResource(R.drawable.ic_success_small);
                    } else {
                        faceAlignmentResultIv.setImageResource(R.drawable.ic_failed_small);
                    }
                    faceAlignmentResultIv.setVisibility(visibility);
                } catch (Exception e) {

                }
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.e("onCreate:");
    }

    @Override
    public void onDestroyView() {
        Timber.e("onDestroyView:" + "here");
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (dataDispatchThread != null) {
            dataDispatchThread.submit(data);
        }
    }

    @Override
    public void onFoundFace(final Bitmap faceBmp) {
        BitmapUtil.savePicInLocal(faceBmp, SDCardUtils.MS_TEMP_PIC,"",1);
        Timber.e("\n=====================find face yes!==================\n");
        new Thread() {
            @Override
            public void run() {
                finishCaptureFace(faceBmp);
            }
        }.start();
    }

    private void startCaptureFace() {
        Timber.e("startCaptureFace: ");
        faceDetectThread.resumeThread();
    }

    private void resetToCameraPreview() {
        Timber.e("resetToCameraPreview: ");
        faceDetectThread.pauseThread();
        drawPreviewFrameThread.resumeThread();
    }

    private void finishCaptureFace(final Bitmap faceBmp) {
        Timber.e("finishCaptureFace: ");
        faceDetectThread.pauseThread();
        if (faceBmp == null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    faceAlignmentFailed(getString(R.string.NO_FACE_CAPTURED));
                }
            });
            return;
        }
        float textureViewWidth = captureTextureView.getMeasuredWidth();
        float textureViewHeight = captureTextureView.getMeasuredHeight();
        float scaleX = textureViewWidth / faceBmp.getWidth();
        float scaleY = textureViewHeight / faceBmp.getHeight();
        float scale = scaleX > scaleY ? scaleX : scaleY;
        float bitmapNewWidth = faceBmp.getWidth() * scale;
        float bitmapNewHeight = faceBmp.getHeight() * scale;
        float dx = (textureViewWidth - bitmapNewWidth) / 2;
        float dy = (textureViewHeight - bitmapNewHeight) / 2;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        matrix.postTranslate(dx, dy);
        try {
            Canvas canvas = captureTextureView.lockCanvas();
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(faceBmp, matrix, null);
            captureTextureView.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            Timber.e("onFoundFace: " + Log.getStackTraceString(e));
        }

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                faceAlignmentDialogFragment.showFragment(getChildFragmentManager(), FaceAlignmentDialogFragment.WAIT_FRAGMENT);
            }
        });

        try {
            Photo historicalSelfieFile = new Photo(personInfo.getPhoto(), FaceAlignmentUtil.PhotoType.PNG);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            faceBmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            Photo selfieFile = new Photo(baos.toByteArray(), FaceAlignmentUtil.PhotoType.JPG);
            Pair<Photo, Photo> photoPair = new Pair<>(selfieFile, historicalSelfieFile);

            faceAlignmentSubject.onNext(photoPair);
        } catch (Exception e) {
            faceAlignmentFailed(getString(R.string.EXCEPTION_OCCURRED) + e.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceiveNFCTag(EventReceiveNFCTag event) {
        if (faceAlignmenting) return;
        try {
            readIDCardDialogFragment.dismiss();
        } catch (Exception e) {
        }
        if (PrefUtil.getLinkType().equals(ServiceUtil.NFC)) {
            Intent idcardIntent = ServiceUtil.buildNfcReadIDCardIntent(getContext(), PrefUtil.getUseSpecificServer(), PrefUtil.getSpecificServerAddress(), PrefUtil.getSpecificServerPort(), event.getTag());
            getContext().startService(idcardIntent);
            readIDCardDialogFragment.showFragment(getChildFragmentManager(), ReadIDCardDialogFragment.WAIT_FRAGMENT);
        }
    }

    @OnClick({R.id.volumeIBtn, R.id.recordManagementBtn, R.id.settingIBtn, R.id.startFaceDetectBtn, R.id.stopFaceDetectBtn, R.id.doFaceAlignmentBtn, R.id.resetBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.startFaceDetectBtn:
                faceAlignmentDialogFragment.showFragment(getChildFragmentManager(), FaceAlignmentDialogFragment.WAIT_FRAGMENT);
                break;
            case R.id.stopFaceDetectBtn:
                faceAlignmentDialogFragment.showFragment(getChildFragmentManager(), FaceAlignmentDialogFragment.SUCCESS_FRAGMENT);
                break;
            case R.id.doFaceAlignmentBtn:
                faceAlignmentDialogFragment.showFragment(getChildFragmentManager(), FaceAlignmentDialogFragment.FAILED_FRAGMENT);
                break;
            case R.id.resetBtn:
                faceAlignmentDialogFragment.dismiss();
                break;
            case R.id.settingIBtn:
                getContext().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.recordManagementBtn:
                getContext().startActivity(new Intent(getContext(), RecordManageActivity.class));
                break;
            case R.id.volumeIBtn:
                volumePopupWindow.show(view);
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = CameraUtil.getCamera(PrefUtil.getCameraType(), PREFER_PREVIEW_WIDTH, PREFER_PREVIEW_HEIGHT, this);
        } catch (Exception e) {
            Timber.e("onSurfaceTextureAvailable:" + Log.getStackTraceString(e));
        }
        drawPreviewFrameThread = new DrawPreviewFrameThread(getContext(), cameraTextureView, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height);
        drawPreviewFrameThread.start();
        faceDetectThread = new FaceDetectThread(cameraTextureView, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height);
        faceDetectThread.setListener(this);
        faceDetectThread.start();
        faceDetectThread.pauseThread();
        dataDispatchThread = new DataDispatchThread(drawPreviewFrameThread, faceDetectThread);
        dataDispatchThread.start();
        try {
            camera.setPreviewDisplay(holder);
        } catch (Exception e) {
            Timber.e("onSurfaceTextureAvailable:" + Log.getStackTraceString(e));
        }
        try {
            camera.startPreview();
        } catch (Exception e) {
            Timber.e("onSurfaceTextureAvailable:" + Log.getStackTraceString(e));
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
    }
}
