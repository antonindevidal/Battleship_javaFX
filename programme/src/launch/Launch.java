package launch;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launch extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        stage.getIcons().add(new Image("images/ph.gif"));
        stage.setHeight(500);
        stage.setWidth(500);
        stage.setTitle("Battleship");
        stage.setScene(new Scene(root,500,500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
