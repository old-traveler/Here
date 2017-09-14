package com.here.account;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.here.R;
import com.here.adapter.AccountAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Account;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;


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
        accounts=null;
        AccountAdapter accountAdapter=new AccountAdapter(R.layout.item_account,account);
        rvAccount.setLayoutManager(new LinearLayoutManager(this));
        rvAccount.setAdapter(accountAdapter);
    }
}
