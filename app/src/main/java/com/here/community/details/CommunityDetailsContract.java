package com.here.community.details;

import com.here.bean.Community;

import java.util.List;

/**
 * Created by hyc on 2017/7/14 08:07
 */

public interface CommunityDetailsContract {

    void showLoading();

    void stopLoading();

    void loadSuccess(List<Community> communities);

    void fail(String error);

    String getKind();

}
