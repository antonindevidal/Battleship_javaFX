package game;

public class Cell {
    private boolean hit;
    private Ship ship;
    private String boatImage = null;
    private int imageRotation=0;

    public String getBoatImage() {
        return boatImage;
    }

    public void setBoatImage(String boatImage) {
        this.boatImage = boatImage;
    }

    public int getImageRotation() {
        return imageRotation;
    }

    public void setImageRotation(int imageRotation) {
        this.imageRotation = imageRotation;
    }

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

    public void setShip(Ship ship) {
        this.ship=ship;
    }


    public Cell() {
        this.hit = false;
        this.ship = null;
    }

    //return false if boat already hit
    //return true if shoot was good
    public Game.shootResult shoot()
    {
        if (isHit())
        {
            //cell already hit
            return Game.shootResult.alreadyHit;
        }
        else
        {
            if (hasShip())
            {
                ship.hit();
                //hit shot
                hit=true;
                return Game.shootResult.hit;
            }
            //miss shot
            hit =true;
            return Game.shootResult.miss;
        }
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
