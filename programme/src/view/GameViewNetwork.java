package view;

import game.Board;
import game.Manager.ComputerManager;
import game.Manager.Manager;
import game.Manager.NetworkManager;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import network.Client;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.floor;

public class GameViewNetwork implements Initializable { // One of the two controller for GameViewV2.fxml

    @FXML
    private GridPane playerGrid;
    @FXML
    private GridPane computerGrid;
    @FXML
    private Label hints;
    @FXML
    private Label dialogu;
    @FXML
    private Label orientation;
    @FXML
    private Button restartButton;

    private int lastX=-1,lastY=-1;

    private Client c;
    private NetworkManager game;

    private Previsualisation previsualisation;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBoards();
    }

    public NetworkManager getGame() {
        return game;
    }

    private void bindGrid(GridPane gp, Board b) // Bind all the images on a board to the gridPane to display them when they change
    {
        ObservableList<Node> children = gp.getChildren();
        for(int x=0;x<10;x++)
        {
            for(int y=0;y<10;y++)
            {
                StackPane stackPane = (StackPane) children.get(y * 10 + x + 1);
                ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                imageView.imageProperty().bindBidirectional(b.getCell(x, y).imgProperty());
                imageView.rotateProperty().bindBidirectional(b.getCell(x, y).imageRotationProperty());
            }
        }
    }

    @FXML
    private void screenClick(MouseEvent mouseEvent) {
        if (mouseEvent.isSecondaryButtonDown()) // Change the orientation variable when you right click
        {
            game.setHorizontal(!game.isHorizontal());
        }
        previsualisation.refreshPrevisualisation(lastX,lastY);

    }
    public void setRestartButtonVisible()
    {
        restartButton.setVisible(true);
    }


    public void setC(Client c) { // Set a client because its the view for network
        this.c = c;

        c.receiveCoordinates(); // Start looking for messages from a ServerConnection
        game = c.getGame();
         // Set a restart button to display when a game end

        // Bind the two grid to the gridPane
        bindGrid(playerGrid,c.getGame().getMyBoard());
        bindGrid(computerGrid,c.getGame().getOtherPlayerBoard());

        previsualisation = new Previsualisation(playerGrid,game);
        previsualisation.createPrevisualisation(5);
        // Bind manager text to the labels
        dialogu.textProperty().bindBidirectional(game.texte1Property());
        hints.textProperty().bindBidirectional(game.texte2Property());
        orientation.textProperty().bindBidirectional(game.orientationPProperty());
    }

    @FXML
    private void clickPlayerGrid(MouseEvent mouseEvent) { // Click on the bottom grid
        if( game.getPartOfGame()== ComputerManager.jeu.place && mouseEvent.isPrimaryButtonDown()) // Check if can place boats
        {
            // Get coordinates from the click
            int x= (int)floor(mouseEvent.getX()/playerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/playerGrid.getWidth()*10);

            c.sendCoordinates(x,y,game.isHorizontal()); // Send the coords to the other player
            game.placeMyBoat(x,y, game.isHorizontal()); // Try to place the boat

            previsualisation.refreshPrevisualisation(x,y);

            System.out.println(game.getNbBoatToPlace());
            if (game.getNbBoatToPlace() ==0)
                previsualisation.destroyPrevisualisation(5);
        }

    }
    @FXML
    private void clickComputerGrid(MouseEvent mouseEvent) { // Click on the to grid
        if (game.getPartOfGame() == ComputerManager.jeu.joue && game.isPlayerTurn()) // Check if time to shoot
        {
            // Get coordinates from the click
            int x= (int)floor(mouseEvent.getX()/computerGrid.getHeight()*10);
            int y= (int)floor(mouseEvent.getY()/computerGrid.getWidth()*10);

            c.sendCoordinates(x,y,game.isHorizontal()); // Send the coords to the other player
            game.iShoot(x,y); // Shoot
            game.isEnding(); // Check if end of the game
        }
    }

    @FXML
    private void mainMenu(MouseEvent mouseEvent) { // Go back to the menu
        try
        {
            // Load the menu
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
        //Set an ImageView to all the grid cells (not on the fxml because there are 200 cells)
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                ImageView iv = new ImageView();
                iv.setFitHeight(30);
                iv.setFitWidth(30);
                StackPane sp = new StackPane();
                sp.getChildren().add(iv);
                playerGrid.add(sp, j, i);

                ImageView iv2 = new ImageView();
                iv2.setFitHeight(30);
                iv2.setFitWidth(30);
                StackPane sp2 = new StackPane();
                sp2.getChildren().add(iv2);
                computerGrid.add(sp2, j, i);
            }
        }
    }
    @FXML
    private void movedMouseOverPlayerGrid(MouseEvent mouseEvent) {
        int x = (int) floor(mouseEvent.getX() / computerGrid.getHeight() * 10);
        int y = (int) floor(mouseEvent.getY() / computerGrid.getWidth() * 10);

        if ((x == lastX && y == lastY) || game.getPartOfGame() != Manager.jeu.place)
            return;
        lastY = y;
        lastX = x;

        if (x >= 10) x = 9;
        if (y >= 10) y = 9;
        previsualisation.refreshPrevisualisation(x, y);


    }



}
