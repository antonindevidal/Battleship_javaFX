package game;

import game.manager.ComputerManager;
import javafx.beans.property.*;

import java.io.Serializable;

/**
 * Represent a cell of a board
 */
public class Cell implements Serializable {

    /**
     * true if the boat has already been hit
     */
    private boolean hit;

    /**
     * ship associated to this cell (null if no boat)
     */
    private Ship ship;

    /**
     * Property of the rotation of the image
     */
    private transient IntegerProperty imageRotation = new SimpleIntegerProperty();
        public int getImageRotation() { return imageRotation.get(); }
        public IntegerProperty imageRotationProperty() { return imageRotation; }
        public void setImageRotation(int imageRotation) { this.imageRotation.set(imageRotation); this.imageRotationSer = imageRotation; }


    /**
     * Property Image of the cell
     */
    private transient StringProperty boatImage = new SimpleStringProperty();
        public String getBoatImage() { return boatImage.get(); }
        public StringProperty boatImageProperty() { return boatImage; }
        public void setBoatImage(String boatImage) { this.boatImage.set(boatImage); this.boatImageSer=boatImage; }

    /**
     * boat image for serialization
     */
    private String boatImageSer;
    /**
     * boat rotation for imageRotation
     */
    private int imageRotationSer;

    public String getBoatImageSer() { return boatImageSer; }

    public int getImageRotationSer() { return imageRotationSer; }

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


    /**
     * constructor
     */
    public Cell() {
        this.hit = false;
        this.ship = null;
    }

    /**
     * shoot on the cell
     * @return result of the shoot
     */
    public ComputerManager.shootResult shoot()
    {
        if (isHit())// Si la cellule a déjà été touchée
        {
            return ComputerManager.shootResult.alreadyHit; // Retourne la valeur "déjà touché"
        }
        else
        {
            if (hasShip()) // Si la cellule à un bateau
            {
                ship.hit(); // On hit le  bateau
                hit=true;
                return ComputerManager.shootResult.hit;
            }
            // Le tire est manqué ( pas de bateau sur la cellule
            hit =true;
            return ComputerManager.shootResult.miss;
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
