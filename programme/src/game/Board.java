package game;

import java.util.HashMap;
import java.util.Map;

/*
    Classe représentant un plateau de jeux
 */

public class Board {
    //  Map représentant les images utilisées sur les plateaux
    public static final Map<String,String> imagesBateaux= new HashMap<String, String>() {{
        put("front","/images/frontBoat.png");   // Coté de bateau
        put("body","/images/boatChest.png");    // Corp du bateau
        put("target","/images/target.png");     // Quand le bateau est touché
        put("redCross","/images/redCross.png"); // Quand on loupe un tire
    }};

    public static final int gridSize = 10; //Taille de la grille


    private Cell[][] grid = new Cell[10][10]; // Grille de 10 par 10 représentant le plateau
    private int nbShip; //Nombre de bateaux restant sur la grille


    public Board() {
        this.nbShip = 5; // 5 bateaux au début d'une partie

        for(int y=0; y<10;y++)
        {
            for(int x=0; x<10;x++)
            {
                grid[y][x] = new Cell(); // On créé les cellules sur la grille
            }
        }
    }

    public Cell getCell(int x, int y) // retourne une cellule pour des coordonnés
    {
        return grid[y][x];
    }
    public boolean hasLost() // Retourne si le plateau à perdu
    {
        return nbShip<=0;
    }

    public Game.shootResult shoot(int x, int y) // Tire  à partir de coordonnés
    {
        Game.shootResult sR =grid[y][x].shoot(); //Tire sur la cellule
        if(sR == Game.shootResult.hit) // Si le tire a touché
        {
            grid[y][x].setBoatImage(imagesBateaux.get("target")); // On change l'image
            if(!grid[y][x].getShip().isAlive())// Si le bateau touché  est coulé
            {
                nbShip--; // Retire un bateau
                return Game.shootResult.sink; // On retourne le résultat d'n bateau coulé
            }
        }
        else if(sR == Game.shootResult.miss) // Si on a loupé le tire
        {
            grid[y][x].setBoatImage(imagesBateaux.get("redCross")); // On change l'image
        }
        return sR;
    }

    private boolean checkCellsAround(int x,int y) // Retourne vrai si les cases autour des coordonnés donnés son vide ou en dehors des limites
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

    public boolean canPlaceShip(int x,int y,int size,boolean horizontal)// Retourne vrai si on peut placer un bateau sur la case
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
    public boolean placeShip(int x,int y,int size,boolean horizontal) // Placer un bateau en fonction des coordonés et de l'orientation
    {
        if (canPlaceShip(x,y,size,horizontal)) // Si on peut placer le bateau
        {
            if(horizontal) // Si on place le bateau à l'horizontal
            {
                Ship ship = new Ship(size) ;
                for (int i=0; i<size;i++) // Pour la taille du bateau on va l'ajouter dans chaque cellules
                {
                    grid[y][x+i].setShip(ship); // On met le bateau dans la cellule
                    grid[y][x+i].setImageRotation(90); // rotation de l'image necessaire pour l'affichage
                    if (i==0)
                    {
                        grid[y][x+i].setBoatImage(imagesBateaux.get("front")); // On place un des bord du bateau
                        grid[y][x+i].setImageRotation(270);
                    }
                    else if(i == size-1)
                    {
                        grid[y][x+i].setBoatImage(imagesBateaux.get("front")); // On met l'autre bord du bateau

                    }
                    else
                    {
                        grid[y][x+i].setBoatImage(imagesBateaux.get("body")); // On met l'image de corp du bateau
                    }
                }
                return true;
            }
            else { // Pareil qu'avant mais à la vertical
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

    public void cleanBoard()
    {
        for (int i = 0; i<10 ; i++)
        {
            for (int j = 0; j<10 ; j++)
            {
                grid[j][i].setBoatImage(imagesBateaux.get(null));
            }
        }
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
