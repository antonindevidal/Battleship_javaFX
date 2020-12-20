package console;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import network.Client;

import java.io.IOException;
import java.util.Scanner;

public class ClientConsole extends Application {




    public static void main(String[] args) {
        launch(args);

    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World!");
        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

        Client c = new Client();
        c.receiveCoordinates();
        c.sendCoordinates();
    }
}
