package com.here.immediate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.example.android.dialog.picker.DataPickerDialog;
import com.example.android.dialog.picker.TimePickerDialog;
import com.here.R;
import com.here.adapter.PublishImageAdapter;
import com.here.base.MvpActivity;
import com.here.bean.ImActivity;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewImmediateActivity extends MvpActivity<NewImmediatePresenter> implements NewImmediateContract {

    @Bind(R.id.rv_publish_image)
    RecyclerView rvPublishImage;
    @Bind(R.id.et_activity_name)
    EditText etActivityName;
    @Bind(R.id.et_activity_describe)
    EditText etActivityDescribe;
    @Bind(R.id.rl_activity_location)
    RelativeLayout rlActivityLocation;
    @Bind(R.id.rl_over_time)
    RelativeLayout rlOverTime;
    @Bind(R.id.et_join_number)
    EditText etJoinNumber;
    @Bind(R.id.rl_activity_kind)
    RelativeLayout rlActivityKind;
    @Bind(R.id.cb_is_apply)
    CheckBox cbIsApply;
    @Bind(R.id.btn_publish_immediate)
    Button btnPublishImmediate;
    @Bind(R.id.tv_over_time)
    TextView tvOverTime;
    @Bind(R.id.tv_kind)
    TextView tvKind;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.rl_complete)
    RelativeLayout rlComplete;
    @Bind(R.id.ll_im_activity)
    LinearLayout llImActivity;


    private PublishImageAdapter adapter;

    public static final int REQUEST_CODE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_immediate);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_immediate);
        mvpPresenter.attachView(this);
        initHome();
        rvPublishImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        initRecycler(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mvpPresenter.getLocation();
            }
        }, 500);

    }


    @Override
    public void initRecycler(List<String> images) {
        if (images != null) {
            List<String> olds = adapter.getData();
            olds.remove(olds.size() - 1);
            for (int i = 0; i < olds.size(); i++) {
                images.add(i, olds.get(i));
            }
            while (images.size() > 9) {
                images.remove(0);
            }
            String[] strings = new String[images.size()];
            for (int i = 0; i < images.size(); i++) {
                strings[i] = images.get(i);
            }
            imActivity.setImages(strings);
            images.add("add image");
            adapter.setNewData(images);
        } else {
            images = new ArrayList<>();
            images.add("add image");
            adapter = new PublishImageAdapter(R.layout.item_publish_image, images);
            adapter.setActivity(new WeakReference<Activity>(this));
            mvpPresenter.setAddImageListener();
            rvPublishImage.setAdapter(adapter);
        }
    }

    @Override
    public void choicePic() {
        if (getCcamra()){
            SImagePicker
                    .from(NewImmediateActivity.this)
                    .maxCount(9)
                    .rowCount(3)
                    .showCamera(true)
                    .pickMode(SImagePicker.MODE_IMAGE)
                    .forResult(REQUEST_CODE_IMAGE);
        }
    }

    @Override
    public void setListener(PublishImageAdapter.OnItemClickListener listener) {
        adapter.setListener(listener);
    }

    @Override
    public void selectOverTime() {
        TimePickerDialog timeDialog;
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder(this);
        timeDialog = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int[] times) {
                Calendar now = Calendar.getInstance();
                int hour  = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                if (times[0] < hour || times[0]==hour && times[1] <= minute){
                    noFilled("结束时间应在发布时间之后");
                }else {
                    imActivity.setOverTime(now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now
                            .get(Calendar.DAY_OF_MONTH)+"-"+times[0] + ":" + (times[1] < 10 ? "0" : "") + times[1]);
                    tvOverTime.setText(imActivity.getOverTime());
                }

            }
        }).create();
        timeDialog.show();
    }

    @Override
    public int getActivityNumber() {
        return TextUtils.isEmpty(etJoinNumber.getText().toString()) ? 0 : Integer
                .parseInt(etJoinNumber.getText().toString());
    }

    @Override
    public void getLocation() {
        AMapLocationClient mLocationClient = null;
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClientOption.setNeedAddress(true);
        locationClientOption.setOnceLocation(true);
        if (locationClientOption.isOnceLocation()) {
            locationClientOption.setOnceLocationLatest(true);
        }
        locationClientOption.setWifiActiveScan(true);
        locationClientOption.setMockEnable(true);
        locationClientOption.setInterval(2000);
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationOption(locationClientOption);
        mLocationClient.setLocationListener(mvpPresenter);
        mLocationClient.startLocation();
    }

    @Override
    public void selectKind() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = new ArrayList<>();
        data.add(getString(R.string.shopping));
        data.add(getString(R.string.sport));
        data.add(getString(R.string.movie));
        data.add(getString(R.string.game));
        data.add(getString(R.string.delicious_food));
        data.add(getString(R.string.role_game));
        data.add(getString(R.string.sing));
        data.add(getString(R.string.cosmetology));
        data.add(getString(R.string.outside));
        data.add(getString(R.string.bar));
        data.add(getString(R.string.chess));
        data.add(getString(R.string.other));
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("类别")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        imActivity.setKind(itemValue);
                        tvKind.setText(itemValue);
                    }
                }).create();

        dialog.show();
    }

    @Override
    public ImActivity getImActivity() {
        imActivity.setTitle(etActivityName.getText().toString());
        imActivity.setDescribe(etActivityDescribe.getText().toString());
        imActivity.setNeedApply(cbIsApply.isChecked());
        Calendar now = Calendar.getInstance();
        int hour  = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        imActivity.setPublishTime(hour+":"+minute);
        return imActivity;
    }

    @Override
    public void setLocationMessage(String Location) {
        tvLocation.setText(Location);
        rlActivityLocation.setClickable(false);
        imActivity.setLocation(Location);
    }

    @Override
    public void getLocationFail() {
        tvLocation.setText(R.string.get_location_fail);
        rlActivityLocation.setClickable(true);
    }

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void stopLoading() {
        dissmiss();
    }

    @Override
    public void noFilled(String msg) {
        toastShow(msg);
    }

    @Override
    public void uploadSuccess() {
        llImActivity.setVisibility(View.GONE);
        rlComplete.setVisibility(View.VISIBLE);
        toastShow("发布成功");
    }


    @Override
    protected NewImmediatePresenter createPresenter() {
        return new NewImmediatePresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            mvpPresenter.showImages(pathList);
        }
    }

    @OnClick({R.id.rl_activity_location, R.id.rl_over_time, R.id.rl_activity_kind, R.id.btn_publish_immediate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_activity_location:
                mvpPresenter.getLocation();
                break;
            case R.id.rl_over_time:
                selectOverTime();
                break;
            case R.id.rl_activity_kind:
                selectKind();
                break;
            case R.id.btn_publish_immediate:
                mvpPresenter.publishNewImActivity();
                break;

        }
    }
}
