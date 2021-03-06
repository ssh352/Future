package com.kunxun.future.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import com.kunxun.future.R;

public class GradientView extends View {
    private float mIndex = 0;
    private Shader mShader;
    private static final int mUpdateStep = 15;
    private static final int mMaxWidth = 40 * mUpdateStep; // 26*25
    private static final int mMinWidth = 6 * mUpdateStep;  // 5*25
    int mDefaultColor;
    int mSlideColor;
    private int mWidth, mHeight;
    private String mStringToShow;
    private Paint mTextPaint;
    private float mTextHeight;

    private static final int mSlideIconOffSetTop = 2;

    public GradientView(Context context) {
        super(context);
    }

    public GradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GradientView);
        mStringToShow = a.getString(R.styleable.GradientView_StringToShow);
        int mTextSize = (int) a.getDimension(R.styleable.GradientView_TextSize, 40);
        mDefaultColor = a.getColor(R.styleable.GradientView_TextColor, Color.GRAY);
        mSlideColor = a.getColor(R.styleable.GradientView_SlideColor, Color.WHITE);

        a.recycle();

        ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIndex = Float.parseFloat(animation.getAnimatedValue().toString());

                mShader = new LinearGradient(mIndex - 20 * mUpdateStep, 100,
                        mIndex + 500, 100, new int[]{mDefaultColor, mDefaultColor, mDefaultColor, mSlideColor,
                        mSlideColor, mDefaultColor, mDefaultColor, mDefaultColor}, null,
                        Shader.TileMode.CLAMP);
                postInvalidate();
            }
        };
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(mMinWidth, mMaxWidth);
        animator.setDuration(3000);
        animator.addUpdateListener(mAnimatorUpdateListener);
        animator.setRepeatCount(Animation.INFINITE);//repeat animation
        animator.start();


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mSlideColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mTextHeight = mTextPaint.ascent();

        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mTextPaint.setShader(mShader);
        canvas.drawText(mStringToShow, mWidth / 2, mHeight / 2 - mTextHeight
                / 2 - mSlideIconOffSetTop, mTextPaint); // slide_unlock
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

}
