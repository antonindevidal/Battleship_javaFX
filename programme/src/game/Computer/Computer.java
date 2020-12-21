package game.Computer;

import game.Board;
import game.Manager.ComputerManager;

abstract public class Computer {

    public abstract void placeBoats(Board board); // Méthode pour placer un bateau
    public abstract ComputerManager.shootResult shoot(Board board); // Méthode pour tirer
}
