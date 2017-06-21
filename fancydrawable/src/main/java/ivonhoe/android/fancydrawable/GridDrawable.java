package ivonhoe.android.fancydrawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ivonhoe on 15-4-22.
 */
public class GridDrawable extends Drawable {

    private static final int DEFAULT_PAINT_FLAGS =
            Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG;
    private static final int DEFAULT_PADDING = 5;
    private static final int DEFAULT_ITEM_SIZE = (int) (80);
    private static final int DEFAULT_COLUMN_COUNT = 3;
    private static final int DEFAULT_ROW_COUNT = 3;

    private Rect mBounds;
    private int mColumnCount = DEFAULT_COLUMN_COUNT;
    private int mRowCount = DEFAULT_ROW_COUNT;
    private ArrayList<ChildDrawable> mChildDrawable = new ArrayList<ChildDrawable>();
    private int mAlpha;
    private int mPaddingL = DEFAULT_PADDING;
    private int mPaddingT = DEFAULT_PADDING;
    private int mPaddingR = DEFAULT_PADDING;
    private int mPaddingB = DEFAULT_PADDING;
    private int hGap = -1;
    private int vGap = -1;
    private int mChildH = DEFAULT_ITEM_SIZE;
    private int mChildW = DEFAULT_ITEM_SIZE;

    private Paint mPaint = new Paint(DEFAULT_PAINT_FLAGS);

    public GridDrawable() {
        super();
        mAlpha = 255;
        mBounds = new Rect();
    }

    @Override
    public void draw(Canvas canvas) {

        final int N = Math.min(mChildDrawable.size(), mRowCount * mColumnCount);
        for (int i = 0; i < N; i++) {
            ChildDrawable item = mChildDrawable.get(i);
            Drawable drawable = item.mDrawable;
            if (drawable != null) {
                item.mDrawable.setBounds(item.mInsetL, item.mInsetT, item.mInsetR, item.mInsetB);
                drawable.draw(canvas);
            }
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        //Update the internal bounds in response to any external requests
        mBounds.set(bounds);
        measureSpace();
    }

    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setFilterBitmap(boolean filterBitmap) {
        mPaint.setFilterBitmap(filterBitmap);
        mPaint.setAntiAlias(filterBitmap);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        final int N = mChildDrawable.size();
        int height = mPaddingT + mPaddingB;
        for (int i = 0; i < N && i < mRowCount * mColumnCount; i++) {
            if ((i + 1) % mColumnCount == 0) {
                height += mChildH + vGap;
            }
        }

        return height - vGap;
    }

    @Override
    public int getIntrinsicWidth() {
        final int N = mChildDrawable.size();
        int width = mPaddingL + mPaddingR;
        if (N < mColumnCount) {
            width += mChildW * N + (N - 1) * hGap;
        } else {
            width += mChildW * mColumnCount + (mColumnCount - 1) * hGap;
        }

        return width;
    }

    public int getAlpha() {
        return mAlpha;
    }

    public void addDrawable(Drawable gridItem) {
        if (gridItem == null) {
            return;
        }
        ChildDrawable childDrawable = new ChildDrawable(gridItem);
        childDrawable.mDrawable = gridItem;
        mChildDrawable.add(childDrawable);

        measureChildrenPosition();
        invalidateSelf();
    }

    public void addDrawable(int i, Drawable gridItem) {
        if (gridItem == null || i > mChildDrawable.size()) {
            return;
        }

        ChildDrawable childDrawable = new ChildDrawable(gridItem);
        childDrawable.mDrawable = gridItem;
        mChildDrawable.add(i, childDrawable);

        measureChildrenPosition();
        invalidateSelf();
    }

    public void remove(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        for (ChildDrawable childDrawable : mChildDrawable) {
            if (childDrawable.mDrawable == drawable) {
                boolean result = mChildDrawable.remove(drawable);
            }
        }

        measureChildrenPosition();
        invalidateSelf();
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingL = left;
        mPaddingT = top;
        mPaddingR = right;
        mPaddingB = bottom;
        measureSpace();
    }

    public void setDefaultChildSize(int width, int height) {
        mChildW = width;
        mChildH = height;
        measureSpace();
    }

    public void setGap(int hGap, int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    private void measureSpace() {
        if (mColumnCount > 1) {
            int width = mBounds.width();
            hGap = (width - (mColumnCount * mChildW) - mPaddingL - mPaddingR) /
                    (mColumnCount - 1);
            hGap = Math.max(hGap, 0);
        }

        if (mRowCount > 1) {
            int height = mBounds.height();
            vGap = (height - (mRowCount * mChildH) - mPaddingT - mPaddingB) / (mRowCount - 1);
            vGap = Math.max(vGap, 0);
        }

        measureChildrenPosition();
    }

    private void measureChildrenPosition() {
        int childLeft = mPaddingL;
        int childTop = mPaddingT;
        final int N = Math.min(mChildDrawable.size(), mRowCount * mColumnCount);
        for (int i = 0; i < N; i++) {
            ChildDrawable item = mChildDrawable.get(i);
            item.mInsetL = childLeft;
            item.mInsetT = childTop;
            item.mInsetR = childLeft + mChildW;
            item.mInsetB = childTop + mChildH;

            if (i != 0 && (i + 1) % mColumnCount == 0) {
                childLeft = mPaddingL;
                childTop += mChildH + vGap;
            } else {
                childLeft += mChildW + hGap;
            }
        }
    }

    static class ChildDrawable {
        public ChildDrawable(Drawable drawable) {
            mDrawable = drawable;
        }

        public Drawable mDrawable;
        public int mInsetL, mInsetT, mInsetR, mInsetB;
        public int mId;
    }

}
