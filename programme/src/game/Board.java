package game;

import java.util.HashMap;
import java.util.Map;

public class Board {
    public static final Map<String,String> imagesBateaux= new HashMap<String, String>() {{
        put("front","/images/frontBoat.png");
        put("body","/images/boatChest.png");
        put("target","/images/target.png");
        put("redCross","/images/redCross.png");
    }};




    public static final int gridSize = 10;
    private Cell[][] grid = new Cell[10][10];
    private int nbShip;


    public Board() {
        this.nbShip = 5;

        for(int y=0; y<10;y++)
        {
            for(int x=0; x<10;x++)
            {
                grid[y][x] = new Cell();
            }
        }
    }

    public Cell getCell(int x, int y)
    {
        return grid[y][x];
    }
    public boolean hasLost()
    {

        return nbShip<=0;
    }

    public Game.shootResult shoot(int x, int y)
    {
        Game.shootResult sR =grid[y][x].shoot();
        if(sR == Game.shootResult.hit)
        {
            grid[y][x].setBoatImage(imagesBateaux.get("target"));
            if(!grid[y][x].getShip().isAlive())
            {
                nbShip--;
                return Game.shootResult.sink;
            }
        }
        else if(sR == Game.shootResult.miss)
        {
            grid[y][x].setBoatImage(imagesBateaux.get("redCross"));
        }
        return sR;
    }

    private boolean checkCellsAround(int x,int y)
    {
        if (x <10 && y<10 &&!grid[y][x].hasShip() )
        {
            if(x+1 < 10 && grid[y][x+1].hasShip())
                return false;
            if(x-1 >= 0 && grid[y][x-1].hasShip())
                return false;
            if(y+1 < 10 && grid[y+1][x].hasShip())
                return false;
            if(y-1 >= 0 && grid[y-1][x].hasShip())
                return false;
            return true;
        }
        return false;
    }

    public boolean canPlaceShip(int x,int y,int size,boolean horizontal)
    {
        if (horizontal)
        {
            for(int i=0;i<size;i++)
            {
                if(!checkCellsAround(x+i,y))
                    return false;
            }
        }
        else {
            for(int i=0;i<size;i++)
            {
                if(!checkCellsAround(x,y+i))
                    return false;
            }
        }
        return true;

    }
    public boolean placeShip(int x,int y,int size,boolean horizontal)
    {
        if (canPlaceShip(x,y,size,horizontal))
        {
            if(horizontal)
            {
                Ship ship = new Ship(size) ;
                for (int i=0; i<size;i++)
                {
                    grid[y][x+i].setShip(ship);
                    grid[y][x+i].setImageRotation(90);
                    if (i==0)
                    {
                        grid[y][x+i].setBoatImage(imagesBateaux.get("front"));
                        grid[y][x+i].setImageRotation(270);
                    }
                    else if(i == size-1)
                    {
                        grid[y][x+i].setBoatImage(imagesBateaux.get("front"));

                    }
                    else
                    {
                        grid[y][x+i].setBoatImage(imagesBateaux.get("body"));
                    }
                }
                return true;
            }
            else {
                Ship ship = new Ship(size) ;
                for (int i=0; i<size;i++)
                {
                    grid[y+i][x].setShip(ship);
                    if (i==0)
                    {
                        grid[y+i][x].setBoatImage(imagesBateaux.get("front"));
                    }
                    else if(i == size-1)
                    {
                        grid[y+i][x].setBoatImage(imagesBateaux.get("front"));
                        grid[y+i][x].setImageRotation(180);
                    }
                    else
                    {
                        grid[y+i][x].setBoatImage(imagesBateaux.get("body"));
                    }
                }
                return true;
            }

        }
        return false;
    }



    @Override
    public String toString() {
        String temp="";
        for(int y=0; y<10;y++)
        {
            for(int x=0; x<10;x++)
            {
                temp=temp+" "+grid[y][x];
            }
            temp=temp+"\n";
        }
        return temp;
    }



}
