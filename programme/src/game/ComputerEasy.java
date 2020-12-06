package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerEasy extends Computer {

    @Override
    public void placeBoats(Board board) {
        Random r = new Random();
        int x,y;
        boolean placed = false,h;
        List<Integer> boatToPlace=new ArrayList<Integer>();
        boatToPlace.add(5);
        boatToPlace.add(4);
        boatToPlace.add(3);
        boatToPlace.add(3);
        boatToPlace.add(2);

        while(boatToPlace.size() >0)
        {
            while(!placed)
            {
                 x=r.nextInt((board.gridSize-1) + 1) ;
                 y=r.nextInt((board.gridSize-1) + 1) ;

                 if (x+y%2 == 0)
                     h=true;
                 else
                     h=false;
                 placed=board.placeShip(x,y,boatToPlace.get(0),h);


            }
            placed=false;
            boatToPlace.remove(0);
        }

    }


    @Override
    public Game.shootResult shoot(Board board) {
        Random r = new Random();
        int x=0,y=0;

        Game.shootResult sR = Game.shootResult.alreadyHit;

        while( sR == Game.shootResult.alreadyHit)
        {
            x=r.nextInt((board.gridSize-1) + 1) ;
            y=r.nextInt((board.gridSize-1) + 1) ;

            sR = board.shoot(x,y);
        }

        return sR;
    }
}
