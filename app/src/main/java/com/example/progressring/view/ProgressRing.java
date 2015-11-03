package com.example.progressring.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.example.progressring.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by danny on 15-11-2.
 */
public class ProgressRing extends View {

    private static final int DEFAULT_RADIUS = 300;
    private static final int DEFAULT_CIRCLE_WIDTH = 20;
    private static final int DEFAULT_SPEED = 80;

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private int mSpeed;

    private int mWidth;
    private int mHeight;

    private Paint mFirstPaint;
    private Paint mSecondPaint;

    private Handler mHandler;

    private float mRadius;
    private int mProgress = 0;

    private ScheduledExecutorService mExec;

    public ProgressRing(Context context) {
        this(context, null);
    }

    public ProgressRing(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressRing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressRing);

        try {
            mFirstColor = a.getColor(R.styleable.ProgressRing_firstColor, Color.BLUE);
            mSecondColor = a.getColor(R.styleable.ProgressRing_secondColor, Color.RED);
            mCircleWidth = a.getDimensionPixelSize(R.styleable.ProgressRing_circleWidth,
                    DEFAULT_CIRCLE_WIDTH);
            mSpeed = a.getInt(R.styleable.ProgressRing_speed, DEFAULT_SPEED);
        } finally {
            a.recycle();
        }

        mFirstPaint = makePaint(mFirstColor, mCircleWidth);
        mSecondPaint = makePaint(mSecondColor, mCircleWidth);


        mExec = Executors.newSingleThreadScheduledExecutor();
        mExec.scheduleWithFixedDelay(new Runnable() {

            int i = 0;

            @Override
            public void run() {
                synchronized (ProgressRing.this) {
                    mProgress++;

                    if (mProgress == 360) {
                        int tmp = mFirstColor;
                        mFirstColor = mSecondColor;
                        mSecondColor = tmp;

                        mFirstPaint.setColor(mFirstColor);
                        mSecondPaint.setColor(mSecondColor);

                        mProgress = 0;
                    }
                }
                postInvalidate();
            }
        }, 1000, mSpeed, TimeUnit.MILLISECONDS);

        setDebug(true);
    }

    private Paint makePaint(int color, int stroke) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(color);
        p.setStrokeWidth(stroke);
        p.setAntiAlias(true);
        return p;
    }

    public void setDebug(boolean debugged) {
        if (debugged) {
            setBackgroundColor(Color.CYAN);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
//
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//        if (widthMode == MeasureSpec.EXACTLY) {
//            mWidth = measuredWidth;
//        } else {
//            mWidth = getPaddingLeft() + DEFAULT_RADIUS + getPaddingRight();
//        }
//
//        System.out.println(MeasureModeTeller.tellMode(widthMode));
//        System.out.println(MeasureModeTeller.tellMode(heightMode));
//
//        if (heightMode == MeasureSpec.EXACTLY) {
//            mHeight = measuredHeight;
//        } else {
//            mHeight = getPaddingBottom() + DEFAULT_RADIUS + getPaddingTop();
//        }
//
//        mWidth = mHeight = Math.min(mWidth, mHeight);
//        setMeasuredDimension(mWidth, mHeight);
//    }

    @Override
    protected void onDraw(final Canvas canvas) {
        float halfStrokeWidth = mCircleWidth / 2;
        mRadius = Math.min(getWidth(), getHeight()) / 2 - halfStrokeWidth;

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mFirstPaint);


        final RectF circleRectF = new RectF(halfStrokeWidth, halfStrokeWidth, getWidth() -
                halfStrokeWidth, getHeight() - halfStrokeWidth);

        canvas.drawArc(circleRectF, -90, mProgress, false, mSecondPaint);
    }
}
