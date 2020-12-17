package game.Computer;

import game.Board;
import game.Game;

abstract public class Computer {

    public abstract void placeBoats(Board board); // Méthode pour placer un bateau
    public abstract Game.shootResult shoot(Board board); // Méthode pour tirer
}
