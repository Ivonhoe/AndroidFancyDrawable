package ivonhoe.android.fancydrawable;

import android.animation.ObjectAnimator;
import android.graphics.Rect;

/**
 * Created by ivonhoe on 15-4-30.
 */
public class BallsCircleDrawable extends AtomDrawable{
    @Override
    public ObjectAnimator[] getAtomAnimator(Atom atom, Rect bound) {
        return null;
    }

    @Override
    public boolean isAtomRunning(Atom atom) {
        return false;
    }
}
