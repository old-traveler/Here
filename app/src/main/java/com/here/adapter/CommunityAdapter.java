package com.here.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.here.HereApplication;
import com.here.R;
import com.here.bean.Community;
import com.here.bean.Propaganda;
import com.here.community.details.CommunityDetailsActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by hyc on 2017/7/13 10:21
 */

public class CommunityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Bind(R.id.rl_item_sport)
    RelativeLayout rlItemSport;
    @Bind(R.id.rl_item_shopping)
    RelativeLayout rlItemShopping;
    @Bind(R.id.rl_item_sing)
    RelativeLayout rlItemSing;
    @Bind(R.id.rl_item_movie)
    RelativeLayout rlItemMovie;
    @Bind(R.id.rl_item_camping)
    RelativeLayout rlItemCamping;
    @Bind(R.id.rl_item_bar)
    RelativeLayout rlItemBar;
    @Bind(R.id.rl_item_beauty)
    RelativeLayout rlItemBeauty;
    @Bind(R.id.rl_item_delicious)
    RelativeLayout rlItemDelicious;
    @Bind(R.id.rl_item_chess)
    RelativeLayout rlItemChess;
    @Bind(R.id.rl_item_game)
    RelativeLayout rlItemGame;
    @Bind(R.id.rl_item_role)
    RelativeLayout rlItemRole;
    @Bind(R.id.rl_item_other)
    RelativeLayout rlItemOther;

    public CommunityAdapter(List<Community> communities) {
        this.communities = communities;
    }

    private List<Community> communities;

    private boolean isLoading = false;


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
        }
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }

    @OnClick({R.id.rl_item_sport, R.id.rl_item_shopping, R.id.rl_item_sing, R.id.rl_item_movie, R.id.rl_item_camping, R.id.rl_item_bar, R.id.rl_item_beauty, R.id.rl_item_delicious, R.id.rl_item_chess, R.id.rl_item_game, R.id.rl_item_role, R.id.rl_item_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_item_sport:

                break;
            case R.id.rl_item_shopping:
                break;
            case R.id.rl_item_sing:
                break;
            case R.id.rl_item_movie:
                break;
            case R.id.rl_item_camping:
                break;
            case R.id.rl_item_bar:
                break;
            case R.id.rl_item_beauty:
                break;
            case R.id.rl_item_delicious:
                break;
            case R.id.rl_item_chess:
                break;
            case R.id.rl_item_game:
                break;
            case R.id.rl_item_role:
                break;
            case R.id.rl_item_other:
                break;
        }
        HereApplication.getContext().startActivity(new Intent(HereApplication.getContext(), CommunityDetailsActivity.class));
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

    class CommunityHolder extends RecyclerView.ViewHolder {

        public CommunityHolder(View itemView) {
            super(itemView);
        }

    }

    class AppointmentHolder extends RecyclerView.ViewHolder {

        public AppointmentHolder(View itemView) {
            super(itemView);
        }

        public void load() {

        }
    }

    class ShareHolder extends RecyclerView.ViewHolder {

        public ShareHolder(View itemView) {
            super(itemView);
        }

        public void load() {

        }
    }

    class TipsHolder extends RecyclerView.ViewHolder {

        public TipsHolder(View itemView) {
            super(itemView);
        }
    }
}
