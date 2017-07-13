package com.here.feedback;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.here.R;
import com.here.base.MvpActivity;
import butterknife.ButterKnife;

public class FeedBackActivity extends MvpActivity<FeedBackPresenter> implements FeedBackContract {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_feedback);
        initHome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.submit_feedback){
            toastShow("提交");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected FeedBackPresenter createPresenter() {
        return new FeedBackPresenter();
    }


}
