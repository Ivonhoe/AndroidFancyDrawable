package ivonhoe.android.bezierinterpolator;

/**
 * Created by ivonhoe on 15-4-15.
 */
public class Point {
    public float x;
    public float y;

    public Point(){
    }

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void set(Point point){
        this.x = point.x;
        this.y = point.y;
    }
}
