package com.here.chat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.here.R;
import com.here.adapter.ChatAdapter;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.immediate.NewImmediateActivity;
import com.here.util.ImUtil;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends MvpActivity<ChatPresenter> implements ChatContract {

    @Bind(R.id.tv_chat_name)
    TextView tvChatName;
    @Bind(R.id.rc_view)
    RecyclerView rcView;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout swRefresh;
    @Bind(R.id.iv_record)
    ImageView ivRecord;
    @Bind(R.id.tv_voice_tips)
    TextView tvVoiceTips;
    @Bind(R.id.layout_record)
    RelativeLayout layoutRecord;
    @Bind(R.id.btn_chat_add)
    Button btnChatAdd;
    @Bind(R.id.btn_chat_emo)
    Button btnChatEmo;
    @Bind(R.id.edit_msg)
    EditText editMsg;
    @Bind(R.id.btn_speak)
    Button btnSpeak;
    @Bind(R.id.btn_chat_voice)
    Button btnChatVoice;
    @Bind(R.id.btn_chat_keyboard)
    Button btnChatKeyboard;
    @Bind(R.id.btn_chat_send)
    Button btnChatSend;
    BmobIMConversation c;
    private ChatAdapter chatAdapter;
    private Drawable[] drawable_Anims;
    BmobRecordManager recordManager;
    public static final int REQUEST_CODE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_chat);
        initHome();
        initChat();
        initVoice();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat,menu);
        return true;
    }

    private void initVoice() {
        btnSpeak.setOnTouchListener(new VoiceTouchListener());
        drawable_Anims = new Drawable[] {
                getResources().getDrawable(R.mipmap.chat_icon_voice2),
                getResources().getDrawable(R.mipmap.chat_icon_voice3),
                getResources().getDrawable(R.mipmap.chat_icon_voice4),
                getResources().getDrawable(R.mipmap.chat_icon_voice5),
                getResources().getDrawable(R.mipmap.chat_icon_voice6) };
        recordManager = BmobRecordManager.getInstance(this);
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {
            @Override
            public void onVolumnChanged(int value) {
                ivRecord.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {
                    btnSpeak.setPressed(false);
                    btnSpeak.setClickable(false);
                    layoutRecord.setVisibility(View.INVISIBLE);
                    mvpPresenter.sendVoiceMessage(localPath, recordTime);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            btnSpeak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!android.os.Environment.getExternalStorageState().equals(
                            android.os.Environment.MEDIA_MOUNTED)) {
                        toastShow("发送语音需要sdcard支持！");
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layoutRecord.setVisibility(View.VISIBLE);
                        tvVoiceTips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(c.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tvVoiceTips.setText(getString(R.string.voice_cancel_tips));
                        tvVoiceTips.setTextColor(Color.RED);
                    } else {
                        tvVoiceTips.setText(getString(R.string.voice_up_tips));
                        tvVoiceTips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layoutRecord.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {
                            recordManager.cancelRecording();
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                mvpPresenter.sendVoiceMessage(recordManager.getRecordFilePath(c.getConversationId()),recordTime);
                            } else {
                                layoutRecord.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    Toast toast;

    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }



    private void initChat() {
        chatAdapter = new ChatAdapter(new ArrayList<BmobIMMessage>());
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setItemAnimator(new DefaultItemAnimator());
        rcView.setAdapter(chatAdapter);
        c= BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getBundle().getSerializable("c"));
        tvChatName.setText(c.getConversationTitle());
        editMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(editMsg.getText().toString())){
                    btnChatVoice.setVisibility(View.VISIBLE);
                    btnChatSend.setVisibility(View.GONE);
                }else {
                    btnChatSend.setVisibility(View.VISIBLE);
                    btnChatVoice.setVisibility(View.GONE);
                }
            }
        });
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else{
            return null;
        }

    }



    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @OnClick({R.id.btn_chat_add, R.id.btn_speak, R.id.btn_chat_voice, R.id.btn_chat_keyboard, R.id.btn_chat_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chat_add:
                sendImageMessage();
                break;
            case R.id.btn_speak:
                break;
            case R.id.btn_chat_voice:
                editMsg.setVisibility(View.GONE);
                btnChatVoice.setVisibility(View.GONE);
                btnChatKeyboard.setVisibility(View.VISIBLE);
                btnSpeak.setVisibility(View.VISIBLE);
                hideSoftInputView();

                break;
            case R.id.btn_chat_keyboard:
                editMsg.setVisibility(View.VISIBLE);
                btnSpeak.setVisibility(View.GONE);
                btnChatVoice.setVisibility(View.VISIBLE);
                btnChatKeyboard.setVisibility(View.GONE);
                break;
            case R.id.btn_chat_send:
                mvpPresenter.sendTextMessage();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BmobIMMessage event) {
        chatAdapter.addNewMessage(rcView,event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            mvpPresenter.sendImageMessage(pathList.get(0));
        }
    }

    @Override
    public void loadingRecord(List<BmobIMMessage> bmobIMMessages) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public int sendTextMessage(BmobIMMessage bmobIMMessage) {
        chatAdapter.addNewMessage(rcView,bmobIMMessage);
        return chatAdapter.getItemCount()-1;
    }

    @Override
    public void sendVoiceMessage() {

    }

    @Override
    public void sendImageMessage() {
        SImagePicker
                .from(ChatActivity.this)
                .maxCount(1)
                .rowCount(3)
                .showCamera(true)
                .pickMode(SImagePicker.MODE_IMAGE)
                .forResult(REQUEST_CODE_IMAGE);
    }

    @Override
    public void sendEmoji() {
    }

    @Override
    public void sendFail(String error) {
        toastShow(error);
    }

    @Override
    public void sending(int value) {

    }

    @Override
    public void sendSuccess(int position) {
        chatAdapter.sendMessageSuccess(position);
    }

    @Override
    public String getMessage() {
        return editMsg.getText().toString();
    }

    @Override
    public BmobIMConversation getConversation() {
        return c;
    }

    @Override
    public void cleanInput() {
        editMsg.setText("");
    }
}
