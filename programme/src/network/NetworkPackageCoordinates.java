package network;

import java.io.Serializable;

public class NetworkPackageCoordinates implements Serializable {

    private int x;
    private int y;
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
