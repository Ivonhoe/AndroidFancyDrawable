package ivonhoe.android.bezierinterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivonhoe on 15-4-15.
 */
public class BezierInterpolator extends CurveInterpolator {

    public BezierInterpolator(float x1, float y1, float x2, float y2) {
        List<Point> controlPoints = new ArrayList<Point>();
        controlPoints.add(new Point(0f, 0f));
        controlPoints.add(new Point(x1, y1));
        controlPoints.add(new Point(x2, y2));
        controlPoints.add(new Point(1f, 1f));
        buildSampler(controlPoints);
    }

    public BezierInterpolator(List<Point> points) {
        buildSampler(points);
    }

    private void buildSampler(List<Point> controlPoints) {
        BezierCurve bezierCurve = new BezierCurve(controlPoints);
        setCurveType(bezierCurve);
        DisplacementSampler samplers = new DisplacementSampler();
        setSamplerUsage(samplers);
        build();
    }
}
