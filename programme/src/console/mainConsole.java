package console;

import game.Board;
import game.Game;

public class mainConsole {
    public static void main(String[] args) {
        Game g = new Game();

        g.placeBoats(0,0,true);
        g.placeBoats(0,1,true);
        g.placeBoats(0,2,true);
        g.placeBoats(0,3,true);
        g.placeBoats(0,4,true);
        g.placeBoats(0,5,true);

        System.out.println(g.getPlayer());
    }





}
