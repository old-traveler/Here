package com.here.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.here.R;
import com.here.adapter.AccountAdapter;
import com.here.adapter.PublishImageAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Account;
import com.here.bean.User;
import com.here.main.MainActivity;
import com.here.util.AccountUtil;
import com.here.util.UserUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;


public class AccountActivity extends MvpActivity<AccountPresenter> implements AccountContract {

    @Bind(R.id.rv_account)
    RecyclerView rvAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_account);
        initHome();
        initData();
    }

    @Override
    protected AccountPresenter createPresenter() {
        return new AccountPresenter();
    }


    @Override
    public void initData() {
        List<Account> accounts=DataSupport.findAll(Account.class);
        List<Account> account =new ArrayList<>();
        for (int i = accounts.size()-1; i >=0; i--) {
            account.add(accounts.get(i));
        }
        AccountAdapter accountAdapter=new AccountAdapter(R.layout.item_account,account);
        rvAccount.setLayoutManager(new LinearLayoutManager(this));
        rvAccount.setItemAnimator(new DefaultItemAnimator());
        rvAccount.setAdapter(accountAdapter);
        accountAdapter.setListener(new AccountAdapter.OnItemClickListener() {
            @Override
            public void onClick(final Account account) {
                showProgressDialog();
                User user = new User();
                user.setUsername(account.getUsername());
                user.setPassword(account.getPassword());
                UserUtil.login(user, new UserUtil.OnDealListener() {
                    @Override
                    public void success() {
                        finishAll();
                        dissmiss();
                        User user = BmobUser.getCurrentUser(User.class);
                        AccountUtil.addAccount(user.getNickname(),user.getUsername(),
                                account.getPassword(),user.getHeadImageUrl(),false);
                        startActivity(new Intent(AccountActivity
                                .this, MainActivity.class));
                    }

                    @Override
                    public void fail(String error) {
                        dissmiss();
                        toastShow(error);
                    }
                });
            }
        });

    }
}
