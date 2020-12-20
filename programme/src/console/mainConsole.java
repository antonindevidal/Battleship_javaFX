package console;

import game.Board;
import game.NetworkPackageCoordinates;
import game.Game;
import javafx.beans.property.IntegerProperty;

import java.util.Random;
import java.util.Scanner;

public class mainConsole {
    public static void main(String[] args) {
        Scanner scanner =  new Scanner(System.in);
        while (true)
        {

            String str = scanner.nextLine();
            String[] splited = str.split("\\s+");
            NetworkPackageCoordinates c = new NetworkPackageCoordinates(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]), Boolean.parseBoolean(splited[2]));
            System.out.println(c);
        }
    }





}
