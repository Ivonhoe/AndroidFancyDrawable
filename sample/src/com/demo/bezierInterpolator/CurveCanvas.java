package com.demo.bezierInterpolator;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.ivonhoe.interpolator.CurveInterpolator;
import com.ivonhoe.interpolator.CurveInterpolator.CurveListener;

import java.util.LinkedList;

/**
 * Created by Ivonhoe on 2014/8/4.
 */
public class CurveCanvas extends View implements CurveListener{

    private Paint mPaint;
    private Path mDistancePath;
    private Path mVelocitySamplesPath;
    private float mCoordinateSize;
    private float mCoordinateOffset;
    private Canvas mCanvas;

    private LinkedList<Point> mDistance = new LinkedList<Point>();

    public CurveCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mDistancePath = new Path();
        mVelocitySamplesPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCoordinateSize = getMeasuredWidth() * 0.9f;
        mCoordinateOffset = getMeasuredWidth() * 0.05f;
    }

/*    @Override
    public void drawControlNails(VelocityCurve velocityCurve) {
        clear();
        Curve curve = velocityCurve.getCurve();
        for (int i = 0; i < curve.getControlCount(); i++) {
            float x = curve.getControl(i).x * mCoordinateSize + mCoordinateOffset;
            float y = curve.getControl(i).y * mCoordinateSize + mCoordinateOffset;
            //YLog.d("drawControlNails,x=" + x + ",y=" + y);
            mCanvas.drawCircle(getX(curve.getControl(i).x), getY(curve.getControl(i).y), 3, mPaint);
        }
        invalidate();
    }

    @Override
    public void drawVelocitySamples(float input, VelocityCurve velocityCurve) {
        mPaint.setColor(Color.GREEN);
        if (input == 0) {
            mVelocitySamplesPath.reset();
            mVelocitySamplesPath.moveTo(getX(input), getY(velocityCurve.getVelocity(input)));
        }

        mVelocitySamplesPath.lineTo(getX(input), getY(velocityCurve.getVelocity(input)));
        mCanvas.drawPath(mVelocitySamplesPath, mPaint);
    }*/

    public void drawDistance(float input, float result) {
        // clear();
        mPaint.setColor(Color.RED);
        float x = getX(input);
        float y = getY(result);
        if (input == 0f) {
            mDistancePath.reset();
            mDistancePath.moveTo(x, y);
        }
        mDistancePath.lineTo(x, y);
        mCanvas.drawPath(mDistancePath, mPaint);
        invalidate();
    }

    private void clear() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mDistance.clear();
        mPaint.setColor(Color.GREEN);
    }

    private float getX(float x) {
        float result = x * mCoordinateSize + mCoordinateOffset;
        return result;
    }

    private float getY(float y) {
        float result = y * mCoordinateSize + mCoordinateOffset;
        return mCoordinateSize + 2 * mCoordinateOffset - result;
    }
}
