package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerNormal extends Computer {
    private enum orientation{vertical,horizontal,neSaisPas}

    private Coordinates lastShoot =new Coordinates(),lastHit=new Coordinates(), boatFirstPos=new Coordinates();

    private Game.shootResult lastShootResult = Game.shootResult.miss;
    private boolean hasATarget, findOnPositive =true;
    private orientation boatOrientation=orientation.neSaisPas;


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
                setLasthit(lastShoot.getX(),lastShoot.getY());
                setBoatFirstPos(lastShoot.getX(),lastShoot.getY());
            }
            return res;
        }
        else
        {
            switch (boatOrientation)
            {
                case vertical:
                    shootVertical(board);
                    System.out.println("hori");
                    break;
                case horizontal:
                    shootHorizontal(board);
                    System.out.println("verti");

                    break;
                case neSaisPas:
                    adjaShoot(board,lastHit.getX(),lastHit.getY());
                    System.out.println("adja x:" +lastHit.getX()+" y:"+lastHit.getY() + " traget:" + hasATarget);

                    if (lastShootResult == Game.shootResult.hit)
                        setLasthit(lastShoot.getX(),lastShoot.getY());
                        guessOrientation();
                    break;
            }
            if (lastShootResult == Game.shootResult.sink)
            {
                hasATarget =false;
                boatOrientation=orientation.neSaisPas;
            }

        }

        return lastShootResult;
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
        setLastShoot(x,y);
        lastShootResult=sR;
        return sR;
    }

    private void adjaShoot(Board board, int x, int y)
    {
        Random r = new Random();
        int cote,paddingx=0,paddingy=0;


        Game.shootResult sR = Game.shootResult.alreadyHit;
        while ( sR == Game.shootResult.alreadyHit)
        {
            paddingx=0;paddingy=0;
            cote = r.nextInt((4-1) + 1);
            switch (cote){
                case 0:
                    if (x+1 <10)
                        paddingx=1;

                    break;
                case 1:
                    if (x-1 >=0)
                        paddingx=-1;

                    break;
                case 2:
                    if (y+1 <10)
                        paddingy=1;
                    break;
                default:
                    if (y-1 >=0)
                        paddingy=-1;

                    break;
            }
            sR = board.shoot(x+paddingx,y+paddingy);
        }

        setLastShoot(x+paddingx,y+paddingy);
        lastShootResult=sR;
    }

    private boolean allAdjacentHit(Board board, int x, int y)
    {
        if (board.getCell(x+1,y).isHit() &&board.getCell(x-1,y).isHit() &&board.getCell(x,y+1).isHit() &&board.getCell(x,y-1).isHit() )
            return true;
        return false;
    }
    private void setLastShoot(int x,int y)
    {
        lastShoot.setX(x);
        lastShoot.setY(y);
    }
    private void setLasthit(int x,int y)
    {
        lastHit.setX(x);
        lastHit.setY(y);
    }
    private void setBoatFirstPos(int x,int y)
    {
        boatFirstPos.setX(x);
        boatFirstPos.setY(y);
    }

    private void guessOrientation()
    {
        int paddingX,paddingY;
        paddingX =lastHit.getX() -boatFirstPos.getX() ;
        paddingY =lastHit.getY() -boatFirstPos.getY() ;

        if(paddingX != 0)
        {
            boatOrientation= orientation.horizontal;
        }
        else if(paddingY != 0)
        {
            boatOrientation= orientation.vertical;
        }
    }

    private void shootHorizontal(Board board)
    {
        int nextX = lastHit.getX() ;
        if(lastShootResult == Game.shootResult.miss)
        {
            findOnPositive = !findOnPositive;
            setLasthit(boatFirstPos.getX(),boatFirstPos.getY());
        }

        if (findOnPositive && nextX<9)
            nextX++;
        else if (!findOnPositive && nextX>0)
            nextX--;


        lastShootResult = board.shoot(nextX,lastHit.getY());
    }

    private void shootVertical(Board board)
    {
        int nextY = lastHit.getY() ;
        if(lastShootResult == Game.shootResult.miss)
        {
            findOnPositive = !findOnPositive;
            setLasthit(boatFirstPos.getX(),boatFirstPos.getY());
        }

        if (findOnPositive && nextY <9)
            nextY++;
        else if(!findOnPositive && nextY>0)
            nextY--;


        lastShootResult = board.shoot(lastHit.getX(),nextY);
    }


}
