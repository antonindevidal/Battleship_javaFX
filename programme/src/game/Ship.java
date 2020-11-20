package game;

public class Ship {

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
