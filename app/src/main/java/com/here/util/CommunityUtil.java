package com.here.util;

import android.util.Log;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.Appointment;
import com.here.bean.Community;
import com.here.bean.FollowId;
import com.here.bean.Mood;
import com.here.bean.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 社区查看工具类 Created by hyc on 2017/7/15 10:47
 */

public class CommunityUtil {

    /**
     * 查询监听
     */
    public interface CommunitySearchListener{
        void success(List<Community> communities);
        void fail(String error);
    }

    /**
     * 通过类型查询心情信息
     * @param kind
     * @param listener
     */
    public static void queryMoodByKind(String kind
            ,int page, final CommunitySearchListener listener){
        BmobQuery<Mood> moodBmobQuery = new BmobQuery<>();
        moodBmobQuery.addWhereEqualTo("kind",kind);
        moodBmobQuery.include("publisher");
        moodBmobQuery.setLimit(20);
        moodBmobQuery.setSkip(page * 20);
        moodBmobQuery.order("-createdAt");
        moodBmobQuery.findObjects(new FindListener<Mood>() {
            @Override
            public void done(List<Mood> list, BmobException e) {
                if (e == null){
                    List<Community> communities = new ArrayList<>();
                    for (Mood mood : list) {
                        Community community = new Community();
                        community.setType(Community.TYPE_SHARE);
                        community.setMood(mood);
                        communities.add(community);
                    }
                    listener.success(communities);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 通过类型查询预约信息
     * @param kind
     * @param listener
     */
    public static void queryAppointmentByKind(String kind
            ,int page, final CommunitySearchListener listener){
        BmobQuery<Appointment> query = new BmobQuery<>();
        query.addWhereEqualTo("kind",kind);
        query.setLimit(20);
        query.setSkip(page * 20);
        query.order("-createdAt");
        query.include("publisher");
        query.findObjects(new FindListener<Appointment>() {
            @Override
            public void done(List<Appointment> list, BmobException e) {
                if (e == null){
                    List<Community> communities = new ArrayList<>();
                    for (Appointment appointment : list) {
                        Community community = new Community();
                        community.setType(Community.TYPE_APPOINTMENT);
                        community.setAppointment(appointment);
                        communities.add(community);
                    }
                    listener.success(communities);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 查询心情分享
     * @param listener
     */
    public static void queryMood(int page,final CommunitySearchListener listener){
        BmobQuery<Mood> query = new BmobQuery<>();
        query.setLimit(20);
        query.setSkip(page * 20);
        query.order("-createdAt");
        query.include("publisher");
        query.findObjects(new FindListener<Mood>() {
            @Override
            public void done(List<Mood> list, BmobException e) {
                if (e == null){
                    List<Community> communities = new ArrayList<>();
                    for (Mood mood : list) {
                        Community community = new Community();
                        community.setType(Community.TYPE_SHARE);
                        community.setMood(mood);
                        communities.add(community);
                    }
                    listener.success(communities);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 查询用户的心情分享的记录
     * @param user 用户
     * @param page 页码
     * @param listener 查询监听
     */
    public static void queryUserMoodRecord(final User user
            , int page, final CommunitySearchListener listener){
        BmobQuery<Mood> query = new BmobQuery<>();
        query.setLimit(20);
        query.setSkip(page * 20);
        query.order("-createdAt");
        query.addWhereEqualTo("publisher",user);
        query.findObjects(new FindListener<Mood>() {
            @Override
            public void done(List<Mood> list, BmobException e) {
                if (e == null){
                    List<Community> communities = new ArrayList<>();
                    for (Mood mood : list) {
                        mood.setPublisher(user);
                        Community community = new Community();
                        community.setType(Community.TYPE_SHARE);
                        community.setMood(mood);
                        communities.add(community);
                    }
                    listener.success(communities);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext()
                                .getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 查询预约活动
     * @param listener
     */
    public static void queryAppointment(int page,final CommunitySearchListener listener){
        BmobQuery<Appointment> query = new BmobQuery<>();
        query.setLimit(20);
        query.setSkip(page * 20);
        query.order("-createdAt");
        query.include("publisher");
        query.findObjects(new FindListener<Appointment>() {
            @Override
            public void done(List<Appointment> list, BmobException e) {
                if (e == null){
                    List<Community> communities = new ArrayList<>();
                    for (Appointment appointment : list) {
                        Community community = new Community();
                        community.setType(Community.TYPE_APPOINTMENT);
                        community.setAppointment(appointment);
                        communities.add(community);
                    }
                    listener.success(communities);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 查询用户发布预约活动的记录
     * @param user 用户
     * @param page 页码
     * @param listener 查询监听
     */
    public static void queryUserAppointmentRecord(final User user
            , int page, final CommunitySearchListener listener){
        BmobQuery<Appointment> query = new BmobQuery<>();
        query.setLimit(20);
        query.setSkip(page * 20);
        query.order("-createdAt");
        query.addWhereEqualTo("publisher",user);
        query.findObjects(new FindListener<Appointment>() {
            @Override
            public void done(List<Appointment> list, BmobException e) {
                if (e == null){
                    List<Community> communities = new ArrayList<>();
                    for (Appointment appointment : list) {
                        appointment.setPublisher(user);
                        Community community = new Community();
                        community.setType(Community.TYPE_APPOINTMENT);
                        community.setAppointment(appointment);
                        communities.add(community);
                    }
                    listener.success(communities);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext()
                                .getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 查询关注用户的预约活动
     * @param listener  查询监听
     */
    public static void queryFollowAppointment(final CommunitySearchListener listener){
        List<String> followUserIds = new ArrayList<>();
        String userId = BmobUser.getCurrentUser(User.class).getObjectId();
        for (FollowId followId : DataSupport.findAll(FollowId.class)) {
            if (followId.getUserId().equals(userId)){
                followUserIds.add(followId.getFollowUserId());
                Log.i("关注Id",followId.getUserId()+"   "+followId.getFollowUserId());
            }
        }
        if (followUserIds.size() > 0){
            BmobQuery<Appointment> query = new BmobQuery<>();
            List<BmobQuery<Appointment>> queries = new ArrayList<>();
            for (String followUserId : followUserIds) {
                BmobQuery<Appointment> appointment = new BmobQuery<>();
                User user = new User();
                user.setObjectId(followUserId);
                appointment.addWhereEqualTo("publisher",user);
                queries.add(appointment);
            }
            query.or(queries);
            query.include("publisher");
            query.findObjects(new FindListener<Appointment>() {
                @Override
                public void done(List<Appointment> list, BmobException e) {
                    if (e == null){
                        List<Community> communities = new ArrayList<>();
                        for (Appointment appointment : list) {
                            Community community = new Community();
                            community.setType(Community.TYPE_APPOINTMENT);
                            community.setAppointment(appointment);
                            communities.add(community);
                        }
                        listener.success(communities);
                    }else {
                        if (e.getErrorCode() == 9016){
                            listener.fail(HereApplication.getContext()
                                    .getString(R.string.err_no_net));
                        }else {
                            listener.fail(e.getMessage());
                        }
                    }
                }
            });
        }else {
            listener.success(null);
        }
    }

    /**
     * 查询关注用户的心情分享
     * @param listener
     */
    public static void queryFollowMood(final CommunitySearchListener listener){
        List<String> followUserIds = new ArrayList<>();
        String userId = BmobUser.getCurrentUser(User.class).getObjectId();
        for (FollowId followId : DataSupport.findAll(FollowId.class)) {
            if (followId.getUserId().equals(userId)){
                followUserIds.add(followId.getFollowUserId());

            }
        }

        if (followUserIds.size() > 0){
            BmobQuery<Mood> query = new BmobQuery<>();
            List<BmobQuery<Mood>> queries = new ArrayList<>();
            for (String followUserId : followUserIds) {
                BmobQuery<Mood> appointment = new BmobQuery<>();
                User user = new User();
                user.setObjectId(followUserId);
                appointment.addWhereEqualTo("publisher",user);
                queries.add(appointment);
            }
            query.or(queries);
            query.include("publisher");
            query.findObjects(new FindListener<Mood>() {
                @Override
                public void done(List<Mood> list, BmobException e) {
                    if (e == null){
                        List<Community> communities = new ArrayList<>();
                        for (Mood mood : list) {
                            Community community = new Community();
                            community.setType(Community.TYPE_SHARE);
                            community.setMood(mood);
                            communities.add(community);
                        }
                        listener.success(communities);
                    }else {
                        if (e.getErrorCode() == 9016){
                            listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                        }else {
                            listener.fail(e.getMessage());
                        }
                    }
                }
            });
        }else {
            listener.success(null);
        }
    }

    /**
     * 通过发布时间排序
     * @param communities
     */
    public static List<Community> sortByTime(List<Community> communities){
        Collections.sort(communities, new Comparator<Community>() {
            @Override
            public int compare(Community o1, Community o2) {
                long time1 = o1.getType() == Community.TYPE_SHARE ? o1.getMood()
                        .getPublishTime() : o1.getAppointment().getPublishTime();
                long time2 = o2.getType() == Community.TYPE_SHARE ? o2.getMood()
                        .getPublishTime() : o2.getAppointment().getPublishTime();
                return (int) (time2-time1);
            }
        });
        return communities;
    }




}
