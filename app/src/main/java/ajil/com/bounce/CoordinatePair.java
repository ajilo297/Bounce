package ajil.com.bounce;

/**
 * Created by ajilo on 24-10-2017.
 */

public class CoordinatePair<T> {
    private T x;
    private T y;

    public CoordinatePair(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }
}
