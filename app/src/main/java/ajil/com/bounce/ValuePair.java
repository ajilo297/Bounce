package ajil.com.bounce;

/**
 * Created by ajilo on 25-10-2017.
 */

public class ValuePair <T>{
    private T x, y;
    public ValuePair() {
    }

    public ValuePair(T x, T y) {
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
