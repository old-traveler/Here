package com.here.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.ImActivity;
import com.here.bean.Join;
import com.here.bean.User;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hyc on 2017/7/11 15:55
 */

public class JoinUtil {

    public interface OnSearchJoinListener{
        void success(List<Join> joins);
        void fail(String error);
    }

    public static void joinNewImActivity(boolean isApply,String time){
        SharedPreferences join = HereApplication.getContext().getSharedPreferences("join", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = join.edit();
        editor.clear();
        editor.putString("time",time);
        editor.putBoolean("isApply",isApply);
        if (!editor.commit()){
            Toast.makeText(HereApplication.getContext(),"缓存失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static void cacheApplyUserId(String id){
        Log.i("缓存","缓存用户id"+id);
        SharedPreferences join = HereApplication.getContext().getSharedPreferences("applyId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = join.edit();
        editor.clear();
        editor.putString("applyId",id);
        if (!editor.commit()){
            Toast.makeText(HereApplication.getContext(),"缓存失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getApplyUserId(){
        SharedPreferences preferences = HereApplication.getContext()
                .getSharedPreferences("applyId", Context.MODE_PRIVATE);
        Log.i("提取","缓存"+preferences.getString("applyId",""));

        return preferences.getString("applyId","");
    }

    public static void clearLimit(){
        SharedPreferences join = HereApplication.getContext().getSharedPreferences("join", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = join.edit();
        editor.clear();
        if (!editor.commit()){
            Toast.makeText(HereApplication.getContext(), "清除缓存失败", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean isTimeAfterCurrent(){
        SharedPreferences preferences = HereApplication.getContext()
                .getSharedPreferences("join", Context.MODE_PRIVATE);
        String time = preferences.getString("time", "");
        if (!TextUtils.isEmpty(time)) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            Log.i("TAG","时间"+year+"-"+month+" "+day+" "+hour+" "+minute+" "+time);
            if (Integer.parseInt(time.split("-")[0]) == year
                    && Integer.parseInt(time.split("-")[1]) == month
                    && Integer.parseInt(time.split("-")[2]) == day) {
                if (hour < Integer.parseInt(time.split("-")[3].split(":")[0])
                        || Integer.parseInt(time.split("-")[3].split(":")[0])
                        == hour && minute <= Integer.parseInt(time.split("-")[3]
                        .split(":")[1])) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isApply(){
        SharedPreferences preferences = HereApplication.getContext()
                .getSharedPreferences("join", Context.MODE_PRIVATE);
        return preferences.getBoolean("isApply",false);
    }

    public static String getTime(){
        SharedPreferences preferences = HereApplication.getContext()
                .getSharedPreferences("join", Context.MODE_PRIVATE);
        return preferences.getString("time","");
    }

    public static void uploadNewJoin(Join join , final UserUtil.OnDealListener listener){
        join.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    listener.success();
                }else{
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
     * 根据当前的活动查询其参与者信息
     * @param imActivity   当前即时活动
     * @param listener     监听
     */
    public static void queryJoinUser(ImActivity imActivity , final OnSearchJoinListener listener){
        BmobQuery<Join> query = new BmobQuery<>();
        query.addWhereEqualTo("imActivity",imActivity);
        query.include("joinUser");
        query.include("imActivity");
        query.findObjects(new FindListener<Join>() {
            @Override
            public void done(List<Join> list, BmobException e) {
                if (e == null){
                    listener.success(list);
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


}
