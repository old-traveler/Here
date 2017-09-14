package com.here.voice;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.here.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMUserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;

public class CallActivity extends AppCompatActivity {

    @Bind(R.id.cv_head)
    CircleImageView cvHead;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);
        Glide.with(this).load(getIntent().getStringExtra("background")).into(cvHead);
        tvNickname.setText(getIntent().getStringExtra("name"));
        soundPool= new SoundPool(1,AudioManager.STREAM_MUSIC,5);
        soundPool.load(this,R.raw.call,1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 0.6f, 0.6f, 1, -1, 1f);
            }
        });
    }

    @OnClick({R.id.cv_accept, R.id.cv_refuse})
    public void onViewClicked(View view) {
        soundPool.autoPause();
        soundPool.release();
        switch (view.getId()) {
            case R.id.cv_accept:
                Intent intent = new Intent(this,VoiceChatViewActivity.class);
                intent.putExtra("channel",getIntent().getSerializableExtra("channel"));
                intent.putExtra("background",getIntent().getSerializableExtra("background"));
                startActivity(intent);
                finish();
                break;
            case R.id.cv_refuse:
                finish();
                break;
        }
    }
}
