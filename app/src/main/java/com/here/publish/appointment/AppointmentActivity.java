package com.here.publish.appointment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.android.dialog.picker.DataPickerDialog;
import com.here.R;
import com.here.adapter.PublishImageAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Appointment;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentActivity extends MvpActivity<AppointmentPresenter> implements AppointmentContract {

    @Bind(R.id.et_appointment_title)
    EditText etAppointmentTitle;
    @Bind(R.id.et_appointment_content)
    EditText etAppointmentContent;
    @Bind(R.id.rv_appointment_image)
    RecyclerView rvAppointmentImage;
    @Bind(R.id.tv_appointment_location)
    TextView tvAppointmentLocation;
    @Bind(R.id.rl_appointment_location)
    RelativeLayout rlAppointmentLocation;
    @Bind(R.id.et_start_time)
    EditText etStartTime;
    @Bind(R.id.rl_start_time)
    RelativeLayout rlStartTime;
    @Bind(R.id.et_over_time)
    EditText etOverTime;
    @Bind(R.id.rl_over_time)
    RelativeLayout rlOverTime;
    @Bind(R.id.et_join_number)
    EditText etJoinNumber;
    @Bind(R.id.tv_appointment_kind)
    TextView tvAppointmentKind;
    @Bind(R.id.rl_appointment_kind)
    RelativeLayout rlAppointmentKind;
    @Bind(R.id.cb_is_apply)
    CheckBox cbIsApply;
    @Bind(R.id.btn_publish_appointment)
    Button btnPublishAppointment;

    private Appointment appointment = new Appointment();

    private PublishImageAdapter adapter;

    public static final int REQUEST_CODE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_appointment);
        initHome();
        mvpPresenter.location();
        rvAppointmentImage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        List<String> images = new ArrayList<>();
        images.add("add image");
        adapter = new PublishImageAdapter(R.layout.item_publish_image,images);
        adapter.setActivity(new WeakReference<Activity>(this));
        rvAppointmentImage.setAdapter(adapter);
        adapter.setListener(new PublishImageAdapter.OnItemClickListener() {
            @Override
            public void onClick() {
                selectPic();
            }
        });
        mvpPresenter.location();
    }

    @Override
    protected AppointmentPresenter createPresenter() {
        return new AppointmentPresenter();
    }

    @OnClick({R.id.rl_appointment_location, R.id.rl_appointment_kind, R.id.btn_publish_appointment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_appointment_location:
                mvpPresenter.location();
                break;
            case R.id.rl_appointment_kind:
                selectKind();
                break;
            case R.id.btn_publish_appointment:
                mvpPresenter.publish();
                break;
        }
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
    public void selectPic() {
        if (adapter.getData().size()>9){
            toastShow("最多选择9张图片");
            return;
        }

        if (getStorage() && getCcamra()){
            SImagePicker
                    .from(this)
                    .maxCount(10 - adapter.getData().size())
                    .rowCount(3)
                    .showCamera(true)
                    .pickMode(SImagePicker.MODE_IMAGE)
                    .forResult(REQUEST_CODE_IMAGE);
        }else {
            Toast.makeText(mActivity, "请给与读取本地相册权限", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            for (String s : pathList) {
                adapter.getData().add(adapter.getItemCount()-1,s);
                adapter.notifyDataSetChanged();
            }

        }
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
                        appointment.setKind(itemValue);
                        tvAppointmentKind.setText(itemValue);
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void setLocationMessage(String location) {
        tvAppointmentLocation.setText(location);
        rlAppointmentLocation.setClickable(false);
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
    public List<String> getImages() {
        adapter.getData().remove(adapter.getItemCount()-1);
        return adapter.getData();
    }

    @Override
    public void fail(String error) {

        toastShow(error);
    }

    @Override
    public void publishSuccess() {
        new AlertView("提示", "发布成功!", null
                , new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                finish();
            }
        }).show();
    }

    @Override
    public Appointment getAppointment() {
        appointment.setApply(cbIsApply.isChecked());
        appointment.setTitle(etAppointmentTitle.getText().toString());
        appointment.setDescribe(etAppointmentContent.getText().toString());
        appointment.setStartDate(etStartTime.getText().toString());
        appointment.setOverDate(etOverTime.getText().toString());
        if (TextUtils.isEmpty(etJoinNumber.getText().toString())){
            appointment.setJoinNumber(0);
        }else {
            appointment.setJoinNumber(Integer.parseInt(etJoinNumber.getText().toString()));
        }
        return appointment;
    }

    @Override
    public void getLocationFail() {
        tvAppointmentLocation.setText("定位失败,点击重试");
    }

    @Override
    public void publishFail(String error) {

        adapter.getData().add("add image");
        adapter.notifyDataSetChanged();
        toastShow(error);
    }
}
