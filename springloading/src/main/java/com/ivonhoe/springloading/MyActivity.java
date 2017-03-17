package com.ivonhoe.springloading;

import android.animation.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.ivonhoe.interpolator.BezierInterpolator;
import com.ivonhoe.interpolator.CurveInterpolator;

public class MyActivity extends Activity {

    private ImageView mImageView;
    private ImageView mContrastView;
    private CurveCanvas mCurveCanvas;
    private Interpolator mBezierInterpolator;
    private LinearInterpolator mLinearInterpolator;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupView();
    }

    private void setupView() {
        mImageView = (ImageView) findViewById(R.id.image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveImage();
            }
        });
        mContrastView = (ImageView) findViewById(R.id.image_contrast);
        mContrastView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveImage();
            }
        });
        mCurveCanvas = (CurveCanvas) findViewById(R.id.canvas);
    }

    /**
     * 通过监听interpolator的进度重绘动画曲线
     */
    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

            Interpolator interpolator = (Interpolator) animation.getInterpolator();
            if (interpolator instanceof CurveInterpolator) {
                float input = ((CurveInterpolator) interpolator).getAnimatedTime();
                float result = animation.getAnimatedFraction();

                mCurveCanvas.drawDistance(input, result);
            }
        }
    };

    private void moveImage() {
        if (mBezierInterpolator == null) {
            // window phone 进度条的插值器
            mBezierInterpolator = new BezierInterpolator(0.03f, 0.615f, 0.995f, 0.415f);
        }
        if (mLinearInterpolator == null)
            mLinearInterpolator = new LinearInterpolator();

        View parent = (View) mImageView.getParent();
        int x = mImageView.getTranslationX() == 0 ? (parent.getWidth() - mImageView.getWidth()) : 0;
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", x);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mImageView, translationX);
        animator.setInterpolator(mBezierInterpolator);
        animator.addUpdateListener(updateListener);

        ObjectAnimator animatorContrast = ObjectAnimator
                .ofPropertyValuesHolder(mContrastView, translationX);
        animatorContrast.setInterpolator(mLinearInterpolator);
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.playTogether(animator, animatorContrast);
        animationSet.setDuration(2000);
        animationSet.start();
    }
}
