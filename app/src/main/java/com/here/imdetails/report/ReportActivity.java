package com.here.imdetails.report;

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
import com.here.bean.ImActivity;
import com.here.bean.Report;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends MvpActivity<ReportPresenter> implements ReportContract {


    @Bind(R.id.et_report_content)
    EditText etReportContent;
    @Bind(R.id.et_report_contract)
    EditText etReportContract;
    @Bind(R.id.tv_report_kind)
    TextView tvReportKind;
    private Report report = new Report();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_report);
        initHome();
        mvpPresenter.attachView(this);
    }

    @Override
    protected ReportPresenter createPresenter() {
        return new ReportPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.submit){
            report.setContent(etReportContent.getText().toString());
            report.setContractNumber(etReportContract.getText().toString());
            mvpPresenter.submit(report);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.tv_report_kind)
    public void onViewClicked() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = new ArrayList<>();
        data.add("虚假活动");
        data.add("广告宣传");
        data.add("违规违法");
        data.add("欺骗用户");
        data.add("反动宣传");
        data.add("安全事故");
        data.add("色情低俗");
        data.add("其他");
        DataPickerDialog dialog = builder.setUnit("")
                .setData(data).setSelection(1).setTitle("类型")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        tvReportKind.setText(itemValue);
                        report.setKind(itemValue);
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
                "还您一个绿色的平台。", "确定", null, null, this,
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
    public ImActivity getImActivity() {
        return (ImActivity) getIntent().getSerializableExtra("activity");
    }
}
