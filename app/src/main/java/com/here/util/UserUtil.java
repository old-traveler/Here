package com.here.util;

import android.text.TextUtils;
import android.util.Log;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.ImageAddress;
import com.here.bean.User;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by hyc on 2017/6/23 13:57
 */

public class UserUtil {

    public interface OnDealListener {
        void success();

        void fail(String error);
    }

    public interface OnImageDealListener {
        void success(String path);

        void fail(String error);
    }

    public interface OnSearchUserListener{
        void success(User user);
        void fail(String error);
    }



    /**
     * 注册
     * @param user 注册的User
     * @param listener 注册监听
     */
    public static void register(User user, final OnDealListener listener) {
        user.signUp(new SaveListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    listener.success();
                } else {
                    Log.i("测试",e.getMessage());
                    if (e.getErrorCode()==202){
                        listener.fail(HereApplication.getContext().getString(R.string.err_register_exist));
                    }else if(e.getErrorCode()==9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else{
                        listener.fail(e.getMessage());
                    }

                }
            }
        });
    }


    /**
     * 登录
     * @param user 登录的User
     * @param listener 登录监听
     */
    public static void login(User user, final OnDealListener listener) {
        user.login(new SaveListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    listener.success();
                } else {
                    if (e.getErrorCode()==9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(HereApplication.getContext().getString(R.string.err_login_fail));
                    }

                }
            }
        });
    }


    /**
     * 更新当前用户的信息
     * @param newUser      需要更新信息的用户对象
     * @param listener     更新监听
     */
    public static void updateUserInfo(User newUser, final OnDealListener listener) {
        User user = BmobUser.getCurrentUser(User.class);
        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.success();
                } else {
                    if (e.getErrorCode()==9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else if(e.getErrorCode() == 301) {
                        listener.fail(HereApplication.getContext().getString(R.string.email_error));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }


    /**
     * 绑定用户邮箱
     * @param newUser    用户
     * @param listener   监听
     */
    public static void bindEmail(User newUser, final OnDealListener listener){
        newUser.requestEmailVerify(newUser.getEmail(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    listener.success();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else if(e.getErrorCode() == 301){
                        listener.fail(HereApplication.getContext().getString(R.string.email_error));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }


    /**
     * 发送短信验证码
     * @param number    手机号码
     * @param listener  发送监听
     */
    public static void verifyPhoneNumber(String number  , final OnDealListener listener){
        BmobSMS.requestSMSCode(number, "【这儿】 您的验证码是`%smscode%`，有效期为`%ttl%`分钟。若非您本人操作，请勿略此短信", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e==null){
                    listener.success();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else if (e.getErrorCode() == 301){
                        listener.fail(HereApplication.getContext().getString(R.string.number_error));
                    }else if(e.getErrorCode() == 209){
                        listener.fail(HereApplication.getContext().getString(R.string.number_exist));
                    }else {
                        listener.fail(e.getMessage()+e.getErrorCode());
                    }
                }
            }
        });

    }

    /**
     * 验证短信验证码是否正确
     * @param number    手机号码
     * @param code      验证码
     * @param listener  验证监听
     */
    public static void verifyCode(String number, String code, final OnDealListener listener){
        BmobSMS.verifySmsCode(number,code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    listener.success();
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
     * 更新用户账号的密码
     * @param oldPsd  原密码
     * @param newPsd  新密码
     * @param listener  更新监听
     */
    public static void updatePassword(String oldPsd , String newPsd, final OnDealListener listener){
        BmobUser.getCurrentUser(User.class).updateCurrentUserPassword(oldPsd, newPsd, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    listener.success();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else if (e.getErrorCode() == 210){
                        listener.fail(HereApplication.getContext().getString(R.string.update_password_fail));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 更新当前用户的头像图片
     * @param path 图片储存路径
     * @param listener 更新监听
     */
    public static void updateUserHeadImage(String path, final OnImageDealListener listener) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {

                    User newUser = new User();
                    newUser.setHeadImageUrl(bmobFile.getUrl());
                    User user = BmobUser.getCurrentUser(User.class);
                    newUser.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                listener.success(bmobFile.getFileUrl());
                            } else {
                                if (e.getErrorCode() == 9016){
                                    listener.fail(HereApplication.getContext()
                                            .getString(R.string.err_no_net));
                                }else {
                                    listener.fail(e.getMessage());
                                }
                            }
                        }
                    });
                } else {
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
     * 更新当前用户的背景图片
     * @param path 图片储存路径
     * @param listener 更新监听
     */
    public static void updateUserBacgound(String path, final OnImageDealListener listener) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {

                    User newUser = new User();
                    newUser.setBackgroundUrl(bmobFile.getUrl());
                    User user = BmobUser.getCurrentUser(User.class);
                    newUser.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                listener.success(bmobFile.getFileUrl());
                            } else {
                                if (e.getErrorCode() == 9016){
                                    listener.fail(HereApplication.getContext()
                                            .getString(R.string.err_no_net));
                                }else {
                                    listener.fail(e.getMessage());
                                }
                            }
                        }
                    });
                } else {
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
     * 通过用户名查询用户信息
     * @param username  用户名
     * @param listener  查询监听
     */
    public static void searchUserInfo(String username, final OnSearchUserListener listener){
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if(e==null &&object.size()>0){
                    listener.success(object.get(0));
                }else if (e == null){
                    listener.fail("查无此人");
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


    public static void searchUserInfoById(String objectId , final OnSearchUserListener listener){
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(objectId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    listener.success(user);
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
