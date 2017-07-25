package com.mc.app.hotel.common.facealignment;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.mc.app.hotel.R;
import com.mc.app.hotel.activity.DeclareInActivity;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.facealignment.event.EventDataSaveRequest;
import com.mc.app.hotel.common.facealignment.event.EventReceiveNFCTag;
import com.mc.app.hotel.common.facealignment.event.EventTakePhotoRequest;
import com.mc.app.hotel.common.facealignment.event.EventTakePhotoResponse;
import com.mc.app.hotel.common.facealignment.util.PermissionUtil;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;
import com.mc.app.hotel.common.facealignment.util.ServiceUtil;
import com.mc.app.hotel.common.facealignment.view.CameraFaceAlignmentFragment;
import com.mc.app.hotel.common.facealignment.view.IDCardFaceAlignmentFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import timber.log.Timber;

import static com.mc.app.hotel.common.facealignment.view.CameraFaceAlignmentFragment.PhotoType.FACE;
import static com.mc.app.hotel.common.facealignment.view.CameraFaceAlignmentFragment.PhotoType.IDCARD;


public class FaceAilgmentActivity extends AppCompatActivity {
    private static final String TAG = FaceAilgmentActivity.class.getSimpleName();
    private static final int IDCARD_PHOTO_CAMEAR_REQUEST = 0x01;
    private static final int FACE_PHOTO_CAMEAR_REQUEST = 0x02;
    PendingIntent nfcPi = null;
    IntentFilter[] nfcIfs = null;
    String[][] techLists = null;
    Handler mainHandler;
    IDCardFaceAlignmentFragment idCardFaceAlignmentFragment;
    CameraFaceAlignmentFragment cameraFaceAlignmentFragment;
    String currentLinkType = "";
    String roomNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_alignment);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        idCardFaceAlignmentFragment = IDCardFaceAlignmentFragment.newInstance();
        cameraFaceAlignmentFragment = CameraFaceAlignmentFragment.newInstance();
        mainHandler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REUQEST_PERMISSION_CODE:
                String[] deniedPermissions = PermissionUtil.handleRequestPermissionResult(permissions, grantResults);
                if (deniedPermissions != null) {
                    PermissionUtil.requestPermission(this, deniedPermissions);
                } else {
                    switch (PrefUtil.getLinkType()) {
                        case ServiceUtil.CAMERA:
                            showFrag(cameraFaceAlignmentFragment, false);
                            break;
                        default:
                            showFrag(idCardFaceAlignmentFragment, false);
                            break;
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
//        if (StateUtil.SupportNFC) {
//            nfcAdapter.enableForegroundDispatch(this, nfcPi, nfcIfs, techLists);
//        }
        if (currentLinkType.equals(PrefUtil.getLinkType()) == false && PermissionUtil.requestPermission(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.NFC, Manifest.permission.READ_PHONE_STATE})) {
            switch (PrefUtil.getLinkType()) {
                case ServiceUtil.CAMERA:
                    showFrag(cameraFaceAlignmentFragment, false);
                    break;
                default:
                    showFrag(idCardFaceAlignmentFragment, false);
                    break;
            }
        }
    }

    private void showFrag(Fragment frag, boolean animated) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (animated) {
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            ft.replace(R.id.fragContainer, frag).commit();
        } catch (Exception e) {
            Timber.e("showFrag:" + Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
//        if (StateUtil.SupportNFC) {
//            nfcAdapter.disableForegroundDispatch(this);
//        }
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            EventBus.getDefault().post(new EventReceiveNFCTag((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)));
        } catch (Exception e) {
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventDataSaveRequest(EventDataSaveRequest request) {
//        databaseSaveThread.submit(request.getFaceRecord());

        Intent i = new Intent(this, DeclareInActivity.class);
        Bundle b = new Bundle();
        if (getIntent() != null && getIntent().getExtras() != null) {
            b = getIntent().getExtras();
        }
        b.putSerializable(Constants.READ_ID_CARD, request.getFaceRecord());
        b.putInt(Constants.FROM, request.getFrom());
        i.putExtras(b);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
//        try {
//            checkUpdateTimer.cancel();
//        } catch (Exception e) {
//        }
//        databaseSaveThread.destroyThread();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTakePhotoRequest(EventTakePhotoRequest request) {
        switch (request.getType()) {
            case IDCARD:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), IDCARD_PHOTO_CAMEAR_REQUEST);
                break;
            case FACE:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), FACE_PHOTO_CAMEAR_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IDCARD_PHOTO_CAMEAR_REQUEST:
                try {
                    EventBus.getDefault().post(new EventTakePhotoResponse((Bitmap) data.getExtras().get("data"), IDCARD));
                } catch (Exception e) {

                }
                break;
            case FACE_PHOTO_CAMEAR_REQUEST:
                try {
                    EventBus.getDefault().post(new EventTakePhotoResponse((Bitmap) data.getExtras().get("data"), FACE));
                } catch (Exception e) {

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
