package com.here.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.here.HereApplication;
import com.here.R;
import com.here.bean.Appointment;
import com.here.bean.Community;
import com.here.bean.Mood;
import com.sackcentury.shinebuttonlib.ShineButton;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/7/14 09:47
 */

public class CommunityDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int DESCRIBE = 0x45;
    public static int SHARE = 0x24;
    public static int APPOINTMENT = 0x23;

    List<Community> communities;

    public List<Community> getData() {
        return communities;
    }

    public void addData(List<Community> list) {
        Community c = communities.get(0);
        list.add(0, c);
        communities = null;
        communities = list;
        notifyDataSetChanged();
    }

    public CommunityDetailsAdapter(List<Community> communities) {
        this.communities = communities;
    }

    @Override
    public int getItemViewType(int position) {
        if (communities.get(position).getType() == DESCRIBE) {
            return DESCRIBE;
        } else if (communities.get(position).getType() == SHARE) {
            return SHARE;
        } else if (communities.get(position).getType() == APPOINTMENT) {
            return APPOINTMENT;
        }
        return SHARE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == DESCRIBE) {
            return new DescribeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_describe, parent, false));
        } else if (viewType == SHARE) {
            return new ShareHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false));
        } else if (viewType == APPOINTMENT) {
            return new AppointmentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DescribeHolder) {
            DescribeHolder describeHolder = (DescribeHolder) holder;
            describeHolder.load(communities.get(position).getDescribe());
        }else if (holder instanceof ShareHolder){
            ShareHolder shareHolder = (ShareHolder) holder;
            shareHolder.load(communities.get(position).getMood());
        }else if (holder instanceof AppointmentHolder){
            AppointmentHolder aHolder = (AppointmentHolder) holder;
            aHolder.load(communities.get(position).getAppointment());
        }
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }


    class DescribeHolder extends RecyclerView.ViewHolder {
        protected TextView tvDetails;

        public DescribeHolder(View itemView) {
            super(itemView);
            tvDetails = (TextView) itemView.findViewById(R.id.item_details_describe);
        }

        public void load(String describe) {
            tvDetails.setText(describe);
        }

    }

    class AppointmentHolder extends RecyclerView.ViewHolder {
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
        }

        public void load(Appointment appointment) {
            Glide.with(HereApplication.getContext())
                    .load(appointment.getPublisher().getHeadImageUrl())
                    .into(cvAppointmentHead);
            tvAppointmentNickname.setText(appointment.getPublisher().getNickname());
            tvAppointmentTime.setText(appointment.getPublishDate());
            tvAppointmentKind.setText(appointment.getKind());
            tvAppointmentAddress.setText(appointment.getAddress());
            tvAppointmentInterval.setText(appointment.getStartDate()+"~"+appointment.getOverDate());
            tvAppointmentTitle.setText(appointment.getTitle());
            tvAppointmentNumber.setText("参与人数："+appointment.getJoinNumber());
            tvAppointmentContent.setText(appointment.getDescribe());
            if (appointment.getImages() != null && appointment.getImages().length > 0) {
                if (appointment.getImages().length == 1) {
                    ivAppointmentImage1.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(appointment.getImages()[0])
                            .into(ivAppointmentImage1);
                    ivAppointmentImage2.setVisibility(View.GONE);
                    ivAppointmentImage3.setVisibility(View.GONE);
                    tvAppointmentPicCount.setVisibility(View.GONE);
                } else if (appointment.getImages().length == 2) {
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
                ivAppointmentImage1.setVisibility(View.GONE);
                ivAppointmentImage2.setVisibility(View.GONE);
                ivAppointmentImage3.setVisibility(View.GONE);
                tvAppointmentPicCount.setVisibility(View.GONE);
                ll_appointment_images.setVisibility(View.GONE);
            }
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

        public void load(Mood mood) {
            Glide.with(HereApplication.getContext())
                    .load(mood.getPublisher().getHeadImageUrl())
                    .into(cvMoodHead);
            tvMoodNickname.setText(mood.getPublisher().getNickname());
            tvMoodTime.setText(mood.getPublisherDate());
            tvMoodTips.setText(mood.getKind());
            tvMoodTitle.setText(mood.getTitle());
            tvMoodContent.setText(mood.getContent());
            if (mood.getImages() != null && mood.getImages().length > 0) {
                if (mood.getImages().length == 1) {
                    ivMoodImage1.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[0])
                            .into(ivMoodImage1);
                    ivMoodImage2.setVisibility(View.GONE);
                    ivMoodImage3.setVisibility(View.GONE);
                    ivMoodPicCount.setVisibility(View.GONE);
                } else if (mood.getImages().length == 2) {
                    ivMoodImage1.setVisibility(View.VISIBLE);
                    ivMoodImage2.setVisibility(View.VISIBLE);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[0])
                            .into(ivMoodImage1);
                    Glide.with(HereApplication.getContext())
                            .load(mood.getImages()[1])
                            .into(ivMoodImage2);
                    ivMoodImage3.setVisibility(View.GONE);
                    ivMoodPicCount.setVisibility(View.GONE);
                } else if (mood.getImages().length == 3) {
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


        }
    }
}