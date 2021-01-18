package network;

import java.io.Serializable;

/**
 * object send during network communication
 * - coordinates
 */
public class NetworkPackageCoordinates implements Serializable {

    /**
     * x coordinates
     */
    private int x;

    /**
     * y coordinate
     */
    private int y;
    /**
     * true if the boat to place is horizontal
     */
    private boolean horizontal;

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * constructor
     * @param x x coordinate
     * @param y y coordinate
     * @param horizontal if boat is horizontal
     */
    public NetworkPackageCoordinates(int x, int y, boolean horizontal) {
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }


}
