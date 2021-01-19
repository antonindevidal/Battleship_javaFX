package view;

import game.Board;
import game.manager.ComputerManager;
import game.manager.Manager;
import game.manager.NetworkManager;
import javafx.beans.binding.Bindings;
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
import javafx.util.StringConverter;
import network.Client;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.floor;
/**
 * Controller for GameViewV2 and  game against another player
 */
public class GameViewNetwork implements Initializable { // One of the two controller for GameViewV2.fxml

    /**
     * grid at the bottom on the view
     */
    @FXML
    private GridPane playerGrid;
    /**
     * grid at the top on the view
     */
    @FXML
    private GridPane computerGrid;
    /**
     * label that display hints
     */
    @FXML
    private Label hints;
    /**
     * Label that display some infomatioons
     */
    @FXML
    private Label dialogu;
    /**
     * Diplay the orientation of the boat ot place
     */
    @FXML
    private Label orientation;
    /**
     * button used to go back to the menu
     */
    @FXML
    private Button restartButton;

    /**
     * previsualize boats when placed
     */
    private Previsualisation previsualisation;

    /**
     * Button used to save the game
     */
    @FXML
    private Button saveButton;
    /**
     * Where the mouse was on the playerGrid
     */
    private int lastX=-1,lastY=-1;

    /**
     * Client for the player
     */
    private Client c;
    /**
     * Manager for the game
     */
    private NetworkManager game;



    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBoards();
        saveButton.setVisible(false); // disable the button to save the game
    }

    public NetworkManager getGame() {
        return game;
    }
    /**
     * For  a grid, it binds an imageView and the rotation of the image view for every cell
     * @param gp gridPane to bind
     * @param b board to bind with the grid pane
     */
    private void bindGrid(GridPane gp, Board b) // Bind all the images on a board to the gridPane to display them when they change
    {
        ObservableList<Node> children = gp.getChildren();
        for(int x=0;x<10;x++)
        {
            for(int y=0;y<10;y++)
            {
                StackPane stackPane = (StackPane) children.get(y * 10 + x + 1);
                ImageView imageView = (ImageView) stackPane.getChildren().get(0);

                StringConverter<Image> sc = new StringConverter<Image>() {
                    @Override
                    public String toString(Image o) { return null; }
                    @Override
                    public Image fromString(String s) { return new Image(s); }
                };

                Bindings.bindBidirectional(b.getCell(x,y).boatImageProperty(),imageView.imageProperty(),sc);

                imageView.rotateProperty().bind(b.getCell(x, y).imageRotationProperty());
            }
        }
    }


    /**
     * When player click on the the window
     * @param mouseEvent
     */
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


    /**
     * Set the client for the game
     * @param c Client to set
     */
    public void setC(Client c) { // Set a client because its the view for network
        this.c = c;

        c.receiveCoordinates(); // Start looking for messages from a ServerConnection
        game = c.getGame();
         // Set a restart button to display when a game end

        // Bind the two grid to the gridPane
        bindGrid(playerGrid,c.getGame().getMyBoard());
        bindGrid(computerGrid,c.getGame().getOtherPlayerBoard());

        previsualisation = new Previsualisation(playerGrid,game);
        previsualisation.createPrevisualisation();
        // Bind manager text to the labels
        dialogu.textProperty().bind(game.texte1Property());
        hints.textProperty().bind(game.texte2Property());
        orientation.textProperty().bind(Bindings.format("Orientation: %s \n(right click to change)",game.orientationPProperty()));
    }

    /**
     * Click on playerGrid
     * @param mouseEvent
     */
    @FXML
    private void clickPlayerGrid(MouseEvent mouseEvent) { // Click on the bottom grid
        if( game.getPartOfGame()== ComputerManager.jeu.place && mouseEvent.isPrimaryButtonDown()) // Check if can place boats
        {
            // Get coordinates from the click
            int x= getXcoord(mouseEvent,playerGrid);
            int y= getYcoord(mouseEvent,playerGrid);

            c.sendCoordinates(x,y,game.isHorizontal()); // Send the coords to the other player
            game.placeMyBoat(x,y, game.isHorizontal()); // Try to place the boat

            previsualisation.refreshPrevisualisation(x,y);

            if (game.getNbBoatToPlace() ==0)
                previsualisation.destroyPrevisualisation();
        }

    }

    /**
     * Click on the computerGrid
     * @param mouseEvent
     */
    @FXML
    private void clickComputerGrid(MouseEvent mouseEvent) { // Click on the to grid
        if (game.getPartOfGame() == ComputerManager.jeu.joue && game.isPlayerTurn()) // Check if time to shoot
        {
            // Get coordinates from the click
            int x= getXcoord(mouseEvent,computerGrid);
            int y= getYcoord(mouseEvent,computerGrid);

            c.sendCoordinates(x,y,game.isHorizontal()); // Send the coords to the other player
            game.iShoot(x,y); // Shoot
            if(game.isEnding()) // Check if end of the game
            {
                setRestartButtonVisible();
            }
        }
    }


    /**
     * Get the case where the mouse is on a grid
     * @param mouseEvent
     * @param gp grid where we get the coordinates
     * @return the case number where the mouse is
     */
    private int getYcoord(MouseEvent mouseEvent, GridPane gp)
    {
        int y= (int)floor(mouseEvent.getY()/gp.getWidth()*10);
        if (y >= 10) y = 9;
        else if (y < 0) y=0;
        return y;
    }


    /**
     * Get the case where the mouse is on a grid
     * @param mouseEvent
     * @param gp grid where we get the coordinates
     * @return the case number where the mouse is
     */
    private int getXcoord(MouseEvent mouseEvent, GridPane gp)
    {
        int x= (int)floor(mouseEvent.getX()/gp.getHeight()*10);
        if (x >= 10) x = 9;
        else if (x < 0) x=0;
        return x;
    }

    /**
     * Go back to the main menu
     * @param mouseEvent
     */
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


    /**
     * Prepare boards for a game
     */
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
    /**
     * When mouse move over the playerGrid
     * @param mouseEvent
     */
    @FXML
    private void movedMouseOverPlayerGrid(MouseEvent mouseEvent) {
        // get coordinates of the mouse relative to the grid
        int x = getXcoord(mouseEvent,computerGrid);
        int y = getYcoord(mouseEvent,computerGrid);

        if ((x == lastX && y == lastY) || game.getPartOfGame() != Manager.jeu.place) //check if the mouse is in the same cell
            return;
        lastY = y;
        lastX = x;

        if (x >= 10) x = 9; // Avoid out of range errors
        if (y >= 10) y = 9;
        previsualisation.refreshPrevisualisation(x, y); // Set the previsualisation


    }

    /**
     * The button is disable but must have a method because the ide is not happy
     * @param mouseEvent
     */
    @FXML
    private void clickSaveButton(MouseEvent mouseEvent)
    {
    }


}
