package com.miaolian.facead;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.miaolian.facead.view.AboutFragment;
import com.miaolian.facead.view.CameraSettingFragment;
import com.miaolian.facead.view.DebugSettingFragment;
import com.miaolian.facead.view.FaceAlignmentSettingFragment;
import com.miaolian.facead.view.IDCardSettingFragment;
import com.miaolian.facead.view.ResetFragment;
import com.miaolian.facead.view.nfcregister.NFCActiveFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar toolBar;

    FaceAlignmentSettingFragment faceAlignmentSettingFragment;
    IDCardSettingFragment idCardSettingFragment;
    AboutFragment aboutFragment;
    NFCActiveFragment nfcActiveFragment;
    CameraSettingFragment cameraSettingFragment;
    DebugSettingFragment debugSettingFragment;
    ResetFragment resetFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        faceAlignmentSettingFragment = FaceAlignmentSettingFragment.newInstance();
        idCardSettingFragment = IDCardSettingFragment.newInstance();
        aboutFragment = AboutFragment.newInstance();
        nfcActiveFragment = NFCActiveFragment.newInstance();
        cameraSettingFragment = CameraSettingFragment.newInstance();
        debugSettingFragment = DebugSettingFragment.newInstance();
        resetFragment = ResetFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFrag(faceAlignmentSettingFragment,false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.debugSettingRb, R.id.resetRb, R.id.cameraSettingRb, R.id.faceAlignmentSettingRb, R.id.idCardSettingRb, R.id.aboutRb, R.id.nfcActiveRb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.faceAlignmentSettingRb:
                showFrag(faceAlignmentSettingFragment,true);
                break;
            case R.id.idCardSettingRb:
                showFrag(idCardSettingFragment,true);
                break;
            case R.id.aboutRb:
                showFrag(aboutFragment,true);
                break;
            case R.id.nfcActiveRb:
                showFrag(nfcActiveFragment,true);
                break;
            case R.id.cameraSettingRb:
                showFrag(cameraSettingFragment,true);
                break;
            case R.id.debugSettingRb:
                showFrag(debugSettingFragment,true);
                break;
            case R.id.resetRb:
                showFrag(resetFragment,true);
                break;
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

}
