package com.here.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.here.R;
import com.here.bean.Community;

import java.util.List;

/**
 * Created by hyc on 2017/7/14 09:47
 */

public class CommunityDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int DESCRIBE = 0x45;
    public static int SHARE = 0x46;
    public static int APPOINTMENT = 0x47;

    List<Community> communities;

    public CommunityDetailsAdapter(List<Community> communities){
        this.communities = communities;
    }


    @Override
    public int getItemViewType(int position) {
        if (communities.get(position).getType() == DESCRIBE){
            return DESCRIBE;
        }else if (communities.get(position).getType() == SHARE){
            return SHARE;
        }else if (communities.get(position).getType() == APPOINTMENT){
            return APPOINTMENT;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == DESCRIBE){
            return new DescribeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_describe,parent,false));
        }else if (viewType == SHARE){
            return new DescribeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share,parent,false));
        }else if (viewType == APPOINTMENT){
            return new AppointmentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return communities.size();
    }


    class DescribeHolder extends RecyclerView.ViewHolder{

        public DescribeHolder(View itemView) {
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
}
