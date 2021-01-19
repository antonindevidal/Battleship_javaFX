package game.serialization;

import game.manager.ComputerManager;

/**
 * Abstract class used for serialization
 */
public abstract class Serialization {


    /**
     * Save a game
     * @param cm computer manager to save
     */
    public abstract void save(ComputerManager cm);


    /**
     * Load a saved game
     * @return a computer manager
     * @throws Exception file does not exist
     */
    public abstract ComputerManager load() throws Exception;

}
