package game;

import game.Manager.ComputerManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class Cell {
    private boolean hit; // Vrai si la cellule à déja été touchée
    private Ship ship; // Le bateau associé à la case ou null si il n'y en a pas
    private String boatImage = null; // Image pour la cellule

    private IntegerProperty imageRotation = new SimpleIntegerProperty();
        public int getImageRotation() { return imageRotation.get(); }
        public IntegerProperty imageRotationProperty() { return imageRotation; }
        public void setImageRotation(int imageRotation) { this.imageRotation.set(imageRotation); }

    private ObjectProperty<Image> img = new SimpleObjectProperty<Image>();
        public Image getImg() { return img.get(); }
        public ObjectProperty<Image> imgProperty() { return img; }
        public void setImg(String img) { this.img.set(new Image(img)); }

    public String getBoatImage() {
        return boatImage;
    }


    public void setBoatImage(String boatImage) {
            this.setImg(boatImage);
        this.boatImage = boatImage;
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
