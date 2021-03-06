package com.miaolian.facead;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.miaolian.facead.event.EventDataSaveRequest;
import com.miaolian.facead.event.EventReceiveNFCTag;
import com.miaolian.facead.event.EventTakePhotoRequest;
import com.miaolian.facead.event.EventTakePhotoResponse;
import com.miaolian.facead.service.CheckUpdateService;
import com.miaolian.facead.thread.CheckUpdateThread;
import com.miaolian.facead.thread.DatabaseSaveThread;
import com.miaolian.facead.util.PermissionUtil;
import com.miaolian.facead.util.PrefUtil;
import com.miaolian.facead.util.ServiceUtil;
import com.miaolian.facead.util.StateUtil;
import com.miaolian.facead.view.CameraFaceAlignmentFragment;
import com.miaolian.facead.view.IDCardFaceAlignmentFragment;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int IDCARD_PHOTO_CAMEAR_REQUEST = 0x01;
    private static final int FACE_PHOTO_CAMEAR_REQUEST = 0x02;
    Timer checkUpdateTimer;
    DatabaseSaveThread databaseSaveThread = null;
    NfcAdapter nfcAdapter = null;
    PendingIntent nfcPi = null;
    IntentFilter[] nfcIfs = null;
    String[][] techLists = null;
    Handler mainHandler;
    IDCardFaceAlignmentFragment idCardFaceAlignmentFragment;
    CameraFaceAlignmentFragment cameraFaceAlignmentFragment;
    String currentLinkType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        idCardFaceAlignmentFragment = IDCardFaceAlignmentFragment.newInstance();
        cameraFaceAlignmentFragment = CameraFaceAlignmentFragment.newInstance();
        databaseSaveThread = new DatabaseSaveThread();
        databaseSaveThread.start();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.enableEncrypt(true);
        if (StateUtil.SupportNFC) {
            initNFC();
        } else {
            Toast.makeText(this, R.string.DO_NOT_SUPPORT_NFC, Toast.LENGTH_SHORT).show();
        }
        mainHandler = new Handler(Looper.getMainLooper());
        checkUpdateTimer = new Timer();
        checkUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                CheckUpdateService.startCheckUpdate(getApplicationContext());
            }
        }, 500, CheckUpdateThread.CHECK_UPDATE_INTERVAL_MS);

    }

    private void initNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (PrefUtil.getLinkType().equals(ServiceUtil.NFC) && nfcAdapter.isEnabled() == false) {
            Toast.makeText(this, R.string.NFC_NOT_OPEN, Toast.LENGTH_SHORT).show();
        }
        nfcPi = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
        nfcIfs = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)};
        techLists = new String[][]{new String[]{NfcB.class.getName()}, new String[]{IsoDep.class.getName()}};
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
        MobclickAgent.setDebugMode(PrefUtil.getDebugMode());
        MobclickAgent.onResume(this);
        EventBus.getDefault().register(this);
        if (StateUtil.SupportNFC) {
            nfcAdapter.enableForegroundDispatch(this, nfcPi, nfcIfs, techLists);
        }
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
        MobclickAgent.onPause(this);
        EventBus.getDefault().unregister(this);
        if (StateUtil.SupportNFC) {
            nfcAdapter.disableForegroundDispatch(this);
        }
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
        databaseSaveThread.submit(request.getFaceRecord());
    }

    @Override
    protected void onDestroy() {
        try {
            checkUpdateTimer.cancel();
        } catch (Exception e) {
        }
        databaseSaveThread.destroyThread();
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
                    EventBus.getDefault().post(new EventTakePhotoResponse((Bitmap) data.getExtras().get("data"), CameraFaceAlignmentFragment.PhotoType.IDCARD));
                } catch (Exception e) {

                }
                break;
            case FACE_PHOTO_CAMEAR_REQUEST:
                try {
                    EventBus.getDefault().post(new EventTakePhotoResponse((Bitmap) data.getExtras().get("data"), CameraFaceAlignmentFragment.PhotoType.FACE));
                } catch (Exception e) {

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
