package com.here.tips;

import com.here.HereApplication;
import com.here.R;
import com.here.base.BasePresenter;
import com.here.bean.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyc on 2017/7/5 12:42
 */

public class TipsPresenter extends BasePresenter<TipsContract> {


    public void loadTips(){
        List<Tip> tips = new ArrayList<>();
        String[] tips_name=HereApplication.getContext().getResources().getStringArray(R.array.tip_name);
        String[] tips_slogan=HereApplication.getContext().getResources().getStringArray(R.array.tip_slogan);
        int[]  bg=HereApplication.getContext().getResources().getIntArray(R.array.tips_bg);
        for (int i = 0; i < 24; i++) {
            Tip tip=new Tip();
            tip.setHave(false);
            tip.setColor(bg[i]);
            tip.setName(tips_name[i]);
            tip.setSlogan(tips_slogan[i]);
            tips.add(tip);
        }
        mvpView.initData(tips);
    }


}
