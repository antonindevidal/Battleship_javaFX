package game;

import java.io.Serializable;

/**
 * Coordinates
 */
public class Coordinates implements Serializable {

    /**
     * x
     */
    private int x;
    /**
     * y
     */
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * constructor
     * @param x coordinate
     * @param y coordinate
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * constructor
     */
    public Coordinates() {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
