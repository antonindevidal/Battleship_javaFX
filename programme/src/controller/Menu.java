package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Menu {
    public Label texte;
    public Button bouton;

    public void boutonClick(ActionEvent actionEvent) {
        texte.setText("ça marche");
    }
}
