package game.computer;

import game.Board;
import game.Coordinates;
import game.manager.ComputerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Extend abstract class Computer
 *
 * Normal version of a computer
 */
public class ComputerNormal extends Computer {

    /**
     * possible orientation of a boat
     */
    private enum orientation{vertical,horizontal,neSaisPas}

    /**
     * some coordinates used as memory for last shoot
     */
    private Coordinates lastShoot =new Coordinates(),lastHit=new Coordinates(), boatFirstPos=new Coordinates();

    /**
     * Result of the last shoot
     */
    private ComputerManager.shootResult lastShootResult = ComputerManager.shootResult.miss;

    /**
     * true if the it has a target
     */
    private boolean hasATarget;

    /**
     * true if this has to find a boat on positive
     */
    private boolean findOnPositive =true;

    /**
     * orientation of the targeted boat
     */
    private orientation boatOrientation=orientation.neSaisPas;


    /**
     * Place boat randomly on the board
     * @param board board to place boats onto
     */
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

                placed=board.placeShip(x,y,boatToPlace.get(0),h,false);
            }
            placed=false;
            boatToPlace.remove(0);
        }
    }

    /**
     * shoot cleverly on the board
     *
     * - if it has no target, shoot randomly
     *
     * - if has a target, look for the rest of the boat, find the orientation of the boat and shoot on this boat until it sink
     * @param board board to shoot on
     * @return result of the shoot
     */
    @Override
    public ComputerManager.shootResult shoot(Board board) {

        ComputerManager.shootResult  res;
        if (!hasATarget) // Si l'ordinateur n'a pas de cible on tire aléatoirement
        {
            res = randShoot(board);
            if(res == ComputerManager.shootResult.hit) {
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
                case vertical:  // Si le bateau est vertical
                    shootVertical(board);
                    break;
                case horizontal: // Si le bateau est horizontal
                    shootHorizontal(board);
                    break;
                case neSaisPas:     // Si on ne connait pas l'orientation du bateau
                    adjaShoot(board,lastHit.getX(),lastHit.getY());

                    if (lastShootResult == ComputerManager.shootResult.hit)
                        setLasthit(lastShoot.getX(),lastShoot.getY());
                        guessOrientation();
                    break;
            }
            if (lastShootResult == ComputerManager.shootResult.sink) // Si l'ordinateur à coulé le bateau
            {
                hasATarget =false; // L'ordinateur n'a plus de cible
                boatOrientation=orientation.neSaisPas;
            }else if (lastShootResult == ComputerManager.shootResult.hit) // Si l'ordinateur à touché le bateau
            {
                setLasthit(lastShoot.getX(),lastShoot.getY()); // On met le dernier tire dans la variable du dernier tire touché
            }

        }

        return lastShootResult;
    }

    /**
     * Shoot randomly
     * @param board board to shoot on
     * @return
     */
    private ComputerManager.shootResult randShoot(Board board) // Tire aléatoire
    {
        Random r = new Random();
        int x=0,y=0;

        ComputerManager.shootResult sR = ComputerManager.shootResult.alreadyHit;

        while( sR == ComputerManager.shootResult.alreadyHit)
        {
            x=r.nextInt((board.gridSize-1) + 1) ;
            y=r.nextInt((board.gridSize-1) + 1) ;

            sR = board.shoot(x,y);
        }
        setLastShoot(x,y);
        lastShootResult=sR;
        return sR;
    }

    /**
     * Shoot randomly on an adjacent cell
     * @param board board to shoot on
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     */
    private void adjaShoot(Board board, int x, int y) // Tire autour de la case
    {
        Random r = new Random();
        int cote,paddingx=0,paddingy=0;


        ComputerManager.shootResult sR = ComputerManager.shootResult.alreadyHit;
        while ( sR == ComputerManager.shootResult.alreadyHit) // Tant que l'on a déjà touché le bateau
        {
            paddingx=0;paddingy=0;
            cote = r.nextInt((4-1) + 1); // On génère un nombre aléatoire entre 0 et 4 pour déterminer de quel coté on va tirer
            switch (cote){
                case 0: // On cherche à droite de la case
                    if (x+1 <10)
                        paddingx=1;
                    break;
                case 1:// On cherche à gauche de la case
                    if (x-1 >=0)
                        paddingx=-1;
                    break;
                case 2:// On cherche en bas de la case
                    if (y+1 <10)
                        paddingy=1;
                    break;
                default:// On cherche en haut de la case
                    if (y-1 >=0)
                        paddingy=-1;
                    break;
            }
            sR = board.shoot(x+paddingx,y+paddingy);
        }

        setLastShoot(x+paddingx,y+paddingy);
        lastShootResult=sR;
    }

    /**
     * Check if all cells around are shoot
     * @param board board to test
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     * @return true if all cells around have been shoot
     */
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

    /**
     * Guess orientation of the boat targeted with the two last shoot
     */
    private void guessOrientation()
    {
        int paddingX,paddingY;
        paddingX =lastHit.getX() -boatFirstPos.getX() ;
        paddingY =lastHit.getY() -boatFirstPos.getY() ;



        if(paddingX != 0)
        {
            boatOrientation= orientation.horizontal;
            if (paddingX >0)
                findOnPositive=true;
            else
                findOnPositive=false;
        }
        else if(paddingY != 0)
        {
            boatOrientation= orientation.vertical;
            if (paddingY >0)
                findOnPositive=true;
            else
                findOnPositive=false;
        }
    }

    /**
     * Shoot horizontally on the board on the targeted boat
     * @param board board to shoot on
     */
    private void shootHorizontal(Board board)
    {
        int nextX = lastHit.getX() ;
        if(lastShootResult == ComputerManager.shootResult.miss)
        {
            findOnPositive = !findOnPositive;
            setLasthit(boatFirstPos.getX(),boatFirstPos.getY());
        }
        else if(findOnPositive && (nextX<9 &&board.getCell(nextX+1,lastHit.getY()).isHit()) || nextX ==9) {
            findOnPositive = !findOnPositive;
            setLasthit(boatFirstPos.getX(), boatFirstPos.getY());
        }
        else if(!findOnPositive && (nextX>0 && board.getCell(nextX-1,lastHit.getY()).isHit()) || nextX ==0)
        {
            findOnPositive = !findOnPositive;
            setLasthit(boatFirstPos.getX(),boatFirstPos.getY());
        }


        if (findOnPositive && nextX<9)
        {
            nextX++;

        }
        else if (!findOnPositive && nextX>0)
        {
            nextX--;
        }


        lastShootResult = board.shoot(nextX,lastHit.getY());
        setLastShoot(nextX,lastHit.getY());
    }
    /**
     * Shoot vertically on the board on the targeted boat
     * @param board board to shoot on
     */
    private void shootVertical(Board board)
    {
        int nextY = lastHit.getY() ;
        if(lastShootResult == ComputerManager.shootResult.miss)
        {
            findOnPositive = !findOnPositive;
            setLasthit(boatFirstPos.getX(),boatFirstPos.getY());
        }

        if (findOnPositive && nextY <9)
            nextY++;
        else if(!findOnPositive && nextY>0)
            nextY--;


        lastShootResult = board.shoot(lastHit.getX(),nextY);
        setLastShoot(lastHit.getX(),nextY);
    }


}
