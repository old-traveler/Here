package com.here.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.HereApplication;
import com.here.R;
import com.here.bean.Account;
import com.here.login.LoginActivity;
import com.here.setting.SettingActivity;
import com.here.util.AccountUtil;
import com.here.util.FindUtil;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/2 23:03
 */

public class AccountAdapter extends BaseQuickAdapter<Account> {


    public interface OnItemClickListener{
        void onClick(Account account);
    }

    private OnItemClickListener listener;

    public AccountAdapter(int layoutResId, List<Account> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final Account account) {
        if (TextUtils.isEmpty(account.getName())){
            baseViewHolder.setText(R.id.tv_account_username,account.getUsername());
        }else {
            baseViewHolder.setText(R.id.tv_account_username,account.getName());
        }
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

        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    if (!account.getUsername().equals(BmobUser.getCurrentUser().getUsername())){
                        listener.onClick(account);
                    }
                }
            }
        });

        baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertView("温馨提示", "是否删除该账号信息", "确定", new String[]{"取消"}, null, mContext,
                        AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == -1) {
                            AccountUtil.deleteAccount(account);
                            notifyItemRemoved(baseViewHolder.getAdapterPosition());
                        }
                    }
                }).show();
                return true;
            }
        });

    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
