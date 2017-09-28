package com.here.publish.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.android.dialog.picker.DataPickerDialog;
import com.here.R;
import com.here.adapter.PublishImageAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Mood;
import com.here.immediate.NewImmediateActivity;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends MvpActivity<SharePresenter> implements ShareContract {

    @Bind(R.id.et_share_title)
    EditText etShareTitle;
    @Bind(R.id.et_share_describe)
    EditText etShareDescribe;
    @Bind(R.id.rv_share_image)
    RecyclerView rvShareImage;
    @Bind(R.id.tv_share_kind)
    TextView tvShareKind;
    @Bind(R.id.rl_share_kind)
    RelativeLayout rlShareKind;
    @Bind(R.id.btn_publish_share)
    Button btnPublishShare;
    @Bind(R.id.ll_share_title)
    LinearLayout llShareTitle;

    private Mood mood = new Mood();

    private PublishImageAdapter adapter;

    public static final int REQUEST_CODE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_share);
        initHome();
        mvpPresenter.attachView(this);
        rvShareImage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        List<String> images = new ArrayList<>();
        images.add("add image");
        adapter = new PublishImageAdapter(R.layout.item_publish_image,images);
        adapter.setActivity(new WeakReference<Activity>(this));
        rvShareImage.setAdapter(adapter);
        adapter.setListener(new PublishImageAdapter.OnItemClickListener() {
            @Override
            public void onClick() {
                selectPic();
            }
        });
    }


    @Override
    protected SharePresenter createPresenter() {
        return new SharePresenter();
    }

    @OnClick({R.id.rl_share_kind, R.id.btn_publish_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_share_kind:
                selectKind();
                break;
            case R.id.btn_publish_share:
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

        if (getCcamra()){
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
                        mood.setKind(itemValue);
                        tvShareKind.setText(itemValue);
                    }
                }).create();

        dialog.show();
    }

    @Override
    public Mood getMoodInfo() {
        mood.setTitle(etShareTitle.getText().toString());
        mood.setContent(etShareDescribe.getText().toString());
        return mood;
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
    public void publishFail(String error) {
        adapter.getData().add("add image");
        adapter.notifyDataSetChanged();
        toastShow(error);
    }
}
