package ivonhoe.android.fancydrawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.animation.Interpolator;

import java.util.ArrayList;

import ivonhoe.android.bezierinterpolator.BezierInterpolator;

/**
 * @author Ivonhoe on 2014/12/24.
 */
public abstract class AtomDrawable extends FancyDrawable {

    protected Interpolator DEFAULT_INTERPOLATOR = new BezierInterpolator(0.03f, 0.615f, 0.995f,
            0.415f);
    protected AnimatorSet mAtomsAnimatorSet;
    protected Atom[] mAtoms;
    protected AtomStyle mAtomStyle;
    protected float mDensity;

    public AtomDrawable() {
        AtomStyle style = new AtomStyle();
        initialize(style);
    }

    public AtomDrawable(AtomStyle style) {
        initialize(style);
    }

    protected void initialize(AtomStyle style) {
        mAtomStyle = style;
        int atomCount = mAtomStyle.getSectionCount();
        mAtoms = new Atom[atomCount];
        for (int i = 0; i < atomCount; i++) {
            mAtoms[i] = new Atom(i);
        }
        mAtomsAnimatorSet = null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStart() {
        if (mBounds != null) {
            setupAnimators(mBounds);
            mAtomsAnimatorSet.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupAnimators(final Rect bound) {
        if (mAtomStyle != null && bound != null) {
            mAtomStyle.setParentBound(bound);
            getAtomsAnimation(mAtomStyle, mAtoms);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < mAtomStyle.getSectionCount(); i++) {
            drawAtom(canvas, mAtomStyle, mAtoms[i]);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        recreateAnimator();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void recreateAnimator() {
        if (isRunning()) {
            if (mAtomsAnimatorSet != null) {
                mAtomsAnimatorSet.cancel();
            }
            mAtomsAnimatorSet = null;
            onStart();
        } else {
            mAtomsAnimatorSet = null;
            if (mBounds != null)
                setupAnimators(mBounds);
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mAtomStyle.setInterpolator(interpolator);
    }

    /**
     *
     */
    public void setAtomText(String text) {
        mAtomStyle.setText(text);
        // recreate atom list
        if (text != null && text.length() != mAtoms.length) {
            mAtoms = new Atom[text.length()];
            for (int i = 0; i < mAtoms.length; i++) {
                mAtoms[i] = new Atom(i);
            }
        }
        recreateAnimator();
    }

    public void setAtomTextSize(float size) {
        mAtomStyle.getPaint().setTextSize(size);
    }

    public void setPaint(Paint paint) {
        mAtomStyle.setPaint(paint);
    }

    public AtomStyle getAtomStyle() {
        return mAtomStyle;
    }

    /**
     * 一个元素如何动画
     */
    public abstract ObjectAnimator[] getAtomAnimator(final Atom atom, final Rect bound);

    /**
     * 一个元素是否在动画中
     */
    public abstract boolean isAtomRunning(Atom atom);

    @Override
    public int getIntrinsicWidth() {
        return super.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return super.getIntrinsicHeight();
    }

    /**
     * 根据元素样式得到动画集合
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AnimatorSet getAtomsAnimation(AtomStyle atomStyle, Atom[] atoms) {
        int count = atomStyle.getSectionCount();
        ArrayList<Animator> temp = new ArrayList<Animator>();
        String text = atomStyle.getText();
        final Rect bound = atomStyle.getParentBound();
        if (bound == null || mAtomsAnimatorSet != null) {
            return mAtomsAnimatorSet;
        }

        mAtomsAnimatorSet = new AnimatorSet();
        for (int i = 0; i < count; i++) {
            final Atom atom = atoms[i];
            if (!TextUtils.isEmpty(text)) {
                atom.setText(text.substring(text.length() - i - 1, text.length() - i));
            }
            ObjectAnimator[] animators = getAtomAnimator(atoms[i], bound);
            for (Animator animator : animators) {
                if (animator != null) {
                    animator.setStartDelay(atomStyle.getDelay() * i);
                    temp.add(animator);
                }
            }
        }

        //Log.d("simply", "--temp:" + temp.size() + ",mAtomsAnimatorSet:" + mAtomsAnimatorSet);
        mAtomsAnimatorSet.setDuration(atomStyle.getDuration());
        mAtomsAnimatorSet.playTogether(temp);
        if (atomStyle.getInterpolator() != null) {
            mAtomsAnimatorSet.setInterpolator(atomStyle.getInterpolator());
        }
        mAtomsAnimatorSet.setStartDelay(atomStyle.getDelay());
        mAtomsAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mAtomsAnimatorSet.getDuration() > 0) {
                    mHandler.sendEmptyMessage(0);
                }
                //Log.d("simply", "+++animation :" + animation);
            }
        });
        return mAtomsAnimatorSet;
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeMessages(0);
            if (mAtomsAnimatorSet != null) {
                mAtomsAnimatorSet.start();
            }
        }
    };

    /**
     * 根据每个元素的状态和样式绘制
     */
    public void drawAtom(Canvas canvas, AtomStyle style, Atom atom) {
        if (!isAtomRunning(atom)) {
            return;
        }
        Paint paint = style.getPaint();
        AtomStyle.Shape shape = style.getShape();

        switch (shape) {
            case CHAR:
                canvas.drawText(atom.getText(), atom.getLocationX(), atom.getLocationY(), paint);
                break;

            case POINT:
                canvas.drawPoint(atom.getLocationX(), atom.getLocationY(), paint);
                break;

            case CIRCLE:
                canvas.drawCircle(atom.getLocationX(), atom.getLocationY(), 5, paint);
                break;

            case DRAWABLE:
                drawDrawable(canvas, style, atom);
                break;
        }
    }

    protected void drawDrawable(Canvas canvas, AtomStyle style, Atom atom) {
        Drawable drawable = style.getDrawable();
        int degree = atom.getRotate();
        float x = atom.getLocationX();
        float y = atom.getLocationY();

        canvas.save();
        canvas.translate(
                (float) (x + (1 - 1.414f * Math.sin((45 - degree) / 180f * Math.PI)) * 20f),
                (float) (y - (1.414f * Math.cos((45 - degree) / 180f * Math.PI) - 1) * 20f));
        canvas.rotate(degree);
        //canvas.translate((float) (20 / Math.cos((float) degree / 180 * Math.PI)), 0);
        drawable.draw(canvas);
        canvas.restore();
    }

    public static class Atom {
        private int id = -1;
        private float delta = 0;
        private float length = 0;
        private float locationX = 0;
        private float locationY = 0;
        private int rotate = 0;
        private String text;

        public Atom(int id) {
            this.id = id;
        }

        public float getLocationX() {
            return locationX;
        }

        public void setLocationX(float locationX) {
            this.locationX = locationX;
        }

        public float getLocationY() {
            return locationY;
        }

        public void setLocationY(float locationY) {
            this.locationY = locationY;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public float getDelta() {
            return delta;
        }

        public void setDelta(float delta) {
            this.delta = delta;
        }

        public int getRotate() {
            return rotate;
        }

        public void setRotate(int rotate) {
            this.rotate = rotate;
        }

        public int getId() {
            return id;
        }

        public float getLength() {
            return length;
        }

        public void setLength(float length) {
            this.length = length;
        }
    }
}
