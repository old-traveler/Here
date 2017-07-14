package com.here.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.here.HereApplication;
import com.here.R;
import com.here.bean.Community;
import com.here.bean.Propaganda;
import java.util.List;


/**
 * Created by hyc on 2017/7/13 10:21
 */

public class CommunityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public CommunityAdapter(List<Community> communities){
        this.communities = communities;
    }

    private List<Community> communities;

    private boolean isLoading=false;


    @Override
    public int getItemViewType(int position) {
        if (communities.get(position).getType() == Community.TYPE_VIEW_PAGE){
            return Community.TYPE_VIEW_PAGE;
        }else if (communities.get(position).getType() == Community.TYPE_COMMUNITY){
            return Community.TYPE_COMMUNITY;
        }else if (communities.get(position).getType() == Community.TYPE_APPOINTMENT){
            return Community.TYPE_APPOINTMENT;
        }else if (communities.get(position).getType() == Community.TYPE_SHARE){
            return Community.TYPE_SHARE;
        }else if (communities.get(position).getType() == Community.TYPE_TIPS){
           return Community.TYPE_TIPS;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Community.TYPE_VIEW_PAGE){
            return new ViewPageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_page,parent,false));
        }else if (viewType == Community.TYPE_COMMUNITY){
            return new CommunityHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community,parent,false));
        }else if (viewType == Community.TYPE_APPOINTMENT){
            return new AppointmentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment,parent,false));
        }else if (viewType == Community.TYPE_SHARE){
            return new ShareHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share,parent,false));
        }else if (viewType == Community.TYPE_TIPS){
            return new TipsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_tip,parent,false));
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewPageHolder && !isLoading){
            isLoading = true;
            ViewPageHolder vHolder = (ViewPageHolder) holder;
            vHolder.load(Community.getPropagandas());
        }
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }

    class ViewPageHolder extends RecyclerView.ViewHolder{
        protected SliderLayout sliderLayout;
        public ViewPageHolder(View itemView) {
            super(itemView);
            sliderLayout = (SliderLayout) itemView.findViewById(R.id.slider);
            sliderLayout.setDuration(5000);
        }

        public void load(Propaganda[] propagandas){
            for (Propaganda propaganda : propagandas) {
                TextSliderView mTextSliderView = new TextSliderView(HereApplication.getContext());
                mTextSliderView.description(propaganda.getDescribe())
                        .image(propaganda.getImage())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);
                sliderLayout.addSlider(mTextSliderView);
            }

        }
    }

    class CommunityHolder extends RecyclerView.ViewHolder{

        public CommunityHolder(View itemView) {
            super(itemView);
        }

    }

    class AppointmentHolder extends RecyclerView.ViewHolder{

        public AppointmentHolder(View itemView) {
            super(itemView);
        }

        public void load(){

        }
    }

    class ShareHolder extends RecyclerView.ViewHolder{

        public ShareHolder(View itemView) {
            super(itemView);
        }

        public void load(){

        }
    }

    class TipsHolder extends RecyclerView.ViewHolder{

        public TipsHolder(View itemView) {
            super(itemView);
        }
    }
}
