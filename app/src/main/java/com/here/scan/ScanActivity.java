package com.here.scan;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.util.BitmapUtil;
import com.here.zxing.EncodingHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class ScanActivity extends MvpActivity<ScanPresenter> implements ScanContract {

    @Bind(R.id.iv_scan_code)
    ImageView ivScanCode;
    @Bind(R.id.cv_scan_head)
    CircleImageView cvScanHead;
    @Bind(R.id.tv_scan_name)
    TextView tvScanName;
    @Bind(R.id.rl_scan_code)
    RelativeLayout rlScanCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        initView();
        setToolBar(R.id.tb_scan);
        initHome();

    }

    private void initView() {
        int width=dipToPx(this,160.0f);
        User user = BmobUser.getCurrentUser(User.class);
        ivScanCode.setImageBitmap(BitmapUtil.createCircleImage(createCode(user.getUsername(),width),this,width));
        if (!TextUtils.isEmpty(user.getHeadImageUrl())){
            Glide.with(this)
                    .load(user.getHeadImageUrl())
                    .into(cvScanHead);
        }else {
            Glide.with(this)
                    .load(R.drawable.grils)
                    .into(cvScanHead);
        }

        tvScanName.setText(TextUtils.isEmpty(user.getNickname())?"木头人":user.getNickname());
    }

    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private Bitmap createCode(String message,int width) {

        Bitmap qrCodeBitmap = null;
        try {
            qrCodeBitmap = EncodingHandler.createQRCode(
                    message, width);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return qrCodeBitmap;

    }

    @Override
    protected ScanPresenter createPresenter() {
        return new ScanPresenter();
    }

    @OnClick({R.id.iv_scan_code, R.id.rl_scan_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scan_code:
                break;
            case R.id.rl_scan_code:
                break;
        }
    }
}
