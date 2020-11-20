package Game;

public class Cell {
    private boolean hit;
    private Ship ship;

    public boolean isHit() {
        return hit;
    }

    public boolean hasShip()
    {
        return ship!=null;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean shoot()
    {
        if (isHit())
            return false;
        else
        {
            if (hasShip())
            {
                ship.hit();
            }
            hit=true;

            return true;
        }
    }

    public Cell() {
        this.hit = false;
        this.ship = null;
    }


    @Override
    public String toString() {
        if(isHit() && hasShip())
            return "3";
        else if (isHit() && ! hasShip())
            return "2";
        else if (!isHit() && hasShip())
            return "1";
        else
            return "0";
    }
}
