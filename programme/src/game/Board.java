package game;

import game.manager.ComputerManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Classe représentant un plateau de jeux
 */

/**
 * Board represent a player grid in the game
 */
public class Board  implements Serializable {
    //  Map représentant les images utilisées sur les plateaux
    /**
     * all images used to display
     */
    public transient static final Map<String,String> IMAGESBATEAUX= new HashMap<String, String>() {{
        put("front","/images/frontBoat.png");   // Coté de bateau
        put("body","/images/boatChest.png");    // Corp du bateau
        put("target","/images/target.png");     // Quand le bateau est touché
        put("redCross","/images/redCross.png"); // Quand on loupe un tire
    }};

    /**
     * size of a grid
     */
    public static final int gridSize = 10; //Taille de la grille


    /**
     * List where all cells are stored
     */
    private List<List<Cell>> gridList  =new ArrayList<>();

    private int nbShip; //Nombre de bateaux restant sur la grille


    public List<List<Cell>> getGrid() { return gridList; }

    public Board() {
        this.nbShip = 5; // 5 bateaux au début d'une partie

        for(int y=0; y<10;y++)
        {
            gridList.add(new ArrayList<>());
            for(int x=0; x<10;x++)
            {
                gridList.get(y).add(new Cell());
            }
        }

    }

    public Cell getCell(int x, int y) // retourne une cellule pour des coordonnés
    {
        return gridList.get(y).get(x);
    }
    public void setCell(int x,int y,Cell c)
    {
        List<Cell> ac = gridList.get(y);
        ac.set(x,c);


    }

    /**
     * @return false if there are no boats left
     */
    public boolean hasLost() // Retourne si le plateau à perdu
    {
        return nbShip<=0;
    }

    /**
     * @param x coordinate
     * @param y coordinate
     * @return  result of the shoot
     */
    public ComputerManager.shootResult shoot(int x, int y) // Tire  à partir de coordonnés
    {
        ComputerManager.shootResult sR =gridList.get(y).get(x).shoot(); //Tire sur la cellule
        if(sR == ComputerManager.shootResult.hit) // Si le tire a touché
        {
            setImage(x,y,"target"); // On change l'image
            if(!gridList.get(y).get(x).getShip().isAlive())// Si le bateau touché  est coulé
            {
                nbShip--; // Retire un bateau
                return ComputerManager.shootResult.sink; // On retourne le résultat d'n bateau coulé
            }
        }
        else if(sR == ComputerManager.shootResult.miss) // Si on a loupé le tire
        {
            setImage(x,y,"redCross");
        }
        return sR;
    }

    /**
     * @param x coordinate
     * @param y coordinate
     * @return  true if all cells around these coordinates are free(no boats)
     */
    private boolean checkCellsAround(int x,int y) // Retourne vrai si les cases autour des coordonnés donnés son vide ou en dehors des limites
    {
        if (x <10 && y<10 &&!gridList.get(y).get(x).hasShip() )
        {
            if(x+1 < 10 && gridList.get(y).get(x+1).hasShip())
                return false;
            if(x-1 >= 0 && gridList.get(y).get(x-1).hasShip())
                return false;
            if(y+1 < 10 && gridList.get(y+1).get(x).hasShip())
                return false;
            if(y-1 >= 0 && gridList.get(y-1).get(x).hasShip())
                return false;
            return true;
        }
        return false;
    }


    /**
     * @param x coordinate
     * @param y coordinate
     * @param size  size of the boat to place
     * @param horizontal if the boat is horizonatl or not
     * @return  true if it can place the ship
     */
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

    /**
     * @param x coordinate
     * @param y coordinate
     * @param size  size of the boat to place
     * @param horizontal    if the boat is horizonatl or not
     * @param showBoat true if  want toshow the boat images on the screen
     * @return true if the boat has been placed correctly
     */
    public boolean placeShip(int x,int y,int size,boolean horizontal,boolean showBoat) // Placer un bateau en fonction des coordonés et de l'orientation
    {
        if (canPlaceShip(x,y,size,horizontal)) // Si on peut placer le bateau
        {
            if(horizontal) // Si on place le bateau à l'horizontal
            {
                Ship ship = new Ship(size) ;
                for (int i=0; i<size;i++) // Pour la taille du bateau on va l'ajouter dans chaque cellules
                {
                    gridList.get(y).get(x+i).setShip(ship); // On met le bateau dans la cellule
                    if(showBoat)
                    {
                        gridList.get(y).get(x+i).setImageRotation(90); // rotation de l'image necessaire pour l'affichage
                        if (i==0)
                        {
                            gridList.get(y).get(x+i).setImageRotation(270);
                            setImage(x+i,y,"front");// On place un des bord du bateau
                        }
                        else if(i == size-1)
                        {
                            setImage(x+i,y,"front"); // On met l'autre bord du bateau
                        }
                        else
                        {
                            setImage(x+i,y,"body");// On met l'image de corp du bateau
                        }
                    }

                }
                return true;
            }
            else { // Pareil qu'avant mais à la vertical
                Ship ship = new Ship(size) ;
                for (int i=0; i<size;i++)
                {
                    gridList.get(y+i).get(x).setShip(ship);
                    if (showBoat)
                    {
                        if (i==0)
                        {
                            setImage(x,y+i,"front");
                        }
                        else if(i == size-1)
                        {
                            gridList.get(y+i).get(x).setImageRotation(180);
                            setImage(x,y+i,"front");
                        }
                        else
                        {
                            setImage(x,y+i,"body");
                        }
                    }

                }
                return true;
            }

        }
        return false;
    }

    /**
     * @param x coordinate
     * @param y coordinate
     * @param img name of the image to use
     */
    private void setImage(int x, int y, String img)
    {
        gridList.get(y).get(x).setBoatImage(IMAGESBATEAUX.get(img));
    }



    @Override
    public String toString() {
        String temp="";
        for(int y=0; y<10;y++)
        {
            for(int x=0; x<10;x++)
            {
                temp=temp+" "+gridList.get(y).get(x);
            }
            temp=temp+"\n";
        }
        return temp;
    }



}
