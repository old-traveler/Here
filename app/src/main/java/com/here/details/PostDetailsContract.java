package com.here.details;

import com.here.bean.Appointment;
import com.here.bean.Comment;
import com.here.bean.Mood;

import java.util.List;

/**
 * Created by hyc on 2017/7/16 11:26
 */

public interface PostDetailsContract {

    String getType();

    Appointment getAppointment();

    Mood  getMood();

    void setMood(Mood mood);

    void setAppointment(Appointment appointment);

    void loadComment(List<Comment> comments);

    void loadFail(String error);

    void showLoading();

    void stopLoading();

    String getCommentMessage();

    void commentSuccess(Comment comment);

    void commentFail(String error);


}
