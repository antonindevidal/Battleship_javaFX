package game;

import game.Computer.Computer;
import game.Computer.ComputerEasy;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class NetworkGame {




    Game.jeu partOfGame= Game.jeu.place;
    private Board myBoard,otherPlayerBoard;
    private boolean horizontal = true;
    private boolean playerTurn = true;
    private List<Integer> boatToPlace=new ArrayList<Integer>();
    private int playerNumber;

    private StringProperty texte1 = new SimpleStringProperty("Orientation: Horizontal\n(right click to change)");
        public String getTexte1() { return texte1.get(); }
        public StringProperty texte1Property() { return texte1; }
        public void setTexte1(String texte1) { this.texte1.set(texte1); }

    private StringProperty texte2 = new SimpleStringProperty("Place your boats on the grid.5 boats left");
        public String getTexte2() { return texte2.get(); }
        public StringProperty texte2Property() { return texte2; }
        public void setTexte2(String texte2) { this.texte2.set(texte2); }

    public Game.jeu getPartOfGame() {
        return partOfGame;
    }

    public Board getMyBoard() {
        return myBoard;
    }

    public Board getOtherPlayerBoard() {
        return otherPlayerBoard;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public List<Integer> getBoatToPlace() {
        return boatToPlace;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
        if (isHorizontal())
        {
            setTexte1("Orientation: Horizontal\n(right click to change)");
        }
        else
            setTexte1("Orientation: Vertical\n (right click to change)");
    }

    public NetworkGame(int playerNumber) {
        this.myBoard = new Board();
        this.otherPlayerBoard = new Board();

        if (playerNumber == 1) playerTurn = true;
        else  playerTurn = false;

        this.playerNumber = playerNumber;
        boatToPlace.add(5);
        boatToPlace.add(4);
        boatToPlace.add(3);
        boatToPlace.add(3);
        boatToPlace.add(2);
    }





    public void otherPlayerPlaceBoat(int x , int y, boolean horizontal)
    {
        if (partOfGame == Game.jeu.place && boatToPlace.size() >0 && !playerTurn)
        {
            boolean result = otherPlayerBoard.placeShip(x,y,boatToPlace.get(0),horizontal,true);
            if (result)
            {
                playerTurn = true;
                if (playerNumber == 1)
                    boatToPlace.remove(0);
                if(boatToPlace.size()<=0)
                {
                    partOfGame= Game.jeu.joue;
                }
            }


        }
        System.out.println("Enemy board \n"+otherPlayerBoard);
        System.out.println("My board \n"+myBoard);
    }

    public void placeMyBoat(int x , int y, boolean horizontal)
    {
        if (partOfGame == Game.jeu.place && boatToPlace.size() >0 && playerTurn)
        {
            boolean result = myBoard.placeShip(x,y,boatToPlace.get(0),horizontal,true);
            if (result)
            {
                playerTurn = false;
                if (playerNumber == 2)
                    boatToPlace.remove(0);


                if(boatToPlace.size()<=0)
                {
                    partOfGame= Game.jeu.joue;
                }
            }


        }
        System.out.println("Enemy board \n"+otherPlayerBoard);
        System.out.println("My board \n"+myBoard);
    }


    public void iShoot(int x, int y)
    {
        if (partOfGame == Game.jeu.joue && playerTurn)
        {
            Game.shootResult sc = otherPlayerBoard.shoot(x,y);
            if (sc == Game.shootResult.miss)
            {
                playerTurn = false;
            }
        }
    }
    public void otherPlayerShoot(int x, int y)
    {
        if (partOfGame == Game.jeu.joue && !playerTurn)
        {
            Game.shootResult sc = myBoard.shoot(x,y);
            if (sc == Game.shootResult.miss)
            {
                playerTurn = true;
            }
        }
    }

    public boolean isEnding()
    {
        if(myBoard.hasLost())
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setTexte1("Opponent won");
                }
            });
            partOfGame = Game.jeu.fin;
            return true;
        }
        else if(otherPlayerBoard.hasLost())
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setTexte1("You win");
                }
            });
            partOfGame = Game.jeu.fin;

            return true;
        }
        return false;
    }
}
