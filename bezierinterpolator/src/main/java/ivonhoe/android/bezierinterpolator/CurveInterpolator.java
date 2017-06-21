package ivonhoe.android.bezierinterpolator;

import android.view.animation.Interpolator;

/**
 * Created by ivonhoe on 15-4-15.
 */
public class CurveInterpolator implements Interpolator {

    private Curve mCurve;

    private CurveSampler mCurveSampler;

    private CurveListener mListener;

    private float mCurrentTime;

    // 设置曲线类型，是贝塞尔曲线还是其他曲线
    public void setCurveType(Curve curve) {
        mCurve = curve;
    }

    // 设置曲线的用途，表示是位移?速度?还是加速度?
    public void setSamplerUsage(CurveSampler sampler) {
        mCurveSampler = sampler;
    }

    public void build() throws RuntimeException {
        if (mCurve == null) {
            throw new RuntimeException(
                    "Can not build curve sampler without curve,please create curve first");
        }
        if (mCurveSampler == null) {
            mCurveSampler = new DisplacementSampler();
        }

        mCurveSampler.attach(mCurve);
    }

    @Override
    public float getInterpolation(float input) {
        float result;
        mCurrentTime = input;
        if (mCurveSampler == null) {
            result = input;
        } else {
            result = mCurveSampler.getSamplerValue(input);
        }

        notify(input, result);
        return result;
    }

    public float getAnimatedTime() {
        return mCurrentTime;
    }

    public void setInterpolatorListener(CurveListener listener) {
        mListener = listener;
    }

    public void notify(float input, float result) {
        if (mListener != null)
            mListener.drawDistance(input, result);
    }

    public interface CurveListener {
        public void drawDistance(float input, float result);
    }
}
