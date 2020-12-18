package game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    private List<List<Cell>> gridList  =new ArrayList<>();

    private int nbShip; //Nombre de bateaux restant sur la grille
    private ObservableList<String> imagesLinks = FXCollections.observableArrayList();

    public ObservableList<String> getImagesLinks() {
        return imagesLinks;
    }

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
        for(int y=0; y<10;y++)
        {
            for(int x=0; x<10;x++)
            {
                imagesLinks.add(gridList.get(y).get(x).getBoatImage());
            }
        }
    }

    public Cell getCell(int x, int y) // retourne une cellule pour des coordonnés
    {
        return gridList.get(y).get(x);
    }
    public boolean hasLost() // Retourne si le plateau à perdu
    {
        return nbShip<=0;
    }

    public Game.shootResult shoot(int x, int y) // Tire  à partir de coordonnés
    {
        Game.shootResult sR =gridList.get(y).get(x).shoot(); //Tire sur la cellule
        if(sR == Game.shootResult.hit) // Si le tire a touché
        {
            gridList.get(y).get(x).setBoatImage(imagesBateaux.get("target")); // On change l'image
            if(!gridList.get(y).get(x).getShip().isAlive())// Si le bateau touché  est coulé
            {
                nbShip--; // Retire un bateau
                return Game.shootResult.sink; // On retourne le résultat d'n bateau coulé
            }
        }
        else if(sR == Game.shootResult.miss) // Si on a loupé le tire
        {
            gridList.get(y).get(x).setBoatImage(imagesBateaux.get("redCross")); // On change l'image
        }
        return sR;
    }

    private boolean checkCellsAround(int x,int y) // Retourne vrai si les cases autour des coordonnés donnés son vide ou en dehors des limites
    {
        if (x <10 && y<10 &&!gridList.get(y).get(x).hasShip() )
        {
            if(x+1 < 10 && gridList.get(y).get(x).hasShip())
                return false;
            if(x-1 >= 0 && gridList.get(y).get(x).hasShip())
                return false;
            if(y+1 < 10 && gridList.get(y).get(x).hasShip())
                return false;
            if(y-1 >= 0 && gridList.get(y).get(x).hasShip())
                return false;
            return true;
        }
        return false;
    }


    public ObservableList<String> getImages()
    {
        ObservableList<String> imagesLinks = FXCollections.observableArrayList();

        for(int y=0; y<10;y++)
        {
            for(int x=0; x<10;x++)
            {
                imagesLinks.add(gridList.get(y).get(x).getBoatImage());
            }
        }

        return  imagesLinks;

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
                    gridList.get(y).get(x+i).setShip(ship); // On met le bateau dans la cellule
                    gridList.get(y).get(x+i).setImageRotation(90); // rotation de l'image necessaire pour l'affichage
                    if (i==0)
                    {
                        gridList.get(y).get(x+i).setBoatImage(imagesBateaux.get("front")); // On place un des bord du bateau
                        gridList.get(y).get(x+i).setImageRotation(270);
                    }
                    else if(i == size-1)
                    {
                        gridList.get(y).get(x+i).setBoatImage(imagesBateaux.get("front")); // On met l'autre bord du bateau

                    }
                    else
                    {
                        gridList.get(y).get(x+i).setBoatImage(imagesBateaux.get("body")); // On met l'image de corp du bateau
                    }
                }
                return true;
            }
            else { // Pareil qu'avant mais à la vertical
                Ship ship = new Ship(size) ;
                for (int i=0; i<size;i++)
                {
                    gridList.get(y+i).get(x).setShip(ship);
                    if (i==0)
                    {
                        gridList.get(y+i).get(x).setBoatImage(imagesBateaux.get("front"));
                    }
                    else if(i == size-1)
                    {
                        gridList.get(y+i).get(x).setBoatImage(imagesBateaux.get("front"));
                        gridList.get(y+i).get(x).setImageRotation(180);
                    }
                    else
                    {
                        gridList.get(y+i).get(x).setBoatImage(imagesBateaux.get("body"));
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
                gridList.get(j).get(i).setBoatImage(imagesBateaux.get(null));
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
                temp=temp+" "+gridList.get(y).get(x);
            }
            temp=temp+"\n";
        }
        return temp;
    }



}
