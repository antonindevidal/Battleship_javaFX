package Game;

import java.util.Arrays;

public class Board {

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

    public boolean hasLost()
    {
        return nbShip<=0;
    }

    public boolean shoot(int x, int y)
    {
        return grid[y][x].shoot();
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
