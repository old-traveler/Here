package com.here.util;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.Appointment;
import com.here.bean.Community;
import com.here.bean.Mood;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
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
    public static void queryMoodByKind(String kind, final CommunitySearchListener listener){
        BmobQuery<Mood> moodBmobQuery = new BmobQuery<>();
        moodBmobQuery.addWhereEqualTo("kind",kind);
        moodBmobQuery.include("publisher");
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
    public static void queryAppointmentByKind(String kind, final CommunitySearchListener listener){
        BmobQuery<Appointment> query = new BmobQuery<>();
        query.addWhereEqualTo("kind",kind);
        query.setLimit(50);
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
    public static void queryMood(final CommunitySearchListener listener){
        BmobQuery<Mood> query = new BmobQuery<>();
        query.setLimit(50);
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
     * 查询预约活动
     * @param listener
     */
    public static void queryAppointment(final CommunitySearchListener listener){
        BmobQuery<Appointment> query = new BmobQuery<>();
        query.setLimit(50);
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
     * 通过发布时间排序
     * @param communities
     */
    public static List<Community> sortByTime(List<Community> communities){
        Collections.sort(communities, new Comparator<Community>() {
            @Override
            public int compare(Community o1, Community o2) {
                long time1 = o1.getType() == Community.TYPE_SHARE ? o1.getMood().getPublishTime()
                        : o1.getAppointment().getPublishTime();
                long time2 = o2.getType() == Community.TYPE_SHARE ? o2.getMood().getPublishTime()
                        : o2.getAppointment().getPublishTime();
                return (int) (time2-time1);
            }
        });
        return communities;
    }

}
