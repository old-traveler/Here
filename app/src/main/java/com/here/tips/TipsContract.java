package com.here.tips;

import com.here.bean.Tip;

import java.util.List;

/**
 * Created by hyc on 2017/7/5 12:42
 */

public interface TipsContract {

    void initData(List<Tip> tips);

    String[] getTips();

    void showLoading();

    void stopLoading();

    void loadSuccess();

    void loadFail(String error);

}
