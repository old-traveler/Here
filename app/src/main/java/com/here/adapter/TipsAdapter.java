package com.here.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.Tip;

import java.util.List;

/**
 * Created by hyc on 2017/7/5 13:24
 */

public class TipsAdapter extends BaseQuickAdapter<Tip>  {

    public TipsAdapter(List<Tip> data) {
        super(R.layout.item_tips, data);
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final Tip tip) {
        CardView cardView=baseViewHolder.getView(R.id.cv_tips);
        cardView.setCardBackgroundColor(tip.getColor());
        baseViewHolder.setText(R.id.tv_tips_name,tip.getName());
        baseViewHolder.setText(R.id.tv_tips_slogan,tip.getSlogan());
        baseViewHolder.getView(R.id.iv_tips_selected).setVisibility(tip.isHave()?View.VISIBLE:View.GONE);
        baseViewHolder.setOnClickListener(R.id.rl_tips, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip.setHave(tip.isHave()?false:true);
                baseViewHolder.getView(R.id.iv_tips_selected).setVisibility(tip.isHave()?View.VISIBLE:View.GONE);
            }
        });
    }
}
