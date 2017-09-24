package com.here.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.here.HereApplication;
import com.here.R;
import com.here.bean.ImActivity;

/**
 * Created by hyc on 2017/6/19 21:35
 */

public class BitmapUtil {

    public interface OnGetMapHeadListener{
        void success(Bitmap bitmap,ImActivity imActivity);
        void fail(ImActivity imActivity);
    }

    public static Bitmap createCircleImage(Bitmap source, Context context, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawBitmap(source,0,0,paint);
        source= Bitmap.createScaledBitmap(source,min,min,true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.blue,null),min,min,true), 0, 0, paint);
        source.recycle();

        return target;
    }


    public static void drawMark(final ImActivity imActivity, final int min
            , final boolean isWhite, final OnGetMapHeadListener listener) {

        Glide.with(HereApplication.getContext()).load(imActivity.getPublisher()
                .getHeadImageUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null){
                    final Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(target);
                    canvas.drawCircle(min / 2, min / 2, min / 2, paint);
                    resource=Bitmap.createScaledBitmap(resource,min,min,true);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(resource, 0, 0, paint);
                    resource.recycle();
                    listener.success(overlying(target,isWhite),imActivity);

                }else {
                    listener.fail(imActivity);
                }

            }
        });
    }

    public static Bitmap drawMark(Bitmap resource, final int min,boolean isWhite) {

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        resource=Bitmap.createScaledBitmap(resource,min,min,true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(resource, 0, 0, paint);
        resource.recycle();
        return overlying(target,isWhite);

    }

    public static Bitmap overlying(Bitmap source,boolean isWhite){
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap image = null;
        if (isWhite){
            image = BitmapFactory.decodeResource(HereApplication.getContext()
                    .getResources(),R.drawable.white_map_head).copy(Bitmap.Config.ARGB_8888,true);
        }else {
            image = BitmapFactory.decodeResource(HereApplication.getContext()
                    .getResources(),R.drawable.bule_map_head).copy(Bitmap.Config.ARGB_8888,true);
        }
        image=Bitmap.createScaledBitmap(image,200,200,true);
        Canvas canvas = new Canvas(image);
        canvas.drawBitmap(source,28,13,paint);
        source.recycle();
        return image;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,(int) height, matrix, true);
        bgimage.recycle();
        return bitmap;

    }






}
