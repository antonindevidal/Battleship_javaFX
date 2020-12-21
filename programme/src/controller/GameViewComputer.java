package controller;

import game.Board;
import game.Computer.ComputerEasy;
import game.Computer.ComputerNormal;
import game.Manager.ComputerManager;
import javafx.application.Platform;
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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.floor;

public class GameViewComputer implements Initializable {
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

    private ComputerManager game  = new ComputerManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dialogu.textProperty().bindBidirectional(game.texte2Property());
        hints.textProperty().bindBidirectional(game.texte2Property());
        setBoards();

        bindGrid(playerGrid,game.getMyBoard());
        bindGrid(computerGrid,game.getOtherPlayerBoard());
    }
    public GameViewComputer() {
        super();
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
    private void screenClick(MouseEvent mouseEvent) {
        if (mouseEvent.isSecondaryButtonDown())
        {
            game.setHorizontal(!game.isHorizontal());
        }
    }


    @FXML
    private void clickPlayerGrid(MouseEvent mouseEvent) {
        if( game.getPartOfGame()== ComputerManager.jeu.place && mouseEvent.isPrimaryButtonDown())
        {
            int x= (int)floor(mouseEvent.getX()/playerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/playerGrid.getWidth()*10);

            game.placeBoats(x, y, game.isHorizontal());

            if(game.getNbBoatToPlace() <=0)
            {
                dialogu.setStyle("-fx-font: 24 arial;");
            }
        }


    }

    @FXML
    private void clickComputerGrid(MouseEvent mouseEvent) {
        if (game.getPartOfGame() == ComputerManager.jeu.joue && game.isPlayerTurn())
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
                            game.setTexte1("Your turn");
                        }
                    });
                }
            };


            int x= (int)floor(mouseEvent.getX()/computerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/computerGrid.getWidth()*10);

            ComputerManager.shootResult sR =game.playerShoot(x, y);



            threadComputer.start();

            game.isEnding();

        }

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

}