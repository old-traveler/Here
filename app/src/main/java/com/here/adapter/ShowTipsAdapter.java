package com.here.adapter;

import android.support.v7.widget.CardView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.Tip;

import java.util.List;

/**
 * Created by hyc on 2017/7/5 16:04
 */

public class ShowTipsAdapter extends BaseQuickAdapter<Tip> {


    private PublishImageAdapter.OnItemClickListener listener;

    public ShowTipsAdapter( List<Tip> data) {
        super(R.layout.item_show_tips, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Tip tip) {
        CardView cardView=baseViewHolder.getView(R.id.cv_tips);
        cardView.setCardBackgroundColor(tip.getColor());
        baseViewHolder.setText(R.id.tv_tips_name,tip.getName());
        baseViewHolder.setOnClickListener(R.id.rl_show_tips, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onClick();
                }
            }
        });
    }


    public void setListener(PublishImageAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
