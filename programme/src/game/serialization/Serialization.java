package game.serialization;

import game.Manager.ComputerManager;
import game.computer.Computer;
import view.Previsualisation;

public abstract class Serialization {


    public abstract void save(ComputerManager cm);
    public abstract ComputerManager load() throws Exception;

}
