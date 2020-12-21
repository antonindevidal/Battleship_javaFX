package game.Manager;

import game.Board;
import javafx.application.Platform;
import javafx.scene.control.Button;

import java.util.List;

public class NetworkManager extends Manager{

    private int playerNumber;


    @Override
    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
        if (isHorizontal())
        {
            setTexte1("Orientation: Horizontal\n(right click to change)");
        }
        else
            setTexte1("Orientation: Vertical\n (right click to change)");
    }

    public NetworkManager(int playerNumber) {
        super();

        if (playerNumber == 1) playerTurn = true;
        else  playerTurn = false;

        this.playerNumber = playerNumber;

    }





    public void otherPlayerPlaceBoat(int x , int y, boolean horizontal)
    {
        if (partOfGame == ComputerManager.jeu.place && boatToPlace.size() >0 && !playerTurn)
        {
            boolean result = otherPlayerBoard.placeShip(x,y,boatToPlace.get(0),horizontal,true);
            if (result)
            {
                playerTurn = true;
                if (playerNumber == 1)
                    boatToPlace.remove(0);
                if(boatToPlace.size()<=0)
                {
                    partOfGame= ComputerManager.jeu.joue;
                }
            }
        }

    }

    public void placeMyBoat(int x , int y, boolean horizontal)
    {
        if (partOfGame == ComputerManager.jeu.place && boatToPlace.size() >0 && playerTurn)
        {
            boolean result = myBoard.placeShip(x,y,boatToPlace.get(0),horizontal,true);
            if (result)
            {
                playerTurn = false;
                if (playerNumber == 2)
                    boatToPlace.remove(0);


                if(boatToPlace.size()<=0)
                {
                    partOfGame= ComputerManager.jeu.joue;
                }
            }


        }

    }


    public void iShoot(int x, int y)
    {
        if (partOfGame == ComputerManager.jeu.joue && playerTurn)
        {
            ComputerManager.shootResult sc = otherPlayerBoard.shoot(x,y);
            if (sc == ComputerManager.shootResult.miss)
            {
                playerTurn = false;
            }
        }
    }
    public void otherPlayerShoot(int x, int y)
    {
        if (partOfGame == ComputerManager.jeu.joue && !playerTurn)
        {
            ComputerManager.shootResult sc = myBoard.shoot(x,y);
            if (sc == ComputerManager.shootResult.miss)
            {
                playerTurn = true;
            }
        }
    }


}
