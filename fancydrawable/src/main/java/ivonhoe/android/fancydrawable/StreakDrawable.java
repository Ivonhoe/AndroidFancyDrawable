package ivonhoe.android.fancydrawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by ivonhoe on 14-12-15.
 * Based on http://antoine-merle.com/blog/2013/11/12/make-your-progressbar-more-smooth/
 */
public class StreakDrawable extends FancyDrawable {

    private static final float OFFSET_PER_FRAME = 0.01f;

    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mSectionsCount = 6;
    private int mSeparatorWidth = 10;
    private int mStrokeWidth = 9;
    private float mSpeed = 0.5f;
    private float mCurrentOffset;
    private float mMaxOffset = 1f / mSectionsCount;

    private float xSectionWidth = 1f / mSectionsCount;
    private int boundWidth;
    private int boundCenterY;

    public StreakDrawable() {
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(false);
        mPaint.setAntiAlias(false);
        mPaint.setColor(Color.BLUE);

        setBackground(new ColorDrawable(0xFFFFF));
    }

    @Override
    public void onDraw(Canvas canvas) {
        boundWidth = mBounds.width();
        boundCenterY = mBounds.centerY();

        drawStrokes(canvas);
    }

    @Override
    public void onFrame() {
        mCurrentOffset += (OFFSET_PER_FRAME * mSpeed);

        if (mCurrentOffset >= mMaxOffset) {
            mCurrentOffset -= mMaxOffset;
        }
    }

    private void drawStrokes(Canvas canvas) {
        int width = mBounds.width() + mSeparatorWidth;
        int prev;
        int end;
        int spaceLength;
        for (int i = 0; i <= mSectionsCount; i++) {
            float xOffset = i * xSectionWidth + mCurrentOffset;
            xOffset = Math.max(0f, xOffset - xSectionWidth);
            prev = (int) (mInterpolator.getInterpolation(xOffset) * width);

            float ratioSectionWidth =
                    Math.abs(mInterpolator.getInterpolation(xOffset) -
                            mInterpolator.getInterpolation(Math.min(xOffset + xSectionWidth, 1f)));

            //separator between each piece of line
            int sectionWidth = (int) (width * ratioSectionWidth);
            if (sectionWidth + prev < width)
                spaceLength = Math.min(sectionWidth, mSeparatorWidth);
            else {
                spaceLength = 0;

            }

            int drawLength = sectionWidth > spaceLength ? sectionWidth - spaceLength : 0;
            end = prev + drawLength;

            if (end > prev) {
                canvas.drawLine(prev, boundCenterY, end, boundCenterY, mPaint);
            }
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

}
