package ivonhoe.android.fancydrawable;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.animation.LinearInterpolator;

/**
 * Created by ivonhoe on 15-4-30.
 */
public class BallsLineDrawable extends AtomDrawable {

    public BallsLineDrawable() {
        AtomStyle atomStyle = new AtomStyle();
        atomStyle.setShape(AtomStyle.Shape.POINT);
        atomStyle.setInterpolator(DEFAULT_INTERPOLATOR);
        atomStyle.setDuration(4000);
        atomStyle.setDelay(200);
        atomStyle.setSectionCount(6);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);
        paint.setDither(false);
        paint.setAntiAlias(false);
        paint.setColor(Color.BLUE);
        atomStyle.setPaint(paint);
        initialize(atomStyle);
    }

    @Override
    public ObjectAnimator[] getAtomAnimator(Atom atom, Rect bound) {
        if (atom == null || bound == null)
            return null;
        AtomStyle style = getAtomStyle();
        Paint paint = style.getPaint();
        AtomStyle.Shape shape = style.getShape();
        if (shape == AtomStyle.Shape.CHAR) {
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = mBounds.top +
                    (mBounds.bottom - mBounds.top - fontMetrics.bottom + fontMetrics.top) / 2 -
                    fontMetrics.top;
            paint.setTextAlign(Paint.Align.CENTER);
            atom.setLocationY(baseline);
        } else {
            atom.setLocationY(bound.centerY());
        }

        ObjectAnimator[] result = new ObjectAnimator[1];
        result[0] = ObjectAnimator.ofFloat(atom, "locationX", 0, bound.width());
        return result;
    }

    @Override
    public boolean isAtomRunning(Atom atom) {
        return atom.getLocationX() > 0 && atom.getLocationX() < mBounds.width();
    }
}
