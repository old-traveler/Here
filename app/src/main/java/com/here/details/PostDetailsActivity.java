package com.here.details;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.here.R;
import com.here.adapter.CommentAdapter;
import com.here.adapter.DetailsImageAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Appointment;
import com.here.bean.Comment;
import com.here.bean.Mood;
import com.here.personal.PersonalActivity;
import com.here.personal.other.OtherInfoActivity;
import com.here.view.MyGridLayoutManager;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailsActivity extends MvpActivity<PostDetailsPresenter> implements PostDetailsContract {

    @Bind(R.id.cv_post_head)
    CircleImageView cvPostHead;
    @Bind(R.id.tv_post_nickname)
    TextView tvPostNickname;
    @Bind(R.id.tv_post_time)
    TextView tvPostTime;
    @Bind(R.id.tv_post_tips)
    TextView tvPostTips;
    @Bind(R.id.cv_post_tips)
    CardView cvPostTips;
    @Bind(R.id.tv_post_address)
    TextView tvPostAddress;
    @Bind(R.id.ll_post_location)
    LinearLayout llPostLocation;
    @Bind(R.id.tv_post_interval)
    TextView tvPostInterval;
    @Bind(R.id.tv_post_number)
    TextView tvPostNumber;
    @Bind(R.id.tv_post_title)
    TextView tvPostTitle;
    @Bind(R.id.tv_post_content)
    TextView tvPostContent;
    @Bind(R.id.rv_image_post)
    RecyclerView rvImagePost;
    @Bind(R.id.tv_comment_count)
    TextView tvCommentCount;
    @Bind(R.id.rv_post_comment)
    RecyclerView rvPostComment;
    @Bind(R.id.et_comment_input)
    EditText etCommentInput;
    @Bind(R.id.sb_post_like)
    ShineButton sbPostLike;

    @Bind(R.id.pb_post_loading)
    ProgressBar pbPostLoading;

    private DetailsImageAdapter adapter;

    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_post_details);
        initHome();
        mvpPresenter.loadData();
        etCommentInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etCommentInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                        && KeyEvent.ACTION_DOWN == event.getAction())) {
                    mvpPresenter.comment();
                    return true;
                }
                return false;
            }
        });
        commentAdapter = new CommentAdapter(null);
        rvPostComment.setLayoutManager(new LinearLayoutManager(this));
        rvPostComment.setItemAnimator(new DefaultItemAnimator());
        rvPostComment.setAdapter(commentAdapter);
    }

    @Override
    protected PostDetailsPresenter createPresenter() {
        return new PostDetailsPresenter();
    }

    @Override
    public String getType() {
        return getIntent().getStringExtra("type");
    }

    @Override
    public Appointment getAppointment() {
        return (Appointment) getIntent().getSerializableExtra("appointment");
    }

    @Override
    public Mood getMood() {
        return (Mood) getIntent().getSerializableExtra("mood");
    }

    @Override
    public void setMood(final Mood mood) {
        if (TextUtils.isEmpty(mood.getPublisher().getHeadImageUrl())){
            Glide.with(this)
                    .load(R.drawable.head_info)
                    .into(cvPostHead);
        }else {
            Glide.with(this)
                    .load(mood.getPublisher().getHeadImageUrl())
                    .into(cvPostHead);
        }

        cvPostHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mood.getPublisher().getObjectId().equals(BmobUser
                        .getCurrentUser().getObjectId())){
                    Pair<View, String> p = new Pair<View, String>(v, "image");
                    Intent intent = new Intent(PostDetailsActivity
                            .this, PersonalActivity.class);
                    startActivity(intent, ActivityOptions
                            .makeSceneTransitionAnimation(PostDetailsActivity.this, p).toBundle());
                }else {
                    Pair<View, String> p = new Pair<View, String>(v, "image");
                    Intent intent = new Intent(PostDetailsActivity
                            .this, OtherInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("other",mood.getPublisher());
                    intent.putExtras(bundle);
                    startActivity(intent, ActivityOptions
                            .makeSceneTransitionAnimation(PostDetailsActivity.this, p).toBundle());
                }

            }
        });
        tvPostNickname.setText(mood.getPublisher().getNickname());
        tvPostTime.setText(mood.getPublisherDate());
        tvPostTitle.setText(mood.getTitle());
        tvPostContent.setText(mood.getContent());
        tvPostTips.setText(mood.getKind());
        llPostLocation.setVisibility(View.GONE);
        tvPostInterval.setVisibility(View.GONE);
        tvPostNumber.setVisibility(View.GONE);
        if (mood.getImages() == null || mood.getImages().length < 1) {
            rvImagePost.setVisibility(View.GONE);
            return;
        }
        List<String> list = Arrays.asList(mood.getImages());
        if (list.size() == 1) {
            adapter = new DetailsImageAdapter(R.layout.item_one_image, list);
            rvImagePost.setLayoutManager(new MyGridLayoutManager(this, 1));
        } else if (list.size() == 2) {
            adapter = new DetailsImageAdapter(R.layout.item_two_image, list);
            rvImagePost.setLayoutManager(new MyGridLayoutManager(this, 2));
        } else {
            adapter = new DetailsImageAdapter(R.layout.item_im_details, list);
            rvImagePost.setLayoutManager(new MyGridLayoutManager(this, 3));
        }
        adapter.setActivityWeakReference(new WeakReference<Activity>(this));
        rvImagePost.setAdapter(adapter);
    }

    @Override
    public void setAppointment(final Appointment appointment) {
        if (TextUtils.isEmpty(appointment.getPublisher().getHeadImageUrl())){
            Glide.with(this)
                    .load(R.drawable.head_info)
                    .into(cvPostHead);
        }else {
            Glide.with(this)
                    .load(appointment.getPublisher().getHeadImageUrl())
                    .into(cvPostHead);
        }
        cvPostHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appointment.getPublisher().getObjectId()
                        .equals(BmobUser.getCurrentUser().getObjectId())){
                    Pair<View, String> p = new Pair<View, String>(v, "image");
                    Intent intent = new Intent(PostDetailsActivity
                            .this,PersonalActivity.class);
                    startActivity(intent, ActivityOptions
                            .makeSceneTransitionAnimation(PostDetailsActivity.this, p).toBundle());
                }else {
                    Pair<View, String> p = new Pair<View, String>(v, "image");
                    Intent intent = new Intent(PostDetailsActivity
                            .this, OtherInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("other",appointment.getPublisher());
                    intent.putExtras(bundle);
                    startActivity(intent, ActivityOptions
                            .makeSceneTransitionAnimation(PostDetailsActivity.this, p).toBundle());
                }
            }
        });
        tvPostNickname.setText(appointment.getPublisher().getNickname());
        tvPostTime.setText(appointment.getPublishDate());
        tvPostTitle.setText(appointment.getTitle());
        tvPostContent.setText(appointment.getDescribe());
        tvPostTips.setText(appointment.getKind());
        tvPostAddress.setText(appointment.getAddress());
        tvPostTime.setText(appointment.getStartDate() + "~" + appointment.getOverDate());
        tvPostNumber.setText("参与人数：" + appointment.getJoinNumber());
        if (appointment.getImages() == null || appointment.getImages().length < 1) {
            rvImagePost.setVisibility(View.GONE);
            return;
        }
        List<String> list = Arrays.asList(appointment.getImages());
        if (list.size() == 1) {
            adapter = new DetailsImageAdapter(R.layout.item_one_image, list);
            rvImagePost.setLayoutManager(new MyGridLayoutManager(this, 1));
        } else if (list.size() == 2) {
            adapter = new DetailsImageAdapter(R.layout.item_two_image, list);
            rvImagePost.setLayoutManager(new MyGridLayoutManager(this, 2));
        } else {
            adapter = new DetailsImageAdapter(R.layout.item_im_details, list);
            rvImagePost.setLayoutManager(new MyGridLayoutManager(this, 3));
        }
        adapter.setActivityWeakReference(new WeakReference<Activity>(this));
        rvImagePost.setAdapter(adapter);
    }

    @Override
    public void loadComment(List<Comment> comments) {
        commentAdapter.setNewData(comments);
    }

    @Override
    public void loadFail(String error) {
        toastShow(error);
    }

    @Override
    public void showLoading() {
        pbPostLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        pbPostLoading.setVisibility(View.GONE);
    }

    @Override
    public String getCommentMessage() {
        return etCommentInput.getText().toString();
    }

    @Override
    public void commentSuccess(Comment comment) {
        etCommentInput.setText("");
        commentAdapter.add(commentAdapter.getItemCount(), comment);
    }

    @Override
    public void commentFail(String error) {
        toastShow(error);
    }



    @OnClick({R.id.cv_post_head, R.id.sb_post_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_post_head:
                break;
            case R.id.sb_post_like:
                break;
        }
    }
}
