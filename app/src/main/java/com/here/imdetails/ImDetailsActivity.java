package com.here.imdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.here.R;
import com.here.adapter.DetailsImageAdapter;
import com.here.base.MvpActivity;
import com.here.bean.ImActivity;
import com.here.imdetails.report.ReportActivity;
import com.here.personal.PersonalActivity;
import com.here.personal.other.OtherInfoActivity;
import com.here.view.MyGridLayoutManager;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class ImDetailsActivity extends MvpActivity<ImDetailsPresenter> implements ImDetailsContract {

    @Bind(R.id.tb_im_details)
    Toolbar tbImDetails;
    @Bind(R.id.rv_im_details)
    RecyclerView rvImDetails;
    @Bind(R.id.tv_details_name)
    TextView tvDetailsName;
    @Bind(R.id.tv_publisher_time)
    TextView tvPublisherTime;
    @Bind(R.id.cv_details_head)
    CircleImageView cvDetailsHead;
    @Bind(R.id.tv_details_nickname)
    TextView tvDetailsNickname;
    @Bind(R.id.tv_details_follow)
    Button tvDetailsFollow;
    @Bind(R.id.tv_details_address)
    TextView tvDetailsAddress;
    @Bind(R.id.tv_details_kind)
    TextView tvDetailsKind;
    @Bind(R.id.tv_details_over)
    TextView tvDetailsOver;
    @Bind(R.id.tv_details_count)
    TextView tvDetailsCount;
    @Bind(R.id.tv_details_describe)
    TextView tvDetailsDescribe;
    @Bind(R.id.btn_details_apply)
    Button btnDetailsApply;
    DetailsImageAdapter detailsImageAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_details);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_im_details);
        initHome();
        mvpPresenter.attachView(this);
        mvpPresenter.loadingData();
        mvpPresenter.isFollow();
    }

    @Override
    protected ImDetailsPresenter createPresenter() {
        return new ImDetailsPresenter();
    }


    @Override
    public ImActivity getImActivity() {
        return (ImActivity) getIntent().getSerializableExtra("imActivity");
    }

    @Override
    public void loadingData(ImActivity imActivity) {
        tvDetailsName.setText(imActivity.getTitle());
        tvPublisherTime.setText(imActivity.getPublishTime());
        tvDetailsNickname.setText(imActivity.getPublisher().getNickname());
        tvDetailsAddress.setText(imActivity.getLocation());
        tvDetailsKind.setText("活动类型：" + imActivity.getKind());
        tvDetailsCount.setText("参与人数：" + imActivity.getNumber());
        tvDetailsOver.setText("结束时间：" + imActivity.getOverTime().split("-")[3]);
        tvDetailsDescribe.setText(imActivity.getDescribe());
        Glide.with(this)
                .load(imActivity.getPublisher().getHeadImageUrl())
                .into(cvDetailsHead);
        Calendar now = Calendar.getInstance();
        int year  = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        if (!imActivity.getPublishDate().equals(year+"-"+month+1+"-"+day)){
            btnDetailsApply.setVisibility(View.GONE);
        }
        if (imActivity.getImages() == null || imActivity.getImages().length < 1) {
            rvImDetails.setVisibility(View.GONE);
            return;
        }
        List<String> list = Arrays.asList(imActivity.getImages());
        if (list.size() == 1) {
            detailsImageAdapter = new DetailsImageAdapter(R.layout.item_one_image, list);
            rvImDetails.setLayoutManager(new MyGridLayoutManager(this, 1));
        } else if (list.size() == 2) {
            detailsImageAdapter = new DetailsImageAdapter(R.layout.item_two_image, list);
            rvImDetails.setLayoutManager(new MyGridLayoutManager(this, 2));
        } else {
            detailsImageAdapter = new DetailsImageAdapter(R.layout.item_im_details, list);
            rvImDetails.setLayoutManager(new MyGridLayoutManager(this, 3));
        }
        detailsImageAdapter.setActivityWeakReference(new WeakReference<Activity>(this));
        rvImDetails.setAdapter(detailsImageAdapter);
        if (!imActivity.isNeedApply()) {
            btnDetailsApply.setText("加入");
        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_im_details,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.report){
            Intent intent = new Intent(this, ReportActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("activity",getImActivity());
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void fail(String error) {
        toastShow(error);
    }

    @Override
    public void sendApplySuccess() {
        new AlertView("提示", "请求信息发送成功,若十分钟没有应答将自动取消申请", null
                , new String[]{"确定"}, null, this, AlertView.Style.Alert, null).show();
    }

    @Override
    public void joinSuccess() {
        new AlertView("提示", "参与活动成功，请尽快联系发布者", null
                , new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                finish();
            }
        }).show();
    }

    @Override
    public void joinFail(String error) {
        toastShow(error);
    }

    @Override
    public void followSuccess() {
        new AlertView("提示", "关注成功！", null
                , new String[]{"确定"}, null, this, AlertView.Style.Alert, null).show();
        tvDetailsFollow.setText("已关注");
        tvDetailsFollow.setClickable(false);
    }

    @Override
    public void hadFollowed() {
        tvDetailsFollow.setText("已关注");
        tvDetailsFollow.setClickable(false);
    }


    @OnClick({R.id.btn_details_apply, R.id.cv_details_head,R.id.tv_details_follow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_details_follow:
                mvpPresenter.followUser();
                break;
            case R.id.btn_details_apply:
                mvpPresenter.apply();
                break;
            case R.id.cv_details_head:
                if (getImActivity().getPublisher().getObjectId()
                        .equals(BmobUser.getCurrentUser().getObjectId())) {
                    Intent intent = new Intent(this, PersonalActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, OtherInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("other", getImActivity().getPublisher());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }



}
