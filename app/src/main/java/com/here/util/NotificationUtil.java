package com.here.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.here.HereApplication;
import com.here.R;

/**
 * Created by hyc on 2017/7/12 08:07
 */

public class NotificationUtil {
    public static int id = 0;

    /**
     * 新建一个通知信息
     * @param title 通知标题
     * @param content  通知内容
     */
    public static void showNewNotification(String title
            , String content,boolean isJump,Intent intent){
        NotificationManager manager = (NotificationManager) HereApplication
                .getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (isJump){
            PendingIntent pi = PendingIntent.getActivity(
                    HereApplication.getContext(),0,intent,0);
            notification = new NotificationCompat
                    .Builder(HereApplication.getContext())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.grils)
                    .setLargeIcon(BitmapFactory.decodeResource(HereApplication
                            .getContext().getResources(),R.drawable.grils))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0,1000,0,0})
                    .setLights(Color.WHITE,1000,1000)
                    .build();
        }else {
            notification = new NotificationCompat
                    .Builder(HereApplication.getContext())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.grils)
                    .setLargeIcon(BitmapFactory.decodeResource(HereApplication
                            .getContext().getResources(),R.drawable.grils))
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0,1000,0,0})
                    .setLights(Color.WHITE,1000,1000)
                    .build();
        }


        id++;
        manager.notify(id,notification);
    }


}
