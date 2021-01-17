package game.Manager;

import game.Board;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class Manager {



    public enum jeu{place,joue,fin}
    public enum shootResult{miss,hit,sink,alreadyHit}


    protected ComputerManager.jeu partOfGame= ComputerManager.jeu.place;
    protected Board myBoard,otherPlayerBoard;
    protected boolean horizontal = true;
    protected boolean playerTurn = true;
    protected List<Integer> boatToPlace=new ArrayList<Integer>();


    public boolean isHorizontal() {
        return horizontal;
    }

    public jeu getPartOfGame() {
        return partOfGame;
    }

    // Set if the boat is placed horizontaly or not and display that
    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
        if (isHorizontal())
        {
            setOrientationP("Horizontal");
        }
        else
            setOrientationP("Vertical");
    }



    public Board getMyBoard() {
        return myBoard;
    }

    public Board getOtherPlayerBoard() {
        return otherPlayerBoard;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public List<Integer> getBoatToPlace() {
        return boatToPlace;
    }



    public int getNbBoatToPlace()
    {
        return boatToPlace.size();
    }



    // all the text to display (bind in the controller)
    protected StringProperty texte1 = new SimpleStringProperty();
        public String getTexte1() { return texte1.get(); }
        public StringProperty texte1Property() { return texte1; }
        public void setTexte1(String texte1) { this.texte1.set(texte1); }

    protected StringProperty texte2 = new SimpleStringProperty("Place your boats on the grid. 5 boats left");
        public String getTexte2() { return texte2.get(); }
        public StringProperty texte2Property() { return texte2; }
        public void setTexte2(String texte2) { this.texte2.set(texte2); }

    private StringProperty orientationP = new SimpleStringProperty("Horizontal");
        public String getOrientationP() { return orientationP.get(); }
        public StringProperty orientationPProperty() { return orientationP; }
        public void setOrientationP(String orientationP) { this.orientationP.set(orientationP); }

    public Manager() {
        this.myBoard = new Board();
        this.otherPlayerBoard = new Board(); // Add all boats to place on the board (maybe there is a better way to do it...)
        boatToPlace.add(5);
        boatToPlace.add(4);
        boatToPlace.add(3);
        boatToPlace.add(3);
        boatToPlace.add(2);
    }



    public boolean isEnding()// Check if one of the player has lost
    {

        if(myBoard.hasLost())
        {
            Platform.runLater(() ->setTexte1("Opponent won"));
            partOfGame = ComputerManager.jeu.fin;
            return true;
        }
        else if(otherPlayerBoard.hasLost())
        {
            Platform.runLater(() ->setTexte1("You win"));
            partOfGame = ComputerManager.jeu.fin;

            return true;
        }
        return false;
    }
}
