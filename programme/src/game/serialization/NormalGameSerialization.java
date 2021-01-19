package game.serialization;


import game.manager.ComputerManager;

import java.io.*;


/**
 * Class used for serialization
 */
public class NormalGameSerialization extends Serialization {

    /**
     * Save on file save.save the computerManager
     * @param cm computer manager to save
     */
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

    /**
     * @return game loaded
     * @throws Exception if load fail or file doesn't exist
     */
    @Override
    public ComputerManager load() throws Exception {
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
