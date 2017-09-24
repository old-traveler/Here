package com.here.follow;

import com.here.bean.Community;
import com.here.bean.FindImage;

import java.util.List;

/**
 * Created by hyc on 2017/6/21 14:56
 */

public interface FollowContract {


    void loadFail(String error);

    void reminderJoin();

    void refreshData(List<FindImage> images);

    void addData(List<FindImage> images);

}
