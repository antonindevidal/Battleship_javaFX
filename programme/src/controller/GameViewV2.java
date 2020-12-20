package controller;

import game.*;
import game.Computer.Computer;
import game.Computer.ComputerEasy;
import game.Computer.ComputerNormal;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Client;


import java.net.URL;
import java.nio.file.FileSystemAlreadyExistsException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.floor;

public class GameViewV2 implements Initializable {
    @FXML
    private GridPane playerGrid;
    @FXML
    private GridPane computerGrid;
    @FXML
    private Label hints;
    @FXML
    private Label dialogu;
    @FXML
    private Button restartButton;

    private Client c;
    private NetworkGame game;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        setBoards();

        c = new Client();
        c.receiveCoordinates();
        game = c.getGame();
        bindGrid(playerGrid,c.getGame().getMyBoard());
        bindGrid(computerGrid,c.getGame().getOtherPlayerBoard());

        dialogu.textProperty().bindBidirectional(game.texte1Property());
        hints.textProperty().bindBidirectional(game.texte2Property());

    }

    private void bindGrid(GridPane gp, Board b)
    {
        ObservableList<Node> children = gp.getChildren();
        for(int x=0;x<10;x++)
        {
            for(int y=0;y<10;y++)
            {
                ImageView imageView =(ImageView)children.get(y*10+x+1);
                imageView.imageProperty().bindBidirectional(b.getCell(x,y).imgProperty());
                imageView.rotateProperty().bindBidirectional(b.getCell(x,y).imageRotationProperty());
            }
        }
    }

    @FXML
    private void clickPlayerGrid(MouseEvent mouseEvent) {

        if( game.getPartOfGame()== Game.jeu.place && mouseEvent.isPrimaryButtonDown())
        {
            int x= (int)floor(mouseEvent.getX()/playerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/playerGrid.getWidth()*10);

            c.sendCoordinates(x,y,game.isHorizontal());
            game.placeMyBoat(x,y, game.isHorizontal());
        }

    }

    @FXML
    private void clickComputerGrid(MouseEvent mouseEvent) {
        if (game.getPartOfGame() == Game.jeu.joue && game.isPlayerTurn())
        {
            int x= (int)floor(mouseEvent.getX()/computerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/computerGrid.getWidth()*10);

            c.sendCoordinates(x,y,game.isHorizontal());
            game.iShoot(x,y);

            if(game.isEnding())
            {
                restartButton.setVisible(true);
            }
        }
    }


    @FXML
    private void screenClick(MouseEvent mouseEvent) {
        if (mouseEvent.isSecondaryButtonDown())
        {
            game.setHorizontal(!game.isHorizontal());
        }
    }



    @FXML
    private void restartAGame(MouseEvent mouseEvent) {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image("images/ph.gif"));
            Scene sc =new Scene(root);
            stage.setTitle("Battleship");
            stage.setScene(sc);
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());
            ((Node)(mouseEvent.getSource())).getScene().getWindow().hide();

        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void setBoards()
    {
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                ImageView iv = new ImageView();
                iv.setFitHeight(30);
                iv.setFitWidth(30);
                playerGrid.add(iv,j,i);
                ImageView iv2 = new ImageView();
                iv2.setFitHeight(30);
                iv2.setFitWidth(30);
                computerGrid.add(iv2,j,i);
            }
        }
    }

}
