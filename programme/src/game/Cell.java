package game;

public class Cell {
    private boolean hit; // Vrai si la cellule à déja été touchée
    private Ship ship; // Le bateau associé à la case ou null si il n'y en a pas
    private String boatImage = null; // Image pour la cellule
    private int imageRotation=0; // Rotation de l'image

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


    public Game.shootResult shoot()
    {
        if (isHit())// Si la cellule a déjà été touchée
        {
            return Game.shootResult.alreadyHit; // Retourne la valeur "déjà touché"
        }
        else
        {
            if (hasShip()) // Si la cellule à un bateau
            {
                ship.hit(); // On hit le  bateau
                hit=true;
                return Game.shootResult.hit;
            }
            // Le tire est manqué ( pas de bateau sur la cellule
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
