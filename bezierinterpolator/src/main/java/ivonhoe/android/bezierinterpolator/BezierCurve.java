package ivonhoe.android.bezierinterpolator;

import android.util.Log;

import java.util.List;

/**
 * Created by ivonhoe on 15-4-15.
 */
public class BezierCurve extends Curve {

    public BezierCurve(Point... points) {
        super(points);
    }

    public BezierCurve(List<Point> points) {
        super(points);
    }

    @Override
    public Point getPathPoint(float t) {
        final int size = mControlPoints.length;
        if (size < 2) {
            return new Point(0, 0);
        }

        for (int i = 0; i < size; i++) {
            if (mTempPoints[i] == null) {
                mTempPoints[i] = new Point();
            }
            mTempPoints[i].set(mControlPoints[i]);
        }
        return deCasteljau(mTempPoints, t);
    }

    public static Point deCasteljau(Point[] points, float t) {
        final int n = points.length;
        for (int i = 1; i <= n; i++)
            for (int j = 0; j < n - i; j++) {
                points[j].x = (1 - t) * points[j].x + t * points[j + 1].x;
                points[j].y = (1 - t) * points[j].y + t * points[j + 1].y;
            }

        return points[0];
    }
}
