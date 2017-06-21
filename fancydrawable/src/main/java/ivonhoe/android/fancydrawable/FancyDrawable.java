package ivonhoe.android.fancydrawable;

import android.annotation.TargetApi;
import android.graphics.*;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by ivonhoe on 14-12-18.
 * Based on http://cyrilmottier.com/2012/11/27/actionbar-on-the-move/
 */
@TargetApi(Build.VERSION_CODES.DONUT)
public abstract class FancyDrawable extends Drawable implements Animatable {

    protected static final long FRAME_DURATION = 1000 / 60;

    protected boolean mIsRunning;
    protected Paint mPaint = new Paint();

    protected Rect mBounds;
    protected Drawable mBackground;

    protected Runnable mUpdater = new Runnable() {
        @Override
        public void run() {
            onFrame();

            scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
            invalidateSelf();
        }
    };

    @Override
    public void draw(Canvas canvas) {
        canvas.clipRect(mBounds);

        if (mBackground != null) {
            mBackground.setBounds(mBounds);
            mBackground.draw(canvas);
        }

        if (isRunning()) {
            onDraw(canvas);
        }
    }

    @Override
    public void start() {
        if (!isRunning()) {
            mIsRunning = true;

            // start
            onStart();
            scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
            invalidateSelf();
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            mIsRunning = false;
            unscheduleSelf(mUpdater);

            //stop
            onStop();
        }
    }

    @Override
    public boolean isRunning() {
        return mIsRunning;
    }

    protected void onStart() {
    }

    protected void onStop() {
    }

    protected void onFrame() {
    }

    public abstract void onDraw(Canvas canvas);

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (mBounds == null) {
            mBounds = new Rect();
        }
        mBounds.set(bounds);
    }

    public void setBackground(Drawable background) {
        this.mBackground = background;
    }

    public void setBackgroundColor(int color) {
        this.mBackground = new ColorDrawable(color);
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }
}
