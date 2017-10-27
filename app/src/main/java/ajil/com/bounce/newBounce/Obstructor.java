package ajil.com.bounce.newBounce;

/**
 * Created by ajil on 27/10/17.
 */

public class Obstructor {
    private int type;
    private Object object;
    private int obstruction;

    public Obstructor(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getObstruction() {
        return obstruction;
    }

    public void setObstruction(int obstruction) {
        this.obstruction = obstruction;
    }
}
