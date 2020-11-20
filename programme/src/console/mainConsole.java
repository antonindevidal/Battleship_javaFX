package console;

import game.Board;

public class mainConsole {
    public static void main(String[] args) {
        Board b = new Board();
        System.out.println(b);
        boolean test=b.placeShip(0,0,3,true);
        b.placeShip(9,7,3,false);
        System.out.println(b);
    }





}
