package com.here.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.HereApplication;
import com.here.R;
import com.here.bean.Account;

import java.util.List;

/**
 * Created by hyc on 2017/7/2 23:03
 */

public class AccountAdapter extends BaseQuickAdapter<Account> {


    public AccountAdapter(int layoutResId, List<Account> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Account account) {
        baseViewHolder.setText(R.id.tv_account_username,account.getUsername());
        if (!TextUtils.isEmpty(account.getImageUrl())){
            Glide.with(HereApplication.getContext())
                    .load(account.getImageUrl())
                    .into((ImageView) baseViewHolder.getView(R.id.cv_account_head));
        }else {
            Glide.with(HereApplication.getContext())
                    .load(R.drawable.head_info)
                    .into((ImageView) baseViewHolder.getView(R.id.cv_account_head));
        }
        if (getData().indexOf(account)==0){
            baseViewHolder.getView(R.id.iv_is_selected).setVisibility(View.VISIBLE);
        }
    }
}
