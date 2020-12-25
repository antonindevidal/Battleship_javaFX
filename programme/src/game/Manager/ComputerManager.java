package game.Manager;


import game.Board;
import game.Computer.Computer;
import game.Computer.ComputerEasy;
import javafx.application.Platform;

public class ComputerManager extends Manager{


    private Computer  computer;

    public void setComputer(Computer computer) { this.computer = computer; }

    public ComputerManager() {
        super();

        computer = new ComputerEasy(); // By default computer is set to easy
        computer.placeBoats(otherPlayerBoard); // Let the computer place his boats

    }
    public ComputerManager(Computer c) {
        this();
        computer = c;

    }

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
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setTexte2("You've been hit");
                    }
                });
                break;
            case miss:
                playerTurn = true;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setTexte1("Your turn");
                        setTexte2("Opponent missed");
                    }
                });
                break;
            case sink:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setTexte2("One of your boats have been sinked");
                    }
                });
                break;
        }




    }




}
