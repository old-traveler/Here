package com.here.photo;

import java.util.List;

/**
 * Created by hyc on 2017/7/3 05:58
 */

public interface PhotoContract {


    void getImages();

    void loadImages(List<String> images);

    int getPosition();

}
