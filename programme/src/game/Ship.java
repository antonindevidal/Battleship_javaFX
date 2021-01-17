package game;

import java.io.Serializable;

public class Ship  implements Serializable {

    private int pv;

    public int getPv() {
        return pv;
    }

    public Ship(int pv) {
        this.pv = pv;
    }

    public boolean isAlive()
    {
        return getPv()>0;
    }

    public void hit()
    {
        pv--;
    }
}
