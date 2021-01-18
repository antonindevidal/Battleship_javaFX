package game;

import java.io.Serializable;

/**
 * a ship
 */
public class Ship  implements Serializable {

    /**
     * Health point  of the boat
     */
    private int pv;

    public int getPv() {
        return pv;
    }

    /**
     * constructor
     * @param pv health point
     */
    public Ship(int pv) {
        this.pv = pv;
    }

    /**
     * check if the boat is alive
     * @return true if boat is dead - pv less than 0
     */
    public boolean isAlive()
    {
        return getPv()>0;
    }

    /**
     * Boat hit - decrement hp
     */
    public void hit()
    {
        pv--;
    }
}
