package com.mc.app.hotel.common.facealignment;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.adapter.RecordManageRecyclerViewAdapter;
import com.mc.app.hotel.common.facealignment.database.FaceRecordPager;
import com.mc.app.hotel.common.facealignment.database.FaceRecordRepository;
import com.mc.app.hotel.common.facealignment.event.EventSelectRecord;
import com.mc.app.hotel.common.facealignment.model.FaceRecord;
import com.mc.app.hotel.common.facealignment.view.ExportRecordDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class RecordManageActivity extends AppCompatActivity {

    @BindView(R.id.recordManageRv)
    RecyclerView recordManageRv;
    @BindView(R.id.pageUpBtn)
    ImageButton pageUpBtn;
    @BindView(R.id.currentPageIndexTv)
    TextView currentPageIndexTv;
    @BindView(R.id.totalPageNumTv)
    TextView totalPageNumTv;
    @BindView(R.id.pagePownBtn)
    ImageButton pagePownBtn;
    @BindView(R.id.exportRecordBtn)
    Button exportExcelBtn;
    @BindView(R.id.deleteAllRecordBtn)
    Button deleteAllRecordBtn;
    @BindView(R.id.idPhotoIv)
    ImageView idPhotoIv;
    @BindView(R.id.camPhotoIv)
    ImageView camPhotoIv;
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
    @BindView(R.id.similarityTv)
    TextView similarityTv;
    @BindView(R.id.addressTv)
    TextView addressTv;
    @BindView(R.id.nationTv)
    TextView nationTv;
    @BindView(R.id.issueAuthorityTv)
    TextView issueAuthorityTv;

    RecordManageRecyclerViewAdapter recordManageRecyclerViewAdapter;
    FaceRecordPager faceRecordPager;
    List<FaceRecord> faceRecords = new LinkedList<>();
    int pageIndex = 0;
    int totalPageNum = 0;
    Handler mainHandler = null;
    ExportRecordDialogFragment exportRecordDialogFragment;
    @BindView(R.id.jumpPageEt)
    EditText jumpPageEt;
    @BindView(R.id.jumpPageBtn)
    Button jumpPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_manage);
        ButterKnife.bind(this);
        mainHandler = new Handler(Looper.getMainLooper());
        exportRecordDialogFragment = ExportRecordDialogFragment.newInstance();
        recordManageRv.setItemAnimator(null);
        recordManageRecyclerViewAdapter = new RecordManageRecyclerViewAdapter();
        recordManageRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recordManageRv.setAdapter(recordManageRecyclerViewAdapter);
        faceRecordPager = new FaceRecordPager();
        freezeUI();
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    faceRecordPager.prepare();
                    pageIndex = faceRecordPager.pageDown(faceRecords);
                    totalPageNum = faceRecordPager.getTotalPageNum();
                } catch (Exception e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (o != null && o instanceof Exception) {
                    Timber.e("加载数据库发生异常" + Log.getStackTraceString((Exception) o));
                    Toast.makeText(getApplicationContext(), "加载数据库发生异常", Toast.LENGTH_SHORT).show();
                    RecordManageActivity.this.finish();
                    return;
                }
                recordManageRecyclerViewAdapter.add(faceRecords);
                currentPageIndexTv.setText(String.valueOf(pageIndex));
                totalPageNumTv.setText(String.valueOf(totalPageNum));
                unFreezeUI();
            }
        }.execute();
    }

    private void clearFaceAlignmentInfo() {
        nameTv.setText("");
        sexTv.setText("");
        idNumTv.setText("");
        termTv.setText("");
        birthdayTv.setText("");
        similarityTv.setText("");
        nationTv.setText("");
        addressTv.setText("");
        issueAuthorityTv.setText("");
        idPhotoIv.setImageResource(R.drawable.ic_face);
        camPhotoIv.setImageResource(R.drawable.ic_face);
    }

    private void populateFaceAlignmentInfo(FaceRecord faceRecord) {
        nameTv.setText(faceRecord.getName());
        sexTv.setText(faceRecord.getSex());
        idNumTv.setText(faceRecord.getIdNumber());
        termTv.setText(faceRecord.getTermBegin() + "~" + faceRecord.getTermEnd());
        birthdayTv.setText(faceRecord.getBirthday());
        addressTv.setText(faceRecord.getAddress());
        nationTv.setText(faceRecord.getNation());
        issueAuthorityTv.setText(faceRecord.getIssueAuthority());
        similarityTv.setText(String.format("%.2f%%", faceRecord.getSimilarity()));
        idPhotoIv.setImageBitmap(BitmapFactory.decodeByteArray(faceRecord.getIdPhoto(), 0, faceRecord.getIdPhoto().length));
        camPhotoIv.setImageBitmap(BitmapFactory.decodeByteArray(faceRecord.getCamPhoto(), 0, faceRecord.getCamPhoto().length));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectRecord(EventSelectRecord event) {
        freezeUI();
        new AsyncTask<FaceRecord, Object, FaceRecord>() {
            @Override
            protected void onPreExecute() {
                clearFaceAlignmentInfo();
            }

            @Override
            protected FaceRecord doInBackground(FaceRecord... params) {
                return FaceRecordRepository.getInstance().getRecordByIdWithPhoto(params[0].getRecordId());
            }

            @Override
            protected void onPostExecute(FaceRecord faceRecord) {
                if (faceRecord == null) {
                    Timber.e("查询数据失败");
                    Toast.makeText(getApplicationContext(), "查询数据失败", Toast.LENGTH_SHORT).show();
                } else {
                    populateFaceAlignmentInfo(faceRecord);
                }
                unFreezeUI();

            }
        }.execute(event.getFaceRecord());
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    private void freezeUI() {
        jumpPageBtn.setEnabled(false);
        jumpPageEt.setEnabled(false);
        recordManageRv.setEnabled(false);
        pagePownBtn.setEnabled(false);
        pageUpBtn.setEnabled(false);
        deleteAllRecordBtn.setEnabled(false);
        exportExcelBtn.setEnabled(false);
    }

    private void unFreezeUI() {
        jumpPageBtn.setEnabled(true);
        jumpPageEt.setEnabled(true);
        recordManageRv.setEnabled(true);
        pagePownBtn.setEnabled(true);
        pageUpBtn.setEnabled(true);
        deleteAllRecordBtn.setEnabled(true);
        exportExcelBtn.setEnabled(true);
    }


    @OnClick({R.id.jumpPageBtn, R.id.pageUpBtn, R.id.pagePownBtn, R.id.exportRecordBtn, R.id.deleteAllRecordBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.jumpPageBtn:
                freezeUI();
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            int index = Integer.valueOf((String) params[0]);
                            pageIndex = faceRecordPager.jumpTo(index, faceRecords);
                        } catch (Exception e) {
                            return e;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        if (o != null) {
                            if (o instanceof NumberFormatException) {
                                Toast.makeText(getApplicationContext(), "错误的页码", Toast.LENGTH_SHORT).show();
                                unFreezeUI();
                                return;
                            } else {
                                Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
                                RecordManageActivity.this.finish();
                                return;
                            }
                        } else {
                            currentPageIndexTv.setText(String.valueOf(pageIndex));
                            recordManageRecyclerViewAdapter.add(faceRecords);
                            unFreezeUI();
                        }
                    }
                }.execute(jumpPageEt.getEditableText().toString().trim());
                break;
            case R.id.pageUpBtn:
                freezeUI();
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            pageIndex = faceRecordPager.pageUp(faceRecords);
                        } catch (Exception e) {
                            return e;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        if (o != null) {
                            if (o instanceof FaceRecordPager.PageUpException) {
                                Toast.makeText(getApplicationContext(), "没有上一页了", Toast.LENGTH_SHORT).show();
                                unFreezeUI();
                                return;
                            } else if (o instanceof Exception) {
                                Timber.e("获取数据失败:" + Log.getStackTraceString((Exception) o));
                                Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
                                RecordManageActivity.this.finish();
                                return;
                            }
                        }
                        currentPageIndexTv.setText(String.valueOf(pageIndex));
                        recordManageRecyclerViewAdapter.add(faceRecords);
                        unFreezeUI();
                    }
                }.execute();
                break;
            case R.id.pagePownBtn:
                freezeUI();
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            pageIndex = faceRecordPager.pageDown(faceRecords);
                        } catch (Exception e) {
                            return e;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        if (o != null) {
                            if (o instanceof FaceRecordPager.PageDownException) {
                                Toast.makeText(getApplicationContext(), "没有下一页了", Toast.LENGTH_SHORT).show();
                                unFreezeUI();
                                return;
                            } else if (o instanceof Exception) {
                                Timber.e("获取数据失败:" + Log.getStackTraceString((Exception) o));
                                Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
                                RecordManageActivity.this.finish();
                                return;
                            }
                        }
                        currentPageIndexTv.setText(String.valueOf(pageIndex));
                        recordManageRecyclerViewAdapter.add(faceRecords);
                        unFreezeUI();
                    }
                }.execute();

                break;
            case R.id.exportRecordBtn:
                exportRecordDialogFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.deleteAllRecordBtn:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.WARNNING)
                        .setMessage(R.string.CONFIRM_CLEAR_DATABASE)
                        .setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                freezeUI();
                                new AsyncTask() {
                                    @Override
                                    protected Object doInBackground(Object[] params) {
                                        FaceRecordRepository.getInstance().deleteAllRecord();
                                        faceRecordPager = new FaceRecordPager();
                                        faceRecordPager.prepare();
                                        totalPageNum = 0;
                                        pageIndex = 0;
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Object o) {
                                        currentPageIndexTv.setText(String.valueOf(pageIndex));
                                        totalPageNumTv.setText(String.valueOf(totalPageNum));
                                        recordManageRecyclerViewAdapter.clear();
                                        clearFaceAlignmentInfo();
                                        unFreezeUI();
                                    }
                                }.execute();
                            }
                        })
                        .setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
        }
    }

}
