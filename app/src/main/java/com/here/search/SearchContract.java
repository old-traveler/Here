package com.here.search;

import com.here.bean.User;

/**
 * Created by hyc on 2017/7/10 18:55
 */

public interface SearchContract {

    String getSearchInfo();

    void showLoading();

    void stopLoading();

    void loadingSuccess(User user);

    void fail(String error);

}
