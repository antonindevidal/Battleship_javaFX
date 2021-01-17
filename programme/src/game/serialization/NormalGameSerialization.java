package game.serialization;


import game.Manager.ComputerManager;
import game.computer.Computer;
import view.Previsualisation;

import javax.swing.*;
import java.io.*;

public class NormalGameSerialization extends Serialization {
    @Override
    public void save(ComputerManager cm) {

        try {
            FileOutputStream fileOut = new FileOutputStream("save.save");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(cm);

            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    @Override
    public ComputerManager load() throws Exception{
        ComputerManager computerManager = null;
        try {
            FileInputStream fileIn = new FileInputStream("save.save");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            computerManager = (ComputerManager) in.readObject();
            computerManager.setProperties();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            throw new Exception("No save available");

        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return computerManager;
    }
}
