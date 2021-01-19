package game.manager;


import game.Cell;
import game.computer.Computer;
import game.computer.ComputerEasy;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * Manager of a game against computer
 */
public class ComputerManager extends Manager implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    /**
     * Game computer
     */
    private Computer  computer;

    public void setComputer(Computer computer) { this.computer = computer; }

    /**
     * Constructor
     */
    public ComputerManager() {
        super();

        computer = new ComputerEasy(); // By default computer is set to easy
        computer.placeBoats(otherPlayerBoard); // Let the computer place his boats

    }

    /**
     * Constructor with a computer
     * @param c computer for the game
     */
    public ComputerManager(Computer c) {
        this();
        computer = c;

    }

    /**
     * place a boat for player
     * @param x coordinate to place the boat
     * @param y coordinate to place the boat
     * @param horizontal true if the boat is placed horizontally
     */
    public void placeBoats(int x, int y,boolean horizontal) // Player place boats
    {
        if (partOfGame ==jeu.place && boatToPlace.size()>0)
        {
            boolean marche =myBoard.placeShip(x,y,boatToPlace.get(0),horizontal,true);
            if (marche) // marche = true if boat is placed
            {
                boatToPlace.remove(0);

                if(boatToPlace.size()<=0) // All boats are placed
                {
                    partOfGame=jeu.joue;
                    setTexte2("The game started. Click on the top grid to shoot");
                    setOrientationP("");
                }else
                    setTexte2("Place your boats on the grid."+getNbBoatToPlace()+" boats left");
            }

        }
    }

    /**
     * player shoot on a cell
     * @param x coordinate
     * @param y coordinate
     * @return result of the shoot
     */
    public shootResult playerShoot(int x,int y)
    {
        shootResult shootResult =null;

        if (playerTurn) {
            shootResult = otherPlayerBoard.shoot(x, y); // try to shoot on the computer board

            switch (shootResult)
            {
                case hit:
                    setTexte2("You hit a boat");
                    break;
                case alreadyHit:
                    setTexte2("You've already hit this case");
                    break;
                case miss:
                    playerTurn = false;
                    setTexte1("Opponent turn");
                    setTexte2("You've missed your shot");
                    break;
                case sink:
                    setTexte2("You sinked a boat");
                    break;
            }
        }
        return shootResult;

    }

    /**
     * Computer shoot
     */
    public void computerShoot() {
        try {
            Thread.sleep(1000); // Computer sleep to pretend thinking
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shootResult shootResult = computer.shoot(myBoard);



        switch (shootResult) // Set messages to display later because not in the javafx thread
        {
            case hit:
                Platform.runLater(() -> setTexte2("You've been hit"));
                break;
            case miss:
                playerTurn = true;
                Platform.runLater(() ->{
                        setTexte1("Your turn");
                        setTexte2("Opponent missed"); });
                break;
            case sink:
                Platform.runLater(() ->{ setTexte2("One of your boats have been sinked"); });
                break;
        }
    }

    /**
     * Set all properties after deserialisation without properties
     */
    public void setProperties()
    {
        texte1 = new SimpleStringProperty();
        texte2 = new SimpleStringProperty();
        orientationP = new SimpleStringProperty();
        setTexte1("");
        setTexte2("");
        if(horizontal)
            setOrientationP("Horizontal");
        else
            setOrientationP("Vertical");

        for(int i = 0;i<10; i++)
        {
            for(int j = 0;j<10; j++)
            {
                Cell c1 = getMyBoard().getCell(i,j);
                Cell c2 = getOtherPlayerBoard().getCell(i,j);

                getMyBoard().setCell(i,j,new Cell());
                getOtherPlayerBoard().setCell(i,j,new Cell());
                getMyBoard().getCell(i,j).setBoatImage(c1.getBoatImageSer());
                getOtherPlayerBoard().getCell(i,j).setBoatImage(c2.getBoatImageSer());
                getMyBoard().getCell(i,j).setImageRotation(c1.getImageRotationSer());
                getOtherPlayerBoard().getCell(i,j).setImageRotation(c2.getImageRotationSer());


                getMyBoard().getCell(i,j).setShip(c1.getShip());
                getOtherPlayerBoard().getCell(i,j).setShip(c2.getShip());

                if(c1.isHit())
                    getMyBoard().getCell(i,j).shoot();
                if(c2.isHit())
                    getOtherPlayerBoard().getCell(i,j).shoot();
            }
        }
    }


}
