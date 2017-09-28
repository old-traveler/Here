package com.here.personal.accusation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.android.dialog.picker.DataPickerDialog;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.Accusation;
import com.here.bean.User;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccusationActivity extends MvpActivity<AccusationPresenter> implements AccusationContract {


    @Bind(R.id.et_accusation_content)
    EditText etAccusationContent;
    @Bind(R.id.et_accusation_contract)
    EditText etAccusationContract;
    @Bind(R.id.tv_accusation_kind)
    TextView tvAccusationKind;
    private Accusation accusation = new Accusation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accusation);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_accusation);
        initHome();
        mvpPresenter.attachView(this);
    }

    @Override
    protected AccusationPresenter createPresenter() {
        return new AccusationPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_accusation,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.submit_accusation){
            accusation.setContent(etAccusationContent.getText().toString());
            accusation.setNumber(etAccusationContract.getText().toString());
            mvpPresenter.submit(accusation);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.tv_accusation_kind)
    public void onViewClicked() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = new ArrayList<>();
        data.add("发布虚假活动");
        data.add("咋骗钱财");
        data.add("违规违法");
        data.add("侮辱他人");
        data.add("侵犯隐私");
        data.add("其他");
        DataPickerDialog dialog = builder.setUnit("")
                .setData(data).setSelection(0).setTitle("类型")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        tvAccusationKind.setText(itemValue);
                        accusation.setKind(itemValue);
                    }
                }).create();
        dialog.show();
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
    public void submitSuccess() {
        new AlertView("提交成功", "我们会第一时间核实，\n" +
                "进行公正的处理。", "确定", null, null, this,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == -1) {
                    finish();
                }
            }
        }).show();
    }

    @Override
    public void showTips(String error) {
        toastShow(error);
    }

    @Override
    public User getUser() {
        return (User) getIntent().getSerializableExtra("user");
    }
}
