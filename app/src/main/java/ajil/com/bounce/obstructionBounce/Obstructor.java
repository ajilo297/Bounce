package ajil.com.bounce.obstructionBounce;

/**
 * Created by ajil on 26/10/17.
 */

public class Obstructor {
    private int type;
    private Object object;
    private int obstruction;

    public Obstructor(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getObstruction() {
        return obstruction;
    }

    public void setObstruction(int obstruction) {
        this.obstruction = obstruction;
    }

    public int getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
