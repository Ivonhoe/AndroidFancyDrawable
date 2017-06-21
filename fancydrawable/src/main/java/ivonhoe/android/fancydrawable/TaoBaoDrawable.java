package ivonhoe.android.fancydrawable;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

/**
 * @auther Ivonhoe
 */
public class TaoBaoDrawable extends AtomDrawable {

    private Matrix mMatrix;

    public TaoBaoDrawable(Context context) {
        mMatrix = new Matrix();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
/*        mMatrix.setRotate(90);
        Log.d("simply", "setRotate 90 degree, matrix:" + mMatrix.toString());
        canvas.setMatrix(mMatrix);*/
/*        mMatrix.setTranslate(10, 10);*/
        mMatrix.setScale(2, 2);
    }

    @Override
    public ObjectAnimator[] getAtomAnimator(Atom atom, Rect bound) {
        return new ObjectAnimator[0];
    }

    @Override
    public boolean isAtomRunning(Atom atom) {
        return false;
    }
}
