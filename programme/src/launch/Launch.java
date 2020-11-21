package launch;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Launch extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/GameView.fxml"));
        stage.getIcons().add(new Image("images/ph.gif"));
        stage.setHeight(900);
        stage.setWidth(1000);
        stage.setTitle("Battleship");
        stage.setScene(new Scene(root,1000,1000));
        stage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }
}
