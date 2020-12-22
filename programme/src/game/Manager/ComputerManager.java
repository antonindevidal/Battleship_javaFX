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

        computer = new ComputerEasy();
        computer.placeBoats(otherPlayerBoard);

    }
    public ComputerManager(Computer c) {
        this();
        computer = c;

    }

    public void placeBoats(int x, int y,boolean horizontal)
    {
        if (partOfGame ==jeu.place && boatToPlace.size()>0)
        {
            boolean marche =myBoard.placeShip(x,y,boatToPlace.get(0),horizontal,true);
            if (marche)
            {

                boatToPlace.remove(0);
                if(boatToPlace.size()<=0)
                {
                    partOfGame=jeu.joue;
                }
            }

        }

        setTexte2("Place your boats on the grid."+getNbBoatToPlace()+" boats left");
        if(getNbBoatToPlace() <=0) {
            setTexte2("The game started. Click on the top grid to shoot");
            setOrientationP("");
        }


    }
    public shootResult playerShoot(int x,int y)
    {
        shootResult shootResult =null;

        if (playerTurn) {
            shootResult = otherPlayerBoard.shoot(x, y);

            if(shootResult == ComputerManager.shootResult.miss)
            {
                playerTurn = false;
                setTexte1("Opponent turn");
                setTexte2("You've missed your shot");
            }
            else if (shootResult == shootResult.hit)
            {
                setTexte2("You hit a boat");
            }
            else if (shootResult == shootResult.sink)
            {
                setTexte2("You sinked a boat");

            }

        }


        return shootResult;

    }

    public void computerShoot() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shootResult cR = computer.shoot(myBoard);
        if (cR == ComputerManager.shootResult.miss) {
            playerTurn = true;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setTexte1("Your turn");
                    setTexte2("Opponent missed");
                }
            });

        } else if (cR == shootResult.hit) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setTexte2("You've been hit");
                }
            });

        } else if (cR == shootResult.sink) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setTexte2("One of your boats have been sinked");
                }
            });

        }

    }




}
