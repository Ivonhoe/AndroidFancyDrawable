package ivonhoe.android.fancydrawable;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.*;
import android.view.animation.*;
import android.view.animation.Interpolator;

/**
 * @auther Ivonhoe
 */
public class NetEaseDrawable extends AtomDrawable {

    private Paint mPaint;
    private RectF mRectF;
    private int mPadding;
    private int mRectLeft;
    private int mRectTop;
    private int mRectRight;
    private int mRectBottom;

    /**
     * dp
     */
    private int mSize;

    private float mScaleFactor;

    /**
     * 画笔宽度的插值
     */
    private Interpolator mPaintInterpolator;

    /**
     * 圆弧旋转的产值
     */
    private Interpolator mRotateInterpolator;

    public NetEaseDrawable(Context context) {
        mDensity = context.getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mRectF = new RectF();
        mPadding = (int) (4 * mDensity + 0.5f);
        mSize = 40;
        mScaleFactor = 0.11f;

        // 过（0,0），（0.5,1），（1,0）的一元二次方程
        mPaintInterpolator = new android.view.animation.Interpolator() {
            @Override
            public float getInterpolation(float input) {
                return -4 * input * input + 4 * input;
            }
        };
        mRotateInterpolator = new LinearInterpolator();

        AtomStyle style = new AtomStyle();
        style.setDelay(0);
        style.setDuration(1500);
        style.setSectionCount(3);
        style.setShape(AtomStyle.Shape.DRAWABLE);
        style.setInterpolator(null);
        style.setPaint(mPaint);
        initialize(style);
    }

    @Override
    protected void drawDrawable(Canvas canvas, AtomStyle style, Atom atom) {
        Paint paint = style.getPaint();
        paint.setStrokeWidth(atom.getDelta());
        switch (atom.getId()) {
            case 0:
                paint.setColor(Color.RED);
                break;
            case 1:
                paint.setColor(Color.BLUE);
                break;
            case 2:
                paint.setColor(Color.GRAY);
                break;
            default:
        }
        mRectF.set(mRectLeft + atom.getLocationX(), mRectTop + atom.getLocationX(),
                mRectRight - atom.getLocationX(),
                mRectBottom - atom.getLocationX());
        canvas.drawArc(mRectF, atom.getRotate(), atom.getLength(), false, paint);//小弧形
    }

    @Override
    public ObjectAnimator[] getAtomAnimator(Atom atom, Rect bound) {
        ObjectAnimator[] result = new ObjectAnimator[4];
        result[0] = ObjectAnimator.ofFloat(atom, "delta", 4f, 9f);
        result[0].setInterpolator(mPaintInterpolator);
        switch (atom.getId()) {
            case 0:
                result[1] = ObjectAnimator.ofInt(atom, "rotate", 0, 360);
                break;
            case 1:
                result[1] = ObjectAnimator.ofInt(atom, "rotate", 120, 480);
                break;
            case 2:
                result[1] = ObjectAnimator.ofInt(atom, "rotate", 240, 600);
                break;
            default:
                throw new RuntimeException();
        }
        result[1].setInterpolator(mRotateInterpolator);
        result[2] = ObjectAnimator.ofFloat(atom, "length", 80f, 59f);
        result[2].setInterpolator(mPaintInterpolator);
        /**
         * 并不是location，改变绘制圆弧的半径
         * */
        result[3] = ObjectAnimator
                .ofFloat(atom, "locationX", 0, mScaleFactor * getIntrinsicWidth());
        result[3].setInterpolator(mPaintInterpolator);
        return result;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        int height = mBounds.height();
        mRectLeft = mBounds.centerX() - height / 2 + mPadding;
        mRectTop = mPadding;
        mRectRight = mBounds.centerX() + height / 2 - mPadding;
        mRectBottom = height - mPadding;
    }

    @Override
    public boolean isAtomRunning(Atom atom) {
        return true;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (mSize * mDensity + 0.5f);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (mSize * mDensity + 0.5f);
    }
}
