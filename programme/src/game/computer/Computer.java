package game.computer;

import game.Board;
import game.Manager.ComputerManager;

import java.io.Serializable;

abstract public class Computer implements Serializable {

    public abstract void placeBoats(Board board); // Méthode pour placer un bateau
    public abstract ComputerManager.shootResult shoot(Board board); // Méthode pour tirer
}
