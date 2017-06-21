package ivonhoe.android.fancydrawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * Created by ivonhoe on 15-8-25.
 */
public class YouZanDrawable extends AtomDrawable {

    private static final int RECT_SIZE = 40;
    private static final double PI = 180f * Math.PI;
    ShapeDrawable mRectDrawable;
    Interpolator mInterpolator;
    Interpolator mLinearInterpolator;

    public YouZanDrawable() {
        mRectDrawable = new ShapeDrawable(new RectShape());
        mRectDrawable.setBounds(0, 0, RECT_SIZE, RECT_SIZE);
        mRectDrawable.getPaint().setColor(Color.BLUE);

        mLinearInterpolator = new LinearInterpolator();

        // 过（0,0），（0.5,1），（1,0）的一元二次方程
        mInterpolator = new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                return -4 * input * input + 4 * input;
            }
        };
        AtomStyle style = new AtomStyle();
        style.setDelay(0);
        style.setDuration(1000);
        style.setSectionCount(4);
        style.setShape(AtomStyle.Shape.DRAWABLE);
        style.setDrawable(mRectDrawable);
        //style.setInterpolator(null);
        initialize(style);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        // 将绘制原点平移到中心
        canvas.translate(mBounds.width() / 2, mBounds.height() / 2);
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    public ObjectAnimator[] getAtomAnimator(Atom atom, Rect bound) {
        ObjectAnimator[] result = new ObjectAnimator[2];
        float centerHeight = (float) Math.ceil(getIntrinsicHeight() / 2);
        float diagonal = RECT_SIZE * 1.414f;
        float animationDistance = centerHeight - diagonal;
        switch (atom.getId()) {
            case 0:
                result[0] = ObjectAnimator.ofFloat(atom, "locationY", 1f, animationDistance - 30);
                break;
            case 1:
                atom.setLocationY(-diagonal / 2);
                result[0] = ObjectAnimator
                        .ofFloat(atom, "locationX", -diagonal / 2 - 1,
                                -(animationDistance + diagonal / 2 - 30));
                break;
            case 2:
                atom.setLocationX(0);
                result[0] = ObjectAnimator
                        .ofFloat(atom, "locationY", -diagonal - 1, -(centerHeight - 30));
                break;
            case 3:
                atom.setLocationY(-diagonal / 2);
                result[0] = ObjectAnimator
                        .ofFloat(atom, "locationX", diagonal / 2 + 1,
                                centerHeight - diagonal / 2 - 30);
                break;
            default:
                throw new RuntimeException();
        }

        result[0].setInterpolator(mInterpolator);
        int start = 45 + atom.getId() * 90;
        int end = start + 180;
        result[1] = ObjectAnimator.ofInt(atom, "rotate", start, end);
        result[1].setInterpolator(mLinearInterpolator);
        return result;
    }

    @Override
    public boolean isAtomRunning(Atom atom) {
        return true;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (RECT_SIZE * 1.414f * 4);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (RECT_SIZE * 1.414f * 4);
    }

    /**
     * 在以四个矩形的中心为原点的坐标系里绘制Rect
     */
    private void drawRect(Canvas canvas, Drawable drawable, int degree, float x, float y) {
        canvas.save();
        canvas.translate(
                (float) (x + (1 - 1.414f * Math.sin((45 - degree) / 180f * Math.PI)) * 20f),
                (float) (y - (1.414f * Math.cos((45 - degree) / 180f * Math.PI) - 1) * 20f));
        canvas.rotate(degree);
        drawable.draw(canvas);
        canvas.restore();
    }
}
