package controller;

import game.Cell;
import game.Game;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static java.lang.Math.floor;

public class GameView implements Initializable {
    public GridPane playerGrid;
    public GridPane computerGrid;
    public Label hints;
    public Label dialogu;
    public Button restartButton;

    private Game game;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                playerGrid.add(new StackPane(),j,i);
                computerGrid.add(new StackPane(),j,i);
            }
        }
    }

    public GameView() {
        game = new Game();


    }

    @FXML
    private void clickPlayerGrid(MouseEvent mouseEvent) {


        if( game.getPartOfGame()== Game.jeu.place && mouseEvent.isPrimaryButtonDown())
        {
            int x= (int)floor(mouseEvent.getX()/playerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/playerGrid.getWidth()*10);

            game.placeBoats(x, y, game.isHorizontal());
            hints.setText("Place your boats on the grid."+game.getNbBoatToPlace()+" boats left");

            setPlayerColors(playerGrid.getChildren());

            if(game.getNbBoatToPlace() <=0)
            {
                hints.setText("The game started. Click on the top grid to shoot");
            }
        }



    }

    @FXML
    private void clickComputerGrid(MouseEvent mouseEvent) {
        if (game.getPartOfGame() == Game.jeu.joue)
        {
            int x= (int)floor(mouseEvent.getX()/computerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/computerGrid.getWidth()*10);

            Game.shootResult sR =game.shoot(x, y);
            showPlayerShootResult(sR);

            setComputerColors(computerGrid.getChildren());
        }

    }

    private void showPlayerShootResult(Game.shootResult shootResult) {

        if(shootResult == Game.shootResult.alreadyHit)
            hints.setText("You have already shoot this case, play again");
        else if(shootResult == Game.shootResult.hit)
            hints.setText("You have hit a ship! play again");
        else if(shootResult == Game.shootResult.miss)
            hints.setText("You have missed your shot");
        else if(shootResult == Game.shootResult.sink)
            hints.setText("You have sinked a ship! play again");
        if(game.getPartOfGame()== Game.jeu.fin)
        {
            hints.setText("You win");
            restartButton.setVisible(true);
        }


    }

    @FXML
    private void screenClick(MouseEvent mouseEvent) {


        if (mouseEvent.isSecondaryButtonDown())
        {
            game.setHorizontal(!game.isHorizontal());
            if (game.isHorizontal())
            {
                dialogu.setText("Orientation: Horizontal\n(right click to change)");
            }
            else
                dialogu.setText("Orientation: Vertical\n (right click to change)");
        }

    }


    private void setPlayerColors(ObservableList<Node> children) {
        for(int x=0;x<10;x++)
        {
            for(int y=0;y<10;y++)
            {
                StackPane stackPane =(StackPane) children.get(y*10+x+1);
                Cell c =game.getPlayer().getCell(x,y);
                if (c.isHit() && c.hasShip())
                    stackPane.setStyle("-fx-background-color: #ff0000");
                else if (c.isHit() && !c.hasShip())
                    stackPane.setStyle("-fx-background-color: #0022ff");
                else if (!c.isHit() && c.hasShip())
                    stackPane.setStyle("-fx-background-color: #26941e");
            }
        }
    }

    private void setComputerColors(ObservableList<Node> children) {
        for(int x=0;x<10;x++)
        {
            for(int y=0;y<10;y++)
            {
                StackPane stackPane =(StackPane) children.get(y*10+x+1);
                Cell c =game.getOrdi().getCell(x,y);
                if (c.isHit() && c.hasShip())
                    stackPane.setStyle("-fx-background-color: #ff0000");
                else if (c.isHit() && !c.hasShip())
                    stackPane.setStyle("-fx-background-color: #0022ff");
                else if (!c.isHit() && c.hasShip())
                    stackPane.setStyle("-fx-background-color: #26941e");
            }
        }
    }


    public void restartAGame(MouseEvent mouseEvent) {
        game = new Game();
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                playerGrid.add(new StackPane(),j,i);
                computerGrid.add(new StackPane(),j,i);
            }
        }
        resetGridColor();
        restartButton.setVisible(false);


        hints.setText("Place your boats on the grid."+game.getNbBoatToPlace()+" boats left");
    }

    private void resetGridColor() {
        StackPane stackPane;
        for(int x=0;x<10;x++)
        {
            for(int y=0;y<10;y++)
            {
                stackPane =(StackPane) computerGrid.getChildren().get(y*10+x+1);
                stackPane.setStyle(null);
                stackPane = (StackPane) playerGrid.getChildren().get(y*10+x+1);
                stackPane.setStyle(null);
            }
        }
    }
}