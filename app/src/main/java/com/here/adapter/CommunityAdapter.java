package com.here.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.here.HereApplication;
import com.here.R;
import com.here.bean.Appointment;
import com.here.bean.Community;
import com.here.bean.Like;
import com.here.bean.LikeId;
import com.here.bean.Mood;
import com.here.bean.Propaganda;
import com.here.bean.User;
import com.here.community.details.CommunityDetailsActivity;
import com.here.details.PostDetailsActivity;
import com.here.personal.PersonalActivity;
import com.here.personal.other.OtherInfoActivity;
import com.here.photo.PhotoActivity;
import com.here.photo.PhotoPresenter;
import com.here.util.CommonUtils;
import com.here.util.DensityUtil;
import com.here.util.LikeUtil;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/7/13 10:21
 */

public class CommunityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Activity> context;

    private static final String APP_ID = "1106163416";

    private List<String> postIds;

    private int[] colors;

    private boolean isLikeing = false;

    private boolean isShare = true;

    private Tencent mTencent;

    public CommunityAdapter(List<Community> communities,Activity context) {
        this.communities = communities;
        this.context = new WeakReference<Activity>(context);
        colors = HereApplication.getContext().getResources().getIntArray(R.array.tips_bg);
        refresh();
        mTencent = Tencent.createInstance(APP_ID,HereApplication.getContext());
    }

    public void refresh(){
        postIds = new ArrayList<>();
        List<LikeId> likes = DataSupport.findAll(LikeId.class);
        String userId = BmobUser.getCurrentUser(User.class).getObjectId();
        for (LikeId like : likes) {
            if (like.getUserId().equals(userId)){
                postIds.add(like.getPublishId());
            }
        }

    }

    public void restore(){
        while (communities.size() > 3){
            int i = communities.size() - 1;
            communities.remove(i);
            notifyItemRemoved(i);
        }
    }

    private List<Community> communities;

    private boolean isLoading = false;

    public void setData(List<Community> communityList){
        communityList.add(0,communities.get(0));
        communityList.add(1,communities.get(1));
        communityList.add(2,communities.get(2));
        communities = null;
        communities = communityList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (communities.get(position).getType() == Community.TYPE_VIEW_PAGE) {
            return Community.TYPE_VIEW_PAGE;
        } else if (communities.get(position).getType() == Community.TYPE_COMMUNITY) {
            return Community.TYPE_COMMUNITY;
        } else if (communities.get(position).getType() == Community.TYPE_APPOINTMENT) {
            return Community.TYPE_APPOINTMENT;
        } else if (communities.get(position).getType() == Community.TYPE_SHARE) {
            return Community.TYPE_SHARE;
        } else if (communities.get(position).getType() == Community.TYPE_TIPS) {
            return Community.TYPE_TIPS;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Community.TYPE_VIEW_PAGE) {
            return new ViewPageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_page, parent, false));
        } else if (viewType == Community.TYPE_COMMUNITY) {
            return new CommunityHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community, parent, false));
        } else if (viewType == Community.TYPE_APPOINTMENT) {
            return new AppointmentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false));
        } else if (viewType == Community.TYPE_SHARE) {
            return new ShareHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false));
        } else if (viewType == Community.TYPE_TIPS) {
            return new TipsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_tip, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewPageHolder && !isLoading) {
            isLoading = true;
            ViewPageHolder vHolder = (ViewPageHolder) holder;
            vHolder.load(Community.getPropagandas());
        }else if (holder instanceof CommunityHolder){
            CommunityHolder cHolder = (CommunityHolder) holder;
            cHolder.load();
        }else if (holder instanceof ShareHolder){
            ShareHolder shareHolder = (ShareHolder) holder;
            shareHolder.load(communities.get(position).getMood(),position);
        }else if (holder instanceof AppointmentHolder){
            AppointmentHolder appointmentHolder = (AppointmentHolder) holder;
            appointmentHolder.load(communities.get(position).getAppointment(),position);
        }else if (holder instanceof TipsHolder){
            TipsHolder tipsHolder = (TipsHolder) holder;
            tipsHolder.load();
        }
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }

    public void addData(List<Community> communities) {
        for (Community community : communities) {
            this.communities.add(community);
            notifyItemInserted(this.communities.size()-1);
        }
    }

    public OnSwitchChangeListener getListener() {
        return listener;
    }

    public void setListener(OnSwitchChangeListener listener) {
        this.listener = listener;
    }


    class ViewPageHolder extends RecyclerView.ViewHolder {
        protected SliderLayout sliderLayout;

        public ViewPageHolder(View itemView) {
            super(itemView);
            sliderLayout = (SliderLayout) itemView.findViewById(R.id.slider);
            sliderLayout.setDuration(5000);
        }

        public void load(Propaganda[] propagandas) {
            for (Propaganda propaganda : propagandas) {
                TextSliderView mTextSliderView = new TextSliderView(HereApplication.getContext());
                mTextSliderView.description(propaganda.getDescribe())
                        .image(propaganda.getImage())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);
                sliderLayout.addSlider(mTextSliderView);
            }

        }
    }

    class CommunityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RelativeLayout rlItemSport;
        RelativeLayout rlItemShopping;
        RelativeLayout rlItemSing;
        RelativeLayout rlItemMovie;
        RelativeLayout rlItemCamping;
        RelativeLayout rlItemBar;
        RelativeLayout rlItemBeauty;
        RelativeLayout rlItemDelicious;
        RelativeLayout rlItemChess;
        RelativeLayout rlItemGame;
        RelativeLayout rlItemRole;
        RelativeLayout rlItemOther;
        public CommunityHolder(View itemView) {
            super(itemView);
            rlItemSport = (RelativeLayout) itemView.findViewById(R.id.rl_item_sport);
            rlItemShopping = (RelativeLayout) itemView.findViewById(R.id.rl_item_shopping);
            rlItemSing = (RelativeLayout) itemView.findViewById(R.id.rl_item_sing);
            rlItemMovie = (RelativeLayout) itemView.findViewById(R.id.rl_item_movie);
            rlItemCamping = (RelativeLayout) itemView.findViewById(R.id.rl_item_camping);
            rlItemBar = (RelativeLayout) itemView.findViewById(R.id.rl_item_bar);
            rlItemBeauty = (RelativeLayout) itemView.findViewById(R.id.rl_item_beauty);
            rlItemDelicious = (RelativeLayout) itemView.findViewById(R.id.rl_item_delicious);
            rlItemChess = (RelativeLayout) itemView.findViewById(R.id.rl_item_chess);
            rlItemGame = (RelativeLayout) itemView.findViewById(R.id.rl_item_game);
            rlItemRole = (RelativeLayout) itemView.findViewById(R.id.rl_item_role);
            rlItemOther = (RelativeLayout) itemView.findViewById(R.id.rl_item_other);
        }

        public void load(){
            rlItemSport.setOnClickListener(this);
            rlItemShopping.setOnClickListener(this);
            rlItemSing.setOnClickListener(this);
            rlItemMovie.setOnClickListener(this);
            rlItemCamping.setOnClickListener(this);
            rlItemBar.setOnClickListener(this);
            rlItemBeauty.setOnClickListener(this);
            rlItemDelicious.setOnClickListener(this);
            rlItemChess.setOnClickListener(this);
            rlItemGame.setOnClickListener(this);
            rlItemRole.setOnClickListener(this);
            rlItemOther.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context.get() , CommunityDetailsActivity.class);
            switch (v.getId()){
                case R.id.rl_item_sport:
                    intent.putExtra("kind","运动");
                    break;
                case R.id.rl_item_shopping:
                    intent.putExtra("kind","逛街");
                    break;
                case R.id.rl_item_sing:
                    intent.putExtra("kind","唱歌");
                    break;
                case R.id.rl_item_movie:
                    intent.putExtra("kind","电影");
                    break;
                case R.id.rl_item_camping:
                    intent.putExtra("kind","户外");
                    break;
                case R.id.rl_item_bar:
                    intent.putExtra("kind","酒吧");
                    break;
                case R.id.rl_item_beauty:
                    intent.putExtra("kind","美容");
                    break;
                case R.id.rl_item_delicious:
                    intent.putExtra("kind","美食");
                    break;
                case R.id.rl_item_chess:
                    intent.putExtra("kind","棋牌");
                    break;
                case R.id.rl_item_game:
                    intent.putExtra("kind","游戏");
                    break;
                case R.id.rl_item_role:
                    intent.putExtra("kind","桌游");
                    break;
                case R.id.rl_item_other:
                    intent.putExtra("kind","其他");
                    break;
            }
            context.get().startActivity(intent);
        }
    }

    class AppointmentHolder extends RecyclerView.ViewHolder{
        CircleImageView cvAppointmentHead;
        TextView tvAppointmentNickname;
        TextView tvAppointmentTime;
        TextView tvAppointmentKind;
        CardView cvAppointmentTips;
        TextView tvAppointmentAddress;
        TextView tvAppointmentInterval;
        TextView tvAppointmentTitle;
        TextView tvAppointmentNumber;
        TextView tvAppointmentContent;
        ImageView ivAppointmentImage1;
        ImageView ivAppointmentImage2;
        ImageView ivAppointmentImage3;
        TextView tvAppointmentPicCount;
        ShineButton sbAppointmentLike;
        LinearLayout ll_appointment_images;
        RelativeLayout rl_appointment_comment;
        RelativeLayout rl_share_appointment;
        public AppointmentHolder(View itemView) {
            super(itemView);
            cvAppointmentHead = (CircleImageView) itemView.findViewById(R.id.cv_appointment_head);
            tvAppointmentNickname = (TextView) itemView.findViewById(R.id.tv_appointment_nickname);
            tvAppointmentTime = (TextView) itemView.findViewById(R.id.tv_appointment_time);
            tvAppointmentKind = (TextView) itemView.findViewById(R.id.tv_appointment_kind);
            cvAppointmentTips = (CardView) itemView.findViewById(R.id.cv_appointment_tips);
            tvAppointmentAddress = (TextView) itemView.findViewById(R.id.tv_appointment_address);
            tvAppointmentInterval = (TextView) itemView.findViewById(R.id.tv_appointment_interval);
            tvAppointmentTitle = (TextView) itemView.findViewById(R.id.tv_appointment_title);
            tvAppointmentNumber = (TextView) itemView.findViewById(R.id.tv_appointment_number );
            tvAppointmentContent = (TextView) itemView.findViewById(R.id.tv_appointment_content);
            ivAppointmentImage1 = (ImageView) itemView.findViewById(R.id.iv_appointment_image1);
            ivAppointmentImage2 = (ImageView) itemView.findViewById(R.id.iv_appointment_image2);
            ivAppointmentImage3 = (ImageView) itemView.findViewById(R.id.iv_appointment_image3);
            tvAppointmentPicCount = (TextView) itemView.findViewById(R.id.tv_appointment_pic_count);
            sbAppointmentLike = (ShineButton) itemView.findViewById(R.id.sb_appointment_like);
            ll_appointment_images = (LinearLayout) itemView.findViewById(R.id.ll_appointment_images);
            rl_appointment_comment = (RelativeLayout) itemView.findViewById(R.id.rl_appointment_comment);
            rl_share_appointment = (RelativeLayout) itemView.findViewById(R.id.rl_share_appointment);
        }

        public void load(final Appointment appointment, final int position) {
            Glide.with(HereApplication.getContext())
                    .load(appointment.getPublisher().getHeadImageUrl())
                    .into(cvAppointmentHead);
            cvAppointmentHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appointment.getPublisher().getObjectId()
                            .equals(BmobUser.getCurrentUser().getObjectId())){
                        Intent intent = new Intent(context.get(), PersonalActivity.class);
                        context.get().startActivity(intent);
                    }else {
                        Intent intent = new Intent(context.get(), OtherInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("other",appointment.getPublisher());
                        intent.putExtras(bundle);
                        context.get().startActivity(intent);
                    }

                }
            });
            tvAppointmentNickname.setText(appointment.getPublisher().getNickname());
            tvAppointmentTime.setText(appointment.getPublishDate());
            tvAppointmentKind.setText(appointment.getKind());
            tvAppointmentAddress.setText(appointment.getAddress());
            tvAppointmentInterval.setText(appointment.getStartDate()+"~"+appointment.getOverDate());
            tvAppointmentTitle.setText(appointment.getTitle());
            tvAppointmentNumber.setText("参与人数："+appointment.getJoinNumber());
            tvAppointmentContent.setText(appointment.getDescribe());
            cvAppointmentTips.setCardBackgroundColor(colors[new Random().nextInt(23)]);
            if (appointment.getImages() != null && appointment.getImages().length > 0) {
                ll_appointment_images.setVisibility(View.VISIBLE);
                if (appointment.getImages().length == 1) {
                    ivAppointmentImage1.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[0])
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    int[] size = CommonUtils.zoomCommunityImage(resource
                                            .getWidth(),resource.getHeight());
                                    DensityUtil.setViewSize(ivAppointmentImage1,size[0],size[1]);
                                    Glide.with(HereApplication.getContext())
                                            .load(appointment.getImages()[0])
                                            .override(size[0],size[1])
                                            .into(ivAppointmentImage1);
                                }
                            });
                    ivAppointmentImage1.setOnClickListener(new MyClickListener(appointment.getImages(),0));
                    ivAppointmentImage2.setVisibility(View.GONE);
                    ivAppointmentImage3.setVisibility(View.GONE);
                    tvAppointmentPicCount.setVisibility(View.GONE);

                } else if (appointment.getImages().length == 2) {
                    ivAppointmentImage1.setOnClickListener(new MyClickListener(appointment.getImages(),0));
                    ivAppointmentImage2.setOnClickListener(new MyClickListener(appointment.getImages(),1));
                    DensityUtil.setViewSize(ivAppointmentImage1,DensityUtil
                            .dip2px(110),DensityUtil.dip2px(110));
                    ivAppointmentImage1.setVisibility(View.VISIBLE);
                    ivAppointmentImage2.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[0])
                            .into(ivAppointmentImage1);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[1])
                            .into(ivAppointmentImage2);
                    ivAppointmentImage3.setVisibility(View.GONE);
                    tvAppointmentPicCount.setVisibility(View.GONE);
                } else if (appointment.getImages().length == 3) {
                    ivAppointmentImage1.setOnClickListener(new MyClickListener(appointment.getImages(),0));
                    ivAppointmentImage2.setOnClickListener(new MyClickListener(appointment.getImages(),1));
                    ivAppointmentImage3.setOnClickListener(new MyClickListener(appointment.getImages(),2));
                    DensityUtil.setViewSize(ivAppointmentImage1,DensityUtil
                            .dip2px(110),DensityUtil.dip2px(110));
                    ivAppointmentImage1.setVisibility(View.VISIBLE);
                    ivAppointmentImage2.setVisibility(View.VISIBLE);
                    ivAppointmentImage3.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[0])
                            .into(ivAppointmentImage1);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[1])
                            .into(ivAppointmentImage2);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[2])
                            .into(ivAppointmentImage3);
                    tvAppointmentPicCount.setVisibility(View.GONE);
                } else {
                    ivAppointmentImage1.setOnClickListener(new MyClickListener(
                            appointment.getImages(),0));
                    ivAppointmentImage2.setOnClickListener(new MyClickListener(
                            appointment.getImages(),1));
                    ivAppointmentImage3.setOnClickListener(new MyClickListener(
                            appointment.getImages(),2));
                    DensityUtil.setViewSize(ivAppointmentImage1,DensityUtil
                            .dip2px(110),DensityUtil.dip2px(110));
                    ivAppointmentImage1.setVisibility(View.VISIBLE);
                    ivAppointmentImage2.setVisibility(View.VISIBLE);
                    ivAppointmentImage3.setVisibility(View.VISIBLE);
                    tvAppointmentPicCount.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[0])
                            .into(ivAppointmentImage1);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[1])
                            .into(ivAppointmentImage2);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[2])
                            .into(ivAppointmentImage3);
                    tvAppointmentPicCount.setText("+" + (appointment.getImages().length - 3));
                }

            } else {
                DensityUtil.setViewSize(ivAppointmentImage1,DensityUtil
                        .dip2px(110),DensityUtil.dip2px(110));
                ivAppointmentImage1.setVisibility(View.GONE);
                ivAppointmentImage2.setVisibility(View.GONE);
                ivAppointmentImage3.setVisibility(View.GONE);
                tvAppointmentPicCount.setVisibility(View.GONE);
                ll_appointment_images.setVisibility(View.GONE);
            }

            if (postIds.contains(appointment.getObjectId())) {
                sbAppointmentLike.setChecked(true);
            }else {
                sbAppointmentLike.setChecked(false);
            }

            sbAppointmentLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLikeing) {
                        return;
                    }
                    isLikeing = true;
                    if (postIds.contains(appointment.getObjectId())) {
                        final Like like = new Like();
                        like.setPublish(appointment.getObjectId());
                        like.setUser(BmobUser.getCurrentUser(User.class));
                        LikeUtil.deletePost(like, new LikeUtil.OnLikeListener() {
                            @Override
                            public void success(String s) {
                                isLikeing = false;
                                refresh();
                            }
                            @Override
                            public void fail(String error) {
                                isLikeing = false;
                                sbAppointmentLike.setChecked(true);
                                Toast.makeText(context.get(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        LikeUtil.likePost(BmobUser.getCurrentUser(User.class), appointment
                                .getObjectId(), new LikeUtil.OnLikeListener() {
                            @Override
                            public void success(String s) {
                                isLikeing = false;
                                final LikeId likeId = new LikeId();
                                likeId.setPublishId(appointment.getObjectId());
                                likeId.setUserId(BmobUser.getCurrentUser(User.class).getObjectId());
                                likeId.setLikeId(s);
                                Log.i("储存","是否成功"+likeId.save());
                                refresh();
                            }
                            @Override
                            public void fail(String error) {
                                isLikeing = false;
                                sbAppointmentLike.setChecked(false);
                                Toast.makeText(context.get(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            rl_appointment_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.get(),PostDetailsActivity.class);
                    intent.putExtra("type","appointment");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("appointment",appointment);
                    intent.putExtras(bundle);
                    context.get().startActivity(intent);
                }
            });

            rl_share_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertView("分享", null, "取消", new String[]{"分享到QQ好友"}, new String[]{"分享到QQ空间"}
                            , context.get(), AlertView.Style.ActionSheet, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            final Bundle params = new Bundle();
                            if (position == 0) {
                                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                                        QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                                params.putString(QQShare.SHARE_TO_QQ_TITLE, appointment.getTitle());
                                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, appointment.getDescribe());
                                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
                                        "https://github.com/old-traveler/Here/blob/master/app/app-release.apk");
                                if (appointment.getImages()!= null && appointment.getImages().length>0){
                                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                                            appointment.getImages()[0]);
                                }else {
                                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                                            "https://avatars1.githubusercontent.com/u/22116148?v=4&u=48ec6f70dc" +
                                                    "e731dcc8f3cbf66231f5ca651e9953&s=40");
                                }
                                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "来here一起玩吧");
                                params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "与附近的人一起活动吧");
                                mTencent.shareToQQ(context.get(), params, new BaseUiListener1());
                            } else if (position == 1) {
                                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                                params.putString(QzoneShare.SHARE_TO_QQ_TITLE, appointment.getTitle());
                                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, appointment.getDescribe());
                                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,"https://github.com/old-travele" +
                                        "r/Here/blob/master/app/app-release.apk");
                                ArrayList<String> imgUrlList = new ArrayList<>();
                                if (appointment.getImages()!= null && appointment.getImages().length>0){
                                    for (String s : appointment.getImages()) {
                                        imgUrlList.add(s);
                                    }
                                }else {
                                    imgUrlList.add( "https://avatars1.githubusercontent.com/u/22116148?v=4&u=48ec6f7" +
                                            "0dce731dcc8f3cbf66231f5ca651e9953&s=40");
                                }
                                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,imgUrlList);
                                mTencent.shareToQzone(context.get(), params, new BaseUiListener1());
                            }else {
                                return;
                            }
                        }
                    }).show();
                }
            });
        }

    }

    class ShareHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_share_images;
        CircleImageView cvMoodHead;
        TextView tvMoodNickname;
        TextView tvMoodTime;
        TextView tvMoodTips;
        CardView cvMoodTips;
        TextView tvMoodTitle;
        TextView tvMoodContent;
        ImageView ivMoodImage1;
        ImageView ivMoodImage2;
        ImageView ivMoodImage3;
        TextView ivMoodPicCount;
        RelativeLayout rlMoodShare;
        RelativeLayout rlMoodComment;
        ShineButton sbLike;
        RelativeLayout rlMoodLike;

        public ShareHolder(View itemView) {
            super(itemView);
            cvMoodHead = (CircleImageView) itemView.findViewById(R.id.cv_mood_head);
            tvMoodNickname = (TextView) itemView.findViewById(R.id.tv_mood_nickname);
            tvMoodTime = (TextView) itemView.findViewById(R.id.tv_mood_time);
            tvMoodTips = (TextView) itemView.findViewById(R.id.tv_mood_tips);
            cvMoodTips = (CardView) itemView.findViewById(R.id.cv_mood_tips);
            tvMoodTitle = (TextView) itemView.findViewById(R.id.tv_mood_title);
            tvMoodContent = (TextView) itemView.findViewById(R.id.tv_mood_content);
            ivMoodImage1 = (ImageView) itemView.findViewById(R.id.iv_mood_image1);
            ivMoodImage2 = (ImageView) itemView.findViewById(R.id.iv_mood_image2);
            ivMoodImage3 = (ImageView) itemView.findViewById(R.id.iv_mood_image3);
            ivMoodPicCount = (TextView) itemView.findViewById(R.id.iv_mood_pic_count);
            rlMoodShare = (RelativeLayout) itemView.findViewById(R.id.rl_mood_share);
            rlMoodComment = (RelativeLayout) itemView.findViewById(R.id.rl_mood_comment);
            sbLike = (ShineButton) itemView.findViewById(R.id.sb_like);
            rlMoodLike = (RelativeLayout) itemView.findViewById(R.id.rl_mood_like);
            ll_share_images = (LinearLayout) itemView.findViewById(R.id.ll_mood_images);
        }

        public void load(final Mood mood , final int position) {
            Glide.with(HereApplication.getContext())
                    .load(mood.getPublisher().getHeadImageUrl())
                    .into(cvMoodHead);
            cvMoodHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mood.getPublisher().getObjectId().equals(BmobUser
                            .getCurrentUser().getObjectId())){
                        Intent intent = new Intent(context.get(),PersonalActivity.class);
                        context.get().startActivity(intent);
                    }else {
                        Intent intent = new Intent(context.get(), OtherInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("other",mood.getPublisher());
                        intent.putExtras(bundle);
                        context.get().startActivity(intent);
                    }

                }
            });
            tvMoodNickname.setText(mood.getPublisher().getNickname());
            tvMoodTime.setText(mood.getPublisherDate());
            tvMoodTips.setText(mood.getKind());
            tvMoodTitle.setText(mood.getTitle());
            tvMoodContent.setText(mood.getContent());
            cvMoodTips.setCardBackgroundColor(colors[new Random().nextInt(23)]);
            if (mood.getImages() != null && mood.getImages().length > 0) {
                ll_share_images.setVisibility(View.VISIBLE);
                if (mood.getImages().length == 1) {
                    ivMoodImage1.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[0])
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                    int[] size = CommonUtils.zoomCommunityImage(resource
                                            .getWidth(),resource.getHeight());
                                    DensityUtil.setViewSize(ivMoodImage1,size[0],size[1]);
                                    Glide.with(HereApplication.getContext())
                                            .load(mood.getImages()[0])
                                            .override(size[0],size[1])
                                            .into(ivMoodImage1);
                                }
                            });
                    ivMoodImage1.setOnClickListener(new MyClickListener(mood.getImages(),0));
                    ivMoodImage2.setVisibility(View.GONE);
                    ivMoodImage3.setVisibility(View.GONE);
                    ivMoodPicCount.setVisibility(View.GONE);
                } else if (mood.getImages().length == 2) {
                    DensityUtil.setViewSize(ivMoodImage1,DensityUtil
                            .dip2px(110),DensityUtil.dip2px(110));
                    ivMoodImage1.setVisibility(View.VISIBLE);
                    ivMoodImage2.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[0])
                            .into(ivMoodImage1);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[1])
                            .into(ivMoodImage2);
                    ivMoodImage1.setOnClickListener(new MyClickListener(mood.getImages(),0));
                    ivMoodImage2.setOnClickListener(new MyClickListener(mood.getImages(),1));
                    ivMoodImage3.setVisibility(View.GONE);
                    ivMoodPicCount.setVisibility(View.GONE);
                } else if (mood.getImages().length == 3) {
                    DensityUtil.setViewSize(ivMoodImage1,DensityUtil
                            .dip2px(110),DensityUtil.dip2px(110));
                    ivMoodImage1.setOnClickListener(new MyClickListener(mood.getImages(),0));
                    ivMoodImage2.setOnClickListener(new MyClickListener(mood.getImages(),1));
                    ivMoodImage3.setOnClickListener(new MyClickListener(mood.getImages(),2));
                    ivMoodImage1.setVisibility(View.VISIBLE);
                    ivMoodImage2.setVisibility(View.VISIBLE);
                    ivMoodImage3.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[0])
                            .into(ivMoodImage1);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[1])
                            .into(ivMoodImage2);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[2])
                            .into(ivMoodImage3);
                    ivMoodPicCount.setVisibility(View.GONE);
                } else {
                    ivMoodImage1.setOnClickListener(new MyClickListener(mood.getImages(),0));
                    ivMoodImage2.setOnClickListener(new MyClickListener(mood.getImages(),1));
                    ivMoodImage3.setOnClickListener(new MyClickListener(mood.getImages(),2));
                    DensityUtil.setViewSize(ivMoodImage1,DensityUtil
                            .dip2px(110),DensityUtil.dip2px(110));
                    ivMoodImage1.setVisibility(View.VISIBLE);
                    ivMoodImage2.setVisibility(View.VISIBLE);
                    ivMoodImage3.setVisibility(View.VISIBLE);
                    ivMoodPicCount.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[0])
                            .into(ivMoodImage1);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[1])
                            .into(ivMoodImage2);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[2])
                            .into(ivMoodImage3);
                    ivMoodPicCount.setText("+" + (mood.getImages().length - 3));
                }

            } else {
                ivMoodImage1.setVisibility(View.GONE);
                ivMoodImage2.setVisibility(View.GONE);
                ivMoodImage3.setVisibility(View.GONE);
                ivMoodPicCount.setVisibility(View.GONE);
                ll_share_images.setVisibility(View.GONE);
            }

            if (postIds.contains(mood.getObjectId())) {
                sbLike.setChecked(true);
            }else {
                sbLike.setChecked(false);
            }

            sbLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLikeing){
                        return;
                    }
                    isLikeing = true;
                    if (postIds.contains(mood.getObjectId())) {
                        final Like like = new Like();
                        like.setPublish(mood.getObjectId());
                        like.setUser(BmobUser.getCurrentUser(User.class));
                        LikeUtil.deletePost(like, new LikeUtil.OnLikeListener() {
                            @Override
                            public void success(String s) {
                                isLikeing = false;
                                refresh();
                            }
                            @Override
                            public void fail(String error) {
                                isLikeing = false;
                                sbLike.setChecked(true);
                                Toast.makeText(context.get(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        LikeUtil.likePost(BmobUser.getCurrentUser(User.class), mood
                                .getObjectId(), new LikeUtil.OnLikeListener() {
                            @Override
                            public void success(String s) {
                                sbLike.setChecked(true);
                                isLikeing = false;
                                final LikeId likeId = new LikeId();
                                likeId.setPublishId(mood.getObjectId());
                                likeId.setUserId(BmobUser.getCurrentUser(User.class).getObjectId());
                                likeId.setLikeId(s);
                                Log.i("储存","是否成功"+likeId.save()+likeId.getPublishId()+"  "+likeId.getLikeId()+"  "+likeId.getUserId());
                                refresh();
                            }
                            @Override
                            public void fail(String error) {
                                isLikeing = false;
                                sbLike.setChecked(false);
                                Toast.makeText(context.get(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            rlMoodComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.get(), PostDetailsActivity.class);
                    intent.putExtra("type","mood");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mood",mood);
                    intent.putExtras(bundle);
                    context.get().startActivity(new Intent(intent));
                }
            });

            rlMoodShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertView("分享", null, "取消", new String[]{"分享到QQ好友"}, new String[]{"分享到QQ空间"}, context.get(), AlertView.Style.ActionSheet, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            final Bundle params = new Bundle();
                            if (position == 0) {
                                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                                        QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                                params.putString(QQShare.SHARE_TO_QQ_TITLE, mood.getTitle());
                                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mood.getContent());
                                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
                                        "https://github.com/old-traveler/Here/blob/master/app/app-release.apk");
                                if (mood.getImages()!= null && mood.getImages().length>0){
                                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                                            mood.getImages()[0]);
                                }else {
                                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                                            "https://avatars1.githubusercontent.com/u/22116148?v=4&u=48ec6f70dce731dcc8f3cbf66231f5ca651e9953&s=40");
                                }
                                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "来here一起玩吧");
                                params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "与附近的人一起活动吧");
                                mTencent.shareToQQ(context.get(), params, new BaseUiListener1());
                            } else if (position == 1) {
                                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                                params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mood.getTitle());
                                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mood.getContent());
                                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,"https://github.com/old-traveler/Here/blob/master/app/app-release.apk");
                                ArrayList<String> imgUrlList = new ArrayList<>();
                                if (mood.getImages()!= null && mood.getImages().length>0){
                                    for (String s : mood.getImages()) {
                                        imgUrlList.add(s);
                                    }
                                }else {
                                    imgUrlList.add( "https://avatars1.githubusercontent.com/u/22116148?v=4&u=48ec6f70dce731dcc8f3cbf66231f5ca651e9953&s=40");
                                }
                                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,imgUrlList);
                                mTencent.shareToQzone(context.get(), params, new BaseUiListener1());
                            }else {
                                return;
                            }
                        }
                    }).show();

                }
            });
        }
    }

    public class BaseUiListener1 implements IUiListener {
        @Override
        public void onComplete(Object response) {
            doComplete(response);
        }

        protected void doComplete(Object values) {
        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }
    }

    public class MyClickListener implements View.OnClickListener {
        ArrayList<String> imageUrl;
        private int position ;

        public MyClickListener(String[] imageUrl,int position){
            this.imageUrl = new ArrayList<>();
            for (String s : imageUrl) {
                this.imageUrl.add(s);
            }
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            Pair<View, String> p = new Pair<View, String>(v, "image");
            Intent intent = new Intent(context.get(), PhotoActivity.class);
            intent.putStringArrayListExtra("images",imageUrl);
            intent.putExtra("position",position);
            context.get().startActivity(intent, ActivityOptions
                    .makeSceneTransitionAnimation(context.get(), p).toBundle());

        }
    }


    class TipsHolder extends RecyclerView.ViewHolder {
        private TextView tips;
        private ImageView change;
        public TipsHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tv_recommend_tip);
            change = (ImageView) itemView.findViewById(R.id.iv_change);
        }
        public void load(){
            if (!isShare){
                tips.setText("预约活动");
            }else {
                tips.setText("心情分享");
            }
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShare){
                        tips.setText("预约活动");
                        listener.onChange(false);
                        isShare = false;
                    }else {
                        tips.setText("心情分享");
                        listener.onChange(true);
                        isShare = true;
                    }
                }
            });
        }
    }

    public void switchFail(boolean isShare){
        this.isShare = isShare;
        notifyItemChanged(2);
    }

    private OnSwitchChangeListener listener;

    public interface OnSwitchChangeListener{
        void onChange(boolean isShare);
    }
}
