package game.Manager;

import javafx.application.Platform;

public class NetworkManager extends Manager{

    private int playerNumber;




    public NetworkManager(int playerNumber) {
        super();

        if (playerNumber == 1) // Player 1 always move first
        {
            playerTurn = true;
            setTexte1("Your turn");
        }
        else
        {
            playerTurn = false;
            setTexte1("Opponent turn");
        }
        this.playerNumber = playerNumber;

    }

    @Override
    public int getNbBoatToPlace()
    {
        if (playerNumber == 1 ) // player 1 always move first so we need to return the size minus 1
        {
            return boatToPlace.size()-1;
        }
        else
            return boatToPlace.size();
    }





    public void otherPlayerPlaceBoat(int x , int y, boolean horizontal)
    {
        if (partOfGame == ComputerManager.jeu.place && boatToPlace.size() >0 && !playerTurn) // Check it is the other player turn to place boats
        {
            boolean result = otherPlayerBoard.placeShip(x,y,boatToPlace.get(0),horizontal,false); // Place the ship
            if (result)
            {
                Platform.runLater(new Runnable() { // Not in the fx thread
                    @Override
                    public void run() {
                        setTexte1("Your turn");
                    }
                });

                playerTurn = true;
                if (playerNumber == 1) // Remove it is the last player to lace the boat
                    boatToPlace.remove(0);
                if(boatToPlace.size()<=0)
                {
                    partOfGame= ComputerManager.jeu.joue; // Change th part of the game when all boats are placed
                }
            }
        }

    }

    public void placeMyBoat(int x , int y, boolean horizontal)
    {
        if (partOfGame == ComputerManager.jeu.place && boatToPlace.size() >0 && playerTurn)// Check it is the  player turn to place boats
        {
            boolean result = myBoard.placeShip(x,y,boatToPlace.get(0),horizontal,true);
            if (result) // if the boats has been placed
            {
                setTexte1("Opponent turn");
                playerTurn = false;
                if (playerNumber == 2)
                    boatToPlace.remove(0);


                if(boatToPlace.size()<=0)
                {
                    partOfGame= ComputerManager.jeu.joue;
                }
            }


            setTexte2("Place your boats on the grid."+getNbBoatToPlace()+" boats left");
            if(getNbBoatToPlace() <=0) {
                setTexte2("The game started. Click on the top grid to shoot");
                setOrientationP("");

            }

        }

    }


    public void iShoot(int x, int y) // Player shoot
    {
        if (partOfGame == ComputerManager.jeu.joue && playerTurn)
        {
            ComputerManager.shootResult sc = otherPlayerBoard.shoot(x,y); // Try to shoot

            // Display result on the screen
            if (sc == ComputerManager.shootResult.miss)
            {
                playerTurn = false;
                setTexte1("Opponent turn");
                setTexte2("You've missed your shoot");

            }
            else if (sc == shootResult.hit)
            {
                setTexte2("You hit a boat");
            }
            else if (sc == shootResult.sink)
            {
                setTexte2("You sinked a boat");

            }
        }
    }
    public void otherPlayerShoot(int x, int y)
    {
        if (partOfGame == ComputerManager.jeu.joue && !playerTurn) // If other player turn to shoot
        {
            ComputerManager.shootResult sc = myBoard.shoot(x,y);
            if (sc == ComputerManager.shootResult.miss)
            {
                playerTurn = true;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setTexte1("Your turn");
                        setTexte2("Opponent missed");

                    }
                });

            }
            else if (sc == shootResult.hit)
            {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setTexte2("You've been hit");
                    }
                });
            }
            else if (sc == shootResult.sink)
            {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setTexte2("One of your boats have been sinked");
                    }
                });
            }
        }
    }

    public void erreurConnexion()
    {
        setTexte1("Connexion lost");
    } // Display connection error message


}
