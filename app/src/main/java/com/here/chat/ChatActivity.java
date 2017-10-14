package com.here.chat;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.here.R;
import com.here.adapter.ChatAdapter;
import com.here.adapter.EmotionAdapter;
import com.here.adapter.ImageAdapter;
import com.here.base.MvpActivity;
import com.here.bean.User;

import com.here.util.ImUtil;
import com.here.util.PhotoController;
import com.here.view.ChatImageCallback;
import com.here.view.OnChatItemTouchListener;
import com.here.view.OnRecyclerItemClickListener;
import com.here.voice.VoiceChatViewActivity;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.v3.BmobUser;


public class ChatActivity extends MvpActivity<ChatPresenter> implements ChatContract, View.OnLayoutChangeListener {

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
    @Bind(R.id.tv_chat_notice)
    TextView tvChatNotice;
    @Bind(R.id.ll_chat)
    LinearLayout llChat;
    @Bind(R.id.rv_chat_more)
    RecyclerView rvChatMore;
    @Bind(R.id.iv_emoji)
    ImageView ivEmoji;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.fl_chat_more)
    FrameLayout flChatMore;
    private ChatAdapter chatAdapter;
    private Drawable[] drawable_Anims;
    BmobRecordManager recordManager;
    public static final int REQUEST_CODE_IMAGE = 101;
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private boolean isKeyOpen = false;
    private final PhotoController photoController = new PhotoController();

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
        c.setUnreadCount(0);
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        keyHeight = screenHeight / 3;
        editMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flChatMore.getVisibility() == View.VISIBLE){
                    hideMore();
                }
            }
        });

        editMsg.requestFocus();
        final ChatImageCallback callback = new ChatImageCallback();
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvChatMore);
        callback.setListener(new ChatImageCallback.OnSwipeImageListener() {
            @Override
            public void onSendImage(String url) {
                mvpPresenter.sendImageMessage(url);
            }
        });
        rvChatMore.addOnItemTouchListener(new OnChatItemTouchListener(rvChatMore) {
            @Override
            public void onDownRecyclerView(RecyclerView.ViewHolder vh) {
                if (!isEmotion){
                    itemTouchHelper.startDrag(vh);
                }
            }
        });
    }

    private void initEmotion() {
        isEmotion = true;
        flChatMore.setClipChildren(true);
        flChatMore.setClipToPadding(true);
        rvChatMore.setLayoutManager(new GridLayoutManager(this,7));
        rvChatMore.setItemAnimator(new DefaultItemAnimator());
        rvChatMore.setAdapter(new EmotionAdapter(ImUtil.getEmotions(this))
                .setListener(new EmotionAdapter.OnEmotionListener() {
            @Override
            public void onClick(int position) {
                Bitmap bitmap;
                int drawable = getResources().getIdentifier("emoji_"+(position
                        < 10 ? "0"+position:position),"drawable",getPackageName());
                bitmap = BitmapFactory.decodeResource(getResources(),drawable);
                ImageSpan imageSpan = new ImageSpan(ChatActivity.this, bitmap);
                SpannableString spannableString=new SpannableString("emoji_"
                        +(position < 10 ? "0"+position:position));
                spannableString.setSpan(imageSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editMsg.append(spannableString);
            }
        }));
        flChatMore.setVisibility(View.VISIBLE);
        ivEmoji.setBackgroundColor(Color.parseColor("#cfcfcf"));
        ivImage.setBackgroundColor(Color.parseColor("#f1f1f1"));
    }

    boolean isEmotion = false;
    private void initImage(){
        isEmotion = false;
        flChatMore.setClipChildren(false);
        flChatMore.setClipToPadding(false);
        ivEmoji.setBackgroundColor(Color.parseColor("#f1f1f1"));
        ivImage.setBackgroundColor(Color.parseColor("#cfcfcf"));
        photoController.onCreate(this,rvChatMore);
        photoController.loadAllPhoto(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        llChat.addOnLayoutChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.chat_call && c != null) {
            mvpPresenter.sendVoiceRequest(c);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initVoice() {
        btnSpeak.setOnTouchListener(new VoiceTouchListener());
        drawable_Anims = new Drawable[]{
                getResources().getDrawable(R.mipmap.chat_icon_voice2),
                getResources().getDrawable(R.mipmap.chat_icon_voice3),
                getResources().getDrawable(R.mipmap.chat_icon_voice4),
                getResources().getDrawable(R.mipmap.chat_icon_voice5),
                getResources().getDrawable(R.mipmap.chat_icon_voice6)};
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
                    }, 200);
                }
            }
        });
    }
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            if (chatAdapter.getItemCount() > 0) {
                rcView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
            isKeyOpen = true;
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            isKeyOpen = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    getVoice();
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
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
                                mvpPresenter.sendVoiceMessage(recordManager
                                        .getRecordFilePath(c.getConversationId()), recordTime);
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
        c = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getBundle().getSerializable("c"));
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
                if (TextUtils.isEmpty(editMsg.getText().toString())) {
                    btnChatVoice.setVisibility(View.VISIBLE);
                    btnChatSend.setVisibility(View.GONE);
                } else {
                    btnChatSend.setVisibility(View.VISIBLE);
                    btnChatVoice.setVisibility(View.GONE);
                }
            }
        });
        mvpPresenter.queryMessageRecord(c, null);
        swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvpPresenter.queryMessageRecord(c, chatAdapter.getBmobIMMessages().get(0));
                swRefresh.setRefreshing(false);
            }
        });
        rcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < -2 && isKeyOpen) {
                    hideSoftInputView();
                }
            }
        });

    }


    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else {
            return null;
        }

    }

    private void hideMore(){
        flChatMore.setVisibility(View.GONE);
        rvChatMore.removeAllViews();
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

    @OnClick({R.id.btn_chat_add, R.id.btn_speak, R.id.btn_chat_voice, R.id.btn_chat_keyboard, R.id.btn_chat_send,R.id.iv_emoji, R.id.iv_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chat_add:
//                sendImageMessage();
                if (flChatMore.getVisibility() == View.VISIBLE){
                    hideMore();
                }else {
                    if (isKeyOpen){
                        hideSoftInputView();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initEmotion();
                        }
                    }, 50);
                }

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
                editMsg.requestFocus();
                break;
            case R.id.btn_chat_send:
                mvpPresenter.sendTextMessage();
                break;
            case R.id.iv_emoji:
                if (!isEmotion){
                    initEmotion();
                }
                break;
            case R.id.iv_image:
                if (isEmotion){
                    initImage();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getFromUserInfo().getUserId().equals(c.getConversationId())) {
            chatAdapter.addNewMessage(rcView, event.getMessage());
            event.getConversation().setUnreadCount(0);
        } else {
            tvChatNotice.setVisibility(View.VISIBLE);
            tvChatNotice.setText(event.getFromUserInfo().getName()
                    + "：" + event.getMessage().getContent());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvChatNotice.setVisibility(View.GONE);
                }
            }, 5000);
        }
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
        chatAdapter.addNewMessage(rcView, bmobIMMessage);
        return chatAdapter.getItemCount() - 1;
    }

    @Override
    public void sendVoiceMessage() {

    }

    @Override
    public void sendImageMessage() {
        if (getCcamra()) {
            SImagePicker
                    .from(ChatActivity.this)
                    .maxCount(1)
                    .rowCount(3)
                    .showCamera(true)
                    .pickMode(SImagePicker.MODE_IMAGE)
                    .forResult(REQUEST_CODE_IMAGE);
        }
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
    public void loadMessageRecord(List<BmobIMMessage> messages) {
        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(messages, new WeakReference<Activity>(ChatActivity.this));
            chatAdapter.setImageUrl(c.getConversationIcon());
            rcView.setLayoutManager(new LinearLayoutManager(this));
            rcView.setItemAnimator(new DefaultItemAnimator());
            rcView.setAdapter(chatAdapter);
            if (messages.size() > 0) {
                rcView.scrollToPosition(messages.size() - 1);
            }
        } else {
            if (messages.size() > 0) {
                chatAdapter.insert(messages);
            }
        }
        swRefresh.setEnabled(messages.size() == 10);
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

    @Override
    public void startVoiceChat() {
        User user = BmobUser.getCurrentUser(User.class);
        Intent intent = new Intent(this, VoiceChatViewActivity.class);
        intent.putExtra("channel", user.getObjectId());
        intent.putExtra("background", c.getConversationIcon());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Toast.makeText(mActivity, "你拒绝了权限无法应用发语音功能", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
