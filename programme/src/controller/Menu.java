package controller;



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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RBfacile.setToggleGroup(difficulte);
        RBmoyen.setToggleGroup(difficulte);


        difficulte.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (difficulte.getSelectedToggle() != null) {
                    RadioButton rb = (RadioButton)difficulte.getSelectedToggle();

                    playButton.setText(rb.getText());
                }
            }
        });
    }

    @FXML
    private void boutonClick(ActionEvent actionEvent) throws Exception {


        try {
            Parent p = FXMLLoader.load(getClass().getResource("/fxml/GameView.fxml"));
            
            Stage stage = new Stage();
            stage.setTitle("Battleship");
            stage.setHeight(900);
            stage.setWidth(1000);
            stage.setScene(new Scene(p));
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }catch (Exception e)
        {
            System.out.println(e);
        }

    }





}
