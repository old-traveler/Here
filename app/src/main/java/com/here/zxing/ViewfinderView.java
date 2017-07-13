package com.here.zxing;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.here.R;

import java.util.ArrayList;
import java.util.List;


public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192,
            128, 64};
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final long ANIMATION_DELAY = 80L;
    private CameraManager cameraManager;
    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private int scannerAlpha;
    private List<ResultPoint> possibleResultPoints;
    private int i = 0;// ��ӵ�
    private Rect mRect;// ɨ�������߽�
    private GradientDrawable mDrawable;// ���ý���ͼ��Ϊɨ����
    private Drawable lineDrawable;// ����ͼƬ��Ϊɨ����

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);

        // GradientDrawable��lineDrawable
        mRect = new Rect();
        int left = getResources().getColor(R.color.lightgreen);
        int center = getResources().getColor(R.color.green);
        int right = getResources().getColor(R.color.lightgreen);
        lineDrawable = getResources().getDrawable(R.drawable.zx_code_line);
        mDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, new int[]{left,
                left, center, right, right});

        scannerAlpha = 0;
        possibleResultPoints = new ArrayList<ResultPoint>(5);
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return;
        }

        Rect frame = cameraManager.getFramingRect();
        if (frame == null) {
            return;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // ��ɨ����ⲿ�İ�ɫ����
        // �����ɰ���ɫ
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        // ͷ��
        canvas.drawRect(0, 0, width, frame.top, paint);
        // ���
        canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
        // �ұ�
        canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint);
        // �ײ�
        canvas.drawRect(0, frame.bottom, width, height, paint);

        if (resultBitmap != null) {
            // ��ɨ����л���Ԥ��ͼ
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {
            // �����ĸ���
            paint.setColor(getResources().getColor(R.color.green));
            // ���Ͻ�
            canvas.drawRect(frame.left, frame.top, frame.left + 15,
                    frame.top + 5, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + 5,
                    frame.top + 15, paint);
            // ���Ͻ�
            canvas.drawRect(frame.right - 15, frame.top, frame.right,
                    frame.top + 5, paint);
            canvas.drawRect(frame.right - 5, frame.top, frame.right,
                    frame.top + 15, paint);
            // ���½�
            canvas.drawRect(frame.left, frame.bottom - 5, frame.left + 15,
                    frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - 15, frame.left + 5,
                    frame.bottom, paint);
            // ���½�
            canvas.drawRect(frame.right - 15, frame.bottom - 5, frame.right,
                    frame.bottom, paint);
            canvas.drawRect(frame.right - 5, frame.bottom - 15, frame.right,
                    frame.bottom, paint);

            // ��ɨ����л���ģ��ɨ�������
            // ����ɨ��������ɫΪ��ɫ
            paint.setColor(getResources().getColor(R.color.green));
            // ������ɫ������͸��ֵ
            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            // ͸���ȱ仯
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;

            // �����̶����в�������
            // int middle = frame.height() / 2 + frame.top;
            // canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1,
            // middle + 2, paint);

            // ��ɨ�����޸�Ϊ�����ߵ���
            if ((i += 5) < frame.bottom - frame.top) {
                /* ����Ϊ�ý���������Ϊɨ���� */
                // ����ͼΪ����
                // mDrawable.setShape(GradientDrawable.RECTANGLE);
                // ����ͼΪ����
                // mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                // ���;��ε��ĸ�Բ�ǰ뾶
                // mDrawable
                // .setCornerRadii(new float[] { 8, 8, 8, 8, 8, 8, 8, 8 });
                // λ�ñ߽�
                // mRect.set(frame.left + 10, frame.top + i, frame.right - 10,
                // frame.top + 1 + i);
                // ���ý���ͼ���߽�
                // mDrawable.setBounds(mRect);
                // ������������
                // mDrawable.draw(canvas);

				/* ����ΪͼƬ��Ϊɨ���� */
                mRect.set(frame.left - 6, frame.top + i - 6, frame.right + 6,
                        frame.top + 6 + i);
                lineDrawable.setBounds(mRect);
                lineDrawable.draw(canvas);

                // ˢ��
                invalidate();
            } else {
                i = 0;
            }

            // �ظ�ִ��ɨ����������(���ĸ��Ǽ�ɨ����)
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }

    public void recycleLineDrawable() {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
        }
        if (lineDrawable != null) {
            lineDrawable.setCallback(null);
        }
    }
}
