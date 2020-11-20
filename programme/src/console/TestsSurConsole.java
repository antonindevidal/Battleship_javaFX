package Console;

import Game.Board;

public class TestsSurConsole {

    public static void creationBoard()
    {
        Board b = new Board();
        b.shoot(5,5);
        System.out.println(b);
    }
}
