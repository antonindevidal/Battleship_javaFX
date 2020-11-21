package game;

public class Board {

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
            if(!grid[y][x].getShip().isAlive())
            {
                nbShip--;
                return Game.shootResult.sink;
            }
        }
        return sR;
    }

    public boolean canPlaceShip(int x,int y,int size,boolean horizontal)
    {
        if (horizontal)
        {
            for(int i=0;i<size;i++)
            {
                if(x+i >= gridSize )
                {
                    return false;
                }
                if (grid[y][x+i].hasShip())
                {
                    return false;
                }
            }
        }
        else {
            for(int i=0;i<size;i++)
            {
                if(y+i >= gridSize || grid[y+i][x].hasShip())
                {
                    return false;
                }
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
                }
                return true;
            }
            else {
                Ship ship = new Ship(size) ;
                for (int i=0; i<size;i++)
                {
                    grid[y+i][x].setShip(ship);
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
