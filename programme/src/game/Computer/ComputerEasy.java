package game.Computer;

import game.Board;
import game.Computer.Computer;
import game.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/*
 Classe pour ordinateur facile.

  Place ses bateaux aléatoirement et tire aléatoirement
 */
public class ComputerEasy extends Computer {

    @Override
    public void placeBoats(Board board) {
        Random r = new Random();
        int x,y;
        boolean placed = false,h;
        List<Integer> boatToPlace=new ArrayList<Integer>(); // Liste des tailles des bateaux à placer
        boatToPlace.add(5);
        boatToPlace.add(4);
        boatToPlace.add(3);
        boatToPlace.add(3);
        boatToPlace.add(2);

        while(boatToPlace.size() >0) // Tant qu'il y a des bateaux à placer
        {
            while(!placed) // tant que l'on a pas pu placer le bateau
            {
                //  On tire des valeurs x et y aléatoires entre 0 et 9 compris
                 x=r.nextInt((board.gridSize-1) + 1) ;
                 y=r.nextInt((board.gridSize-1) + 1) ;

                 if (x+y%2 == 0) // Si la somme de x et y est un multiple de 2, le bateau sera placé horizontalement
                     h=true;
                 else
                     h=false;
                 placed=board.placeShip(x,y,boatToPlace.get(0),h); // On tente de placer le bateau ici


            }
            placed=false;
            boatToPlace.remove(0); // On enlève le bateau que l'on vient de placer de la liste
        }

    }


    @Override
    public Game.shootResult shoot(Board board) {
        Random r = new Random();
        int x=0,y=0;

        Game.shootResult sR = Game.shootResult.alreadyHit;

        while( sR == Game.shootResult.alreadyHit) // tant que l'on a déjà tiré sur la case en question
        {
            // On tire un x et y aléatoire
            x=r.nextInt((board.gridSize-1) + 1) ;
            y=r.nextInt((board.gridSize-1) + 1) ;

            sR = board.shoot(x,y); // On tire
        }

        return sR; // On retourne le résultat du tire
    }
}
