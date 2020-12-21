package game.Manager;


import game.Board;
import game.Computer.Computer;
import game.Computer.ComputerEasy;

public class ComputerManager extends Manager{


    private Computer  computer;

    public void setComputer(Computer computer) { this.computer = computer; }





    public int getNbBoatToPlace()
    {
        return boatToPlace.size();
    }


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
            setTexte1("Your turn");
        }

    }
    public shootResult playerShoot(int x,int y)
    {
        shootResult shootResult =null;

        if (playerTurn) {
            shootResult = otherPlayerBoard.shoot(x, y);
            if (shootResult != ComputerManager.shootResult.alreadyHit) {

                playerTurn = false;

            }

        }
        if(shootResult == ComputerManager.shootResult.alreadyHit)
            setTexte2("You have already shoot this case, play again");
        else if(shootResult == ComputerManager.shootResult.hit)
            setTexte2("You have hit a ship! play again");
        else if(shootResult == ComputerManager.shootResult.miss)
        {
            setTexte1("Computer turn");
            setTexte2("You have missed your shot");
        }
        else if(shootResult == ComputerManager.shootResult.sink)
            setTexte2("You have sinked a ship! play again");

        return shootResult;

    }

    public void computerShoot()
    {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shootResult cR = computer.shoot(myBoard);
            if(cR == ComputerManager.shootResult.miss)
            {
                playerTurn = true;
            }

    }




}
