package com.here.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.personal.PersonalActivity;
import com.here.personal.other.OtherInfoActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends MvpActivity<SearchPresenter> implements SearchContract {

    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.cv_search_head)
    CircleImageView cvSearchHead;
    @Bind(R.id.tv_search_username)
    TextView tvSearchUsername;
    @Bind(R.id.rl_search_info)
    RelativeLayout rlSearchInfo;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_search);
        initHome();
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @OnClick({R.id.btn_search, R.id.rl_search_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                mvpPresenter.searchUser();
                break;
            case R.id.rl_search_info:
                if (BmobUser.getCurrentUser().getObjectId()
                        .equals(user.getObjectId())){
                    Intent intent = new Intent(SearchActivity.this, PersonalActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent =new Intent(this,OtherInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("other",user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }

    @Override
    public String getSearchInfo() {
        return etSearch.getText().toString();
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
    public void loadingSuccess(User user) {
        Glide.with(this)
                .load(user.getHeadImageUrl())
                .into(cvSearchHead);
        tvSearchUsername.setText(user.getUsername());
        rlSearchInfo.setVisibility(View.VISIBLE);
        this.user = user;
    }

    @Override
    public void fail(String error) {
        rlSearchInfo.setVisibility(View.GONE);
        toastShow(error);
    }
}
