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
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/GameViewV2.fxml"));
            stage.getIcons().add(new Image("images/ph.gif"));
            Scene sc =new Scene(root);
            stage.setTitle("Battleship");
            stage.setScene(sc);
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());
        }catch (Exception e)
        {
            System.out.println(e);
        }




    }


    public static void main(String[] args) {
        launch(args);
    }
}
