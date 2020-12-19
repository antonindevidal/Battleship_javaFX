package controller;

import game.Board;
import game.Cell;
import game.Computer.Computer;
import game.Computer.ComputerEasy;
import game.Computer.ComputerNormal;
import game.Game;
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


import java.net.URL;
import java.nio.file.FileSystemAlreadyExistsException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.floor;

public class GameView implements Initializable {
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

    private Game game = new Game();


    private StringProperty texte = new SimpleStringProperty("Orientation: Horizontal\n(right click to change)");
        private String getTexte() {
            return texte.get();
        }
        private StringProperty texteProperty() {
            return texte;
        }
        private void setTexte(String texte) {
            this.texte.set(texte);
        }

    private StringProperty hintsText = new SimpleStringProperty("Place your boats on the grid.5 boats left");
        private String getHintsText() {
            return hintsText.get();
        }
        private StringProperty hintsTextProperty() {
            return hintsText;
        }
        private void setHintsText(String texte) {
            this.hintsText.set(texte);
        }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dialogu.textProperty().bindBidirectional(texteProperty());
        hints.textProperty().bindBidirectional(hintsTextProperty());
        setBoards();

        bindGrid(playerGrid,game.getPlayer());
        bindGrid(computerGrid,game.getOrdi());

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


    public void setDifficulty(String difficulty)
    {
        switch (difficulty)
        {
            case "Facile":
                game.setComputer(new ComputerEasy());;
                break;
            case "Moyen":
                game.setComputer(new ComputerNormal());;
                break;
            default:
                game.setComputer(new ComputerEasy());;
                break;
        }
    }

    @FXML
    private void clickPlayerGrid(MouseEvent mouseEvent) {


        if( game.getPartOfGame()== Game.jeu.place && mouseEvent.isPrimaryButtonDown())
        {
            int x= (int)floor(mouseEvent.getX()/playerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/playerGrid.getWidth()*10);

            game.placeBoats(x, y, game.isHorizontal());
            setHintsText("Place your boats on the grid."+game.getNbBoatToPlace()+" boats left");

            //setPlayerColors();

            if(game.getNbBoatToPlace() <=0)
            {
                dialogu.setStyle("-fx-font: 24 arial;");

                setHintsText("The game started. Click on the top grid to shoot");
                setTexte("Your turn");
            }
        }



    }

    @FXML
    private void clickComputerGrid(MouseEvent mouseEvent) {
        if (game.getPartOfGame() == Game.jeu.joue && game.isPlayerTurn())
        {
            Thread threadComputer = new Thread(){
                public void run()
                {
                    while(!game.isPlayerTurn())
                    {
                        game.computerShoot();

                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            setTexte("Your turn");
                        }
                    });
                }
            };


            int x= (int)floor(mouseEvent.getX()/computerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/computerGrid.getWidth()*10);

            Game.shootResult sR =game.playerShoot(x, y);

            showPlayerShootResult(sR);

            setTexte("Computer turn");
            threadComputer.start();

            if(game.getPartOfGame()== Game.jeu.fin)
            {

                if (game.getOrdi().hasLost())
                {
                    setTexte("You win");
                }
                else if (game.getOrdi().hasLost())
                {
                    setTexte("You lose");
                }

                restartButton.setVisible(true);
            }

        }

    }

    private void showPlayerShootResult(Game.shootResult shootResult) {

        if(shootResult == Game.shootResult.alreadyHit)
            setHintsText("You have already shoot this case, play again");
        else if(shootResult == Game.shootResult.hit)
            setHintsText("You have hit a ship! play again");
        else if(shootResult == Game.shootResult.miss)
            setHintsText("You have missed your shot");
        else if(shootResult == Game.shootResult.sink)
            setHintsText("You have sinked a ship! play again");

    }

    @FXML
    private void screenClick(MouseEvent mouseEvent) {


        if (mouseEvent.isSecondaryButtonDown())
        {
            game.setHorizontal(!game.isHorizontal());
            if (game.isHorizontal())
            {
                setTexte("Orientation: Horizontal\n(right click to change)");
            }
            else
                setTexte("Orientation: Vertical\n (right click to change)");
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
