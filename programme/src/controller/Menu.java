package controller;



import game.Computer.Computer;
import game.Computer.ComputerEasy;
import game.Computer.ComputerNormal;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class Menu implements Initializable {
    @FXML
    private RadioButton RBmoyen;
    @FXML
    private RadioButton RBfacile;
    @FXML
    private Button playButton;

    @FXML
    private ToggleGroup difficulte;


    private StringProperty buttonText = new SimpleStringProperty("Play");
        public String getButtonText() { return buttonText.get(); }
        public StringProperty buttonTextProperty() { return buttonText; }
        public void setButtonText(String buttonText) { this.buttonText.set(buttonText); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RBfacile.setToggleGroup(difficulte);
        RBmoyen.setToggleGroup(difficulte);

        playButton.textProperty().bindBidirectional(buttonText);

        difficulte.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (difficulte.getSelectedToggle() != null) {
                    RadioButton rb = (RadioButton)difficulte.getSelectedToggle();

                    setButtonText(rb.getText());
                }
            }
        });
    }

    @FXML
    private void boutonClick(ActionEvent actionEvent) throws Exception {
        RadioButton rb = (RadioButton)difficulte.getSelectedToggle();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Battleship");
            Scene sc = new Scene(loader.load());
            stage.getIcons().add(new Image("images/ph.gif"));
            stage.setScene(sc);
            GameView gv = loader.getController();
            gv.setDifficulty(rb.getText() );
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }catch (Exception e)
        {
            System.out.println(e);
        }

    }




}
