package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerNormal extends Computer {
    private enum orientation{vertical,horizontal,neSaisPas}

    private int lastShootX,lastShootY,lastHitx,lastHity;
    private Game.shootResult lastShootResult = Game.shootResult.miss;
    private boolean hasATarget;
    private orientation ori=orientation.neSaisPas;


    @Override
    public void placeBoats(Board board) {
        Random r = new Random();
        int x,y;
        hasATarget=false;
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
                h=r.nextBoolean();

                placed=board.placeShip(x,y,boatToPlace.get(0),h);
            }
            placed=false;
            boatToPlace.remove(0);
        }
    }

    @Override
    public Game.shootResult shoot(Board board) {
        Game.shootResult  res;
        if (!hasATarget)
        {
            res = randShoot(board);
            if(res == Game.shootResult.hit) {
                hasATarget = true;
                lastHitx = lastShootX;
                lastHity = lastShootY;
            }
            return res;
        }
        else
        {
            switch (ori)
            {
                case vertical:
                    break;
                case horizontal:
                    break;
                case neSaisPas:
                    adjaShoot(board,lastHitx,lastHity);
                    break;

            }







        }

        return null;
    }

    private Game.shootResult randShoot(Board board)
    {
        Random r = new Random();
        int x=0,y=0;

        Game.shootResult sR = Game.shootResult.alreadyHit;

        while( sR == Game.shootResult.alreadyHit)
        {
            x=r.nextInt((board.gridSize-1) + 1) ;
            y=r.nextInt((board.gridSize-1) + 1) ;

            sR = board.shoot(x,y);
        }
        setLastShoot(sR,x,y);
        return sR;
    }

    private Game.shootResult adjaShoot(Board board, int x, int y)
    {
        Random r = new Random();
        int cote,paddingx=0,paddingy=0;

        if(allAdjacentHit(board,x,y))
        {
            goTootherSide(board);
        }

        Game.shootResult sR = Game.shootResult.alreadyHit;
        while ( sR == Game.shootResult.alreadyHit)
        {
            paddingx=0;paddingy=0;
            cote = r.nextInt((4-1) + 1);
            switch (cote){
                case 0:
                    if (x+1 >=10)
                        break;
                    paddingx=1;
                    break;
                case 1:
                    if (x-1 <0)
                        break;
                    paddingx=-1;
                    break;
                case 2:
                    if (y+1 >=10)
                        break;
                    paddingy=1;
                    break;
                default:
                    if (y-1 <0)
                        break;
                    paddingy=1;
                    break;
            }
            sR = board.shoot(x+paddingx,y+paddingy);
        }
        setLastShoot(sR,x,y);
        return sR;
    }

    private boolean allAdjacentHit(Board board, int x, int y)
    {
        if (board.getCell(x+1,y).isHit() &&board.getCell(x-1,y).isHit() &&board.getCell(x,y+1).isHit() &&board.getCell(x,y-1).isHit() )
            return true;
        return false;
    }
    private void setLastShoot(Game.shootResult sR,int x,int y)
    {
        lastShootResult=sR;
        lastShootX=x;
        lastShootY=y;
    }

    private void goTootherSide(Board board)
    {
        int checkX=lastHitx,checkY=lastHity;
        while(board.getCell(checkX,checkY).isHit())
        {

        }
    }
}
