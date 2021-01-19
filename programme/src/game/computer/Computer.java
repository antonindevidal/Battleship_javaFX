package game.computer;

import game.Board;
import game.manager.ComputerManager;

import java.io.Serializable;

/**
 * Abstract class for a computer
 * extension point
 */
abstract public class Computer implements Serializable {

    /**
     * Place all his boats n a board
     * @param board board to place boats onto
     */
    public abstract void placeBoats(Board board);

    /**
     * Shoot on a board
     * @param board board to shoot on
     * @return  result of the shoot
     */
    public abstract ComputerManager.shootResult shoot(Board board);
}
