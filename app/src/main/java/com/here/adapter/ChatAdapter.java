package com.here.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.github.library.bubbleview.BubbleImageView;
import com.here.HereApplication;
import com.here.R;
import com.here.bean.User;
import com.here.chat.ChatActivity;
import com.here.photo.PhotoActivity;
import com.here.photo.PhotoPresenter;
import com.here.util.CommentUtil;
import com.here.util.CommonUtils;
import com.here.util.DbUtil;
import com.here.util.DensityUtil;
import com.here.util.NetworkState;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/7/8 16:07
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int TEXT = 001;

    public static final int IMAGE = 002;

    public static final int VOICE = 003;

    public List<BmobIMMessage> getBmobIMMessages() {
        return bmobIMMessages;
    }

    private List<BmobIMMessage> bmobIMMessages;

    private MediaPlayer mediaPlayer;

    private String imageUrl;

    private WeakReference<Activity> context;

    public ChatAdapter(List<BmobIMMessage> bmobIMMessages, WeakReference<Activity> context){
        this.bmobIMMessages=bmobIMMessages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (bmobIMMessages.get(position).getMsgType()){
            case "txt":
                return TEXT;
            case "image":
                return IMAGE;
            case "sound":
                return VOICE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TEXT:
                return new TextMessageHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_text_message,parent,false));
            case IMAGE:
                return new ImageMessageHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_image_message,parent,false));
            case VOICE:
                return new VoiceMessageHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_voice_message,parent,false));
        }
        return new TextMessageHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_message,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextMessageHolder){
            TextMessageHolder tHolder = (TextMessageHolder) holder;
            if (bmobIMMessages.get(position).getFromId().equals(BmobUser
                    .getCurrentUser(User.class).getObjectId())){
                tHolder.rl_text_right.setVisibility(View.VISIBLE);
                tHolder.rl_text_left.setVisibility(View.GONE);
                Glide.with(HereApplication.getContext())
                        .load(BmobUser.getCurrentUser(User.class).getHeadImageUrl())
                        .into(tHolder.cv_text_right_head);
                tHolder.tv_text_right.setText(bmobIMMessages.get(position).getContent());
                if (bmobIMMessages.get(position).getSendStatus()==2){
                    tHolder.pb_text_right.setVisibility(View.GONE);
                    tHolder.iv_text_fail.setVisibility(View.GONE);
                }else if (bmobIMMessages.get(position).getSendStatus() == 1){
                    tHolder.pb_text_right.setVisibility(View.VISIBLE);
                    tHolder.iv_text_fail.setVisibility(View.GONE);
                }else {
                    tHolder.iv_text_fail.setVisibility(View.VISIBLE);
                    tHolder.pb_text_right.setVisibility(View.GONE);
                }
            }else {
                Glide.with(HereApplication.getContext())
                        .load(imageUrl)
                        .into(tHolder.cv_text_left_head);

                tHolder.rl_text_right.setVisibility(View.GONE);
                tHolder.rl_text_left.setVisibility(View.VISIBLE);
                tHolder.tv_text_left.setText(bmobIMMessages.get(position).getContent());
            }
        }else if (holder instanceof ImageMessageHolder){
            final ImageMessageHolder iHolder = (ImageMessageHolder) holder;
            final String url ;
            if (bmobIMMessages.get(position).getContent().indexOf("&") != -1){
                url = bmobIMMessages.get(position).getContent().split("&")[0];
                String cloudAddress = bmobIMMessages.get(position).getContent().split("&")[1];
                DbUtil.getInstance().updateImageAddress(url,cloudAddress);
            }else {
                url = bmobIMMessages.get(position).getContent();
            }
            if (bmobIMMessages.get(position).getFromId().equals(BmobUser
                    .getCurrentUser(User.class).getObjectId())){
                iHolder.rl_image_left.setVisibility(View.GONE);
                iHolder.rl_image_right.setVisibility(View.VISIBLE);
                Glide.with(HereApplication.getContext())
                        .load(BmobUser.getCurrentUser(User.class).getHeadImageUrl())
                        .into(iHolder.cv_image_right);
                int width = 0;
                int height = 0;
                String info = bmobIMMessages.get(position).getExtra();
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    JSONObject json =new JSONObject(String.valueOf(jsonObject.getJSONObject("metaData")));
                    int[] size = CommonUtils.zoomImage(json.getInt("width"),json.getInt("height"));
                    width = size[0];
                    height = size[1];
                    DensityUtil.setViewSize(iHolder.iv_image_right,width,height);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("出错","解析格式出错"+info);
                }

                Glide.with(HereApplication.getContext())
                        .load(url)
                        .asBitmap()
                        .override(width,height)
                        .into(iHolder.iv_image_right);
                if (bmobIMMessages.get(position).getSendStatus() == 2){
                    iHolder.iv_image_fail.setVisibility(View.GONE);
                    iHolder.pb_image.setVisibility(View.GONE);
                }else if(bmobIMMessages.get(position).getSendStatus() == 1){
                    iHolder.iv_image_fail.setVisibility(View.GONE);
                    iHolder.pb_image.setVisibility(View.VISIBLE);
                }else {
                    iHolder.pb_image.setVisibility(View.GONE);
                    iHolder.iv_image_fail.setVisibility(View.VISIBLE);
                }
                iHolder.iv_image_right.setOnClickListener(new MyClickListener(bmobIMMessages.get(position)));
            }else {
                int width = 0;
                int height = 0;
                String info = bmobIMMessages.get(position).getExtra();
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    JSONObject json =new JSONObject(String.valueOf(jsonObject.getJSONObject("metaData")));
                    int[] size = CommonUtils.zoomImage(json.getInt("width"),json.getInt("height"));
                    width = size[0];
                    height = size[1];
                    DensityUtil.setViewSize(iHolder.iv_image_left,width,height);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("出错","解析格式出错"+info);
                }
                iHolder.rl_image_left.setVisibility(View.VISIBLE);
                iHolder.rl_image_right.setVisibility(View.GONE);

                Glide.with(HereApplication.getContext())
                        .load(imageUrl)
                        .into(iHolder.cv_image_left);

                Glide.with(HereApplication.getContext())
                        .load(bmobIMMessages.get(position).getContent())
                        .asBitmap()
                        .override(width,height)
                        .into(iHolder.iv_image_left);

                iHolder.iv_image_left.setOnClickListener(new MyClickListener(bmobIMMessages.get(position)));
            }

        }else if (holder instanceof VoiceMessageHolder){
            VoiceMessageHolder vHolder = (VoiceMessageHolder) holder;
            int duration = 0;
            String info = bmobIMMessages.get(position).getExtra();
            try {
                JSONObject jsonObject = new JSONObject(info);
                JSONObject json =new JSONObject(String.valueOf(jsonObject.getJSONObject("metaData")));
                duration = json.getInt("duration");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("出错","解析格式出错"+info);
            }
            if (bmobIMMessages.get(position).getFromId().equals(BmobUser
                    .getCurrentUser(User.class).getObjectId())){
                vHolder.rl_voice_left.setVisibility(View.GONE);
                vHolder.rl_voice_right.setVisibility(View.VISIBLE);
                Glide.with(HereApplication.getContext())
                        .load(BmobUser.getCurrentUser(User.class).getHeadImageUrl())
                        .into(vHolder.cv_voice_right);
                if (bmobIMMessages.get(position).getSendStatus() == 2 ){
                    vHolder.iv_voice_fail.setVisibility(View.GONE);
                    vHolder.pb_voice.setVisibility(View.GONE);
                }else if (bmobIMMessages.get(position).getSendStatus() == 1){
                    vHolder.pb_voice.setVisibility(View.VISIBLE);
                    vHolder.iv_voice_fail.setVisibility(View.GONE);
                }else {
                    vHolder.pb_voice.setVisibility(View.GONE);
                    vHolder.iv_voice_fail.setVisibility(View.VISIBLE);
                }

                vHolder.tv_voice_right.setText(duration+"\"");
                final int position1 = position;
                vHolder.rl_voice_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer !=null ){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                            mediaPlayer.release();
                        }
                        mediaPlayer = new MediaPlayer();
                        String voice = bmobIMMessages.get(position1).getContent();
                        try {
                            mediaPlayer.setDataSource(voice.indexOf("&")!=-1 ? voice.split("&")[0]:voice);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else {
                final int position1 = position;
                vHolder.rl_voice_left.setVisibility(View.VISIBLE);
                vHolder.rl_voice_right.setVisibility(View.GONE);

                Glide.with(HereApplication.getContext())
                        .load(imageUrl)
                        .into(vHolder.cv_voice_left);

                vHolder.rl_voice_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri= Uri.parse(bmobIMMessages.get(position1).getContent());
                        if (mediaPlayer !=null ){
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                            mediaPlayer.release();
                        }
                        mediaPlayer = new MediaPlayer().create(HereApplication.getContext(),uri);
                        if (NetworkState.netIsActivity){
                            mediaPlayer.start();
                        }
                    }
                });
                vHolder.tv_voice_left.setText(duration+"\"");
            }

        }
    }

    @Override
    public int getItemCount() {
        return bmobIMMessages.size();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    class  TextMessageHolder extends RecyclerView.ViewHolder{
        protected CircleImageView cv_text_left_head;
        protected TextView tv_text_left;
        protected ProgressBar pb_text_right;
        protected CircleImageView cv_text_right_head;
        protected TextView tv_text_right;
        protected RelativeLayout rl_text_left;
        protected RelativeLayout rl_text_right;
        protected ImageView iv_text_fail;

        public TextMessageHolder(View itemView) {
            super(itemView);
            cv_text_left_head = (CircleImageView) itemView.findViewById(R.id.cv_text_left_head);
            tv_text_left = (TextView) itemView.findViewById(R.id.tv_text_left);
            tv_text_right = (TextView) itemView.findViewById(R.id.tv_text_right);
            pb_text_right = (ProgressBar) itemView.findViewById(R.id.pb_text);
            cv_text_right_head = (CircleImageView) itemView.findViewById(R.id.cv_text_head_right);
            rl_text_left = (RelativeLayout) itemView.findViewById(R.id.rl_text_left);
            rl_text_right = (RelativeLayout) itemView.findViewById(R.id.rl_text_right);
            iv_text_fail = (ImageView) itemView.findViewById(R.id.iv_fail);
        }
    }

    class  VoiceMessageHolder extends RecyclerView.ViewHolder{
        protected CircleImageView cv_voice_left;
        protected CircleImageView cv_voice_right;
        protected TextView tv_voice_left;
        protected TextView tv_voice_right;
        protected RelativeLayout rl_voice_left;
        protected RelativeLayout rl_voice_right;
        protected ProgressBar pb_voice;
        protected ImageView iv_voice_fail;
        public VoiceMessageHolder(View itemView) {
            super(itemView);
            cv_voice_left = (CircleImageView) itemView .findViewById(R.id.cv_voice_left);
            cv_voice_right = (CircleImageView) itemView.findViewById(R.id.cv_voice_right);
            tv_voice_left = (TextView) itemView.findViewById(R.id.tv_left_voice);
            tv_voice_right = (TextView) itemView.findViewById(R.id.tv_right_voice);
            rl_voice_left = (RelativeLayout) itemView.findViewById(R.id.rl_left_voice);
            rl_voice_right = (RelativeLayout) itemView.findViewById(R.id.rl_right_voice);
            pb_voice = (ProgressBar) itemView.findViewById(R.id.pb_voice);
            iv_voice_fail = (ImageView) itemView.findViewById(R.id.iv_voice_fail);
        }
    }

    class  ImageMessageHolder extends RecyclerView.ViewHolder {
        protected CircleImageView cv_image_left;
        protected CircleImageView cv_image_right;
        protected BubbleImageView iv_image_left;
        protected BubbleImageView iv_image_right;
        protected RelativeLayout rl_image_left;
        protected RelativeLayout rl_image_right;
        protected ImageView iv_image_fail;
        protected ProgressBar pb_image;
        public ImageMessageHolder(View itemView) {
            super(itemView);
            cv_image_left = (CircleImageView) itemView.findViewById(R.id.cv_image_left);
            cv_image_right = (CircleImageView) itemView.findViewById(R.id.cv_image_right);
            iv_image_left = (BubbleImageView) itemView.findViewById(R.id.iv_image_left);
            iv_image_right = (BubbleImageView) itemView.findViewById(R.id.iv_image_right);
            rl_image_left = (RelativeLayout) itemView.findViewById(R.id.rl_image_left);
            rl_image_right = (RelativeLayout) itemView.findViewById(R.id.rl_image_right);
            iv_image_fail = (ImageView) itemView.findViewById(R.id.iv_image_fail);
            pb_image = (ProgressBar) itemView.findViewById(R.id.pb_image);
        }
    }

    public void addNewMessage(RecyclerView recyclerView,BmobIMMessage bmobIMMessage){
        bmobIMMessages.add(bmobIMMessage);
        notifyItemInserted(bmobIMMessages.size()-1);
        recyclerView.scrollToPosition(bmobIMMessages.size()-1);
    }

    public void sendMessageSuccess(int position){
        notifyItemChanged(position);
    }

    public void insert(List<BmobIMMessage> data){
        if (data != null && data.size()>0){
            for (BmobIMMessage bmobIMMessage : data) {
                int position = data.indexOf(bmobIMMessage);
                bmobIMMessages.add(position,bmobIMMessage);
                notifyItemInserted(position);
            }
        }
    }
    public class MyClickListener implements View.OnClickListener {
        private BmobIMMessage message;


        public MyClickListener(BmobIMMessage message){
            this.message = message;

        }

        @Override
        public void onClick(View v) {
            Pair<View, String> p = new Pair<View, String>(v, "image");//haderIv是头像控件
            Intent intent = new Intent(context.get(), PhotoActivity.class);
            ArrayList<String> images = new ArrayList<>();
            int finalPosition = 0;
            for (BmobIMMessage bmobIMMessage : bmobIMMessages) {
                if (bmobIMMessage.getMsgType().equals("image")){
                    if (bmobIMMessage.getContent().indexOf("&") != -1){
                        images.add(bmobIMMessage.getContent().split("&")[0]);
                    }else {
                        images.add(bmobIMMessage.getContent());
                    }

                    if (bmobIMMessage.getId() == message.getId()){
                        finalPosition = images.size()-1;
                    }
                }
            }
            intent.putStringArrayListExtra("images", images);
            intent.putExtra("position",finalPosition);
            context.get().startActivity(intent, ActivityOptions
                    .makeSceneTransitionAnimation(context.get(), p).toBundle());

        }
    }
}
