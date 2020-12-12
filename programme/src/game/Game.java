package game;


import java.util.ArrayList;
import java.util.List;

public class Game {



    public enum jeu{place,joue,fin}
    public enum shootResult{miss,hit,sink,alreadyHit}

    jeu partOfGame=jeu.place;
    private Board player,ordi;
    private boolean horizontal = true;
    private boolean playerTurn = true;
    private List<Integer> boatToPlace=new ArrayList<Integer>();
    private Computer computer;




    public boolean isHorizontal() {
        return horizontal;
    }

    public jeu getPartOfGame() {
        return partOfGame;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public Board getPlayer() {
        return player;
    }

    public Board getOrdi() {
        return ordi;
    }

    public int getNbBoatToPlace()
    {
        return boatToPlace.size();
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public Game() {
        this.player = new Board();
        this.ordi = new Board();

        computer = new ComputerNormal();

        computer.placeBoats(ordi);

        boatToPlace.add(5);
        boatToPlace.add(4);
        boatToPlace.add(3);
        boatToPlace.add(3);
        boatToPlace.add(2);
    }

    public void placeBoats(int x, int y,boolean horizontal)
    {
        if (partOfGame ==jeu.place && boatToPlace.size()>0)
        {
            boolean marche =player.placeShip(x,y,boatToPlace.get(0),horizontal);
            if (marche)
            {
                boatToPlace.remove(0);
                if(boatToPlace.size()<=0)
                {
                    partOfGame=jeu.joue;
                }
            }

        }
    }
    public shootResult playerShoot(int x,int y)
    {
        shootResult shootResult =null;

        if (playerTurn) {
            shootResult = ordi.shoot(x, y);
            if (shootResult != Game.shootResult.alreadyHit) {
                if (ordi.hasLost()) {
                    partOfGame = jeu.fin;
                    //Player won
                }
                if (shootResult == Game.shootResult.miss) {
                    playerTurn = false;
                }
            }

        }
        return shootResult;

    }

    public void computerShoot()
    {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shootResult cR = computer.shoot(player);
            if(cR == Game.shootResult.miss)
            {
                playerTurn = true;
            }
            else System.out.println("joue 2 fois");

    }


}
