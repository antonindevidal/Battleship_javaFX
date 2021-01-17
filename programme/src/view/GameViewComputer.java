package view;

import com.sun.javafx.scene.layout.region.Margins;
import game.Board;
import game.Cell;
import game.computer.ComputerEasy;
import game.computer.ComputerNormal;
import game.Manager.ComputerManager;
import game.Manager.Manager;
import game.serialization.NormalGameSerialization;
import game.serialization.Serialization;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
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
import javafx.util.StringConverter;

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
    private Label orientation;
    @FXML
    private Button restartButton;

    private Previsualisation previsualisation;


    private ComputerManager game ;

    private int lastX = -1, lastY = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        previsualisation= new Previsualisation(playerGrid,game);

        // Bind the labels

        dialogu.textProperty().bind(game.texte1Property());

        hints.textProperty().bind(game.texte2Property());
        orientation.textProperty().bind(Bindings.format("Orientation: %s \n(right click to change)",game.orientationPProperty()));

        // Creates all stackPanels and imagesView for the grids
        setBoards();

        previsualisation.createPrevisualisation();

        // Bind the grids
        bindGrid(playerGrid, game.getMyBoard());
        bindGrid(computerGrid, game.getOtherPlayerBoard());


    }


    public GameViewComputer() {
        super();
        game = new ComputerManager();
    }
    public GameViewComputer(ComputerManager cm)
    {
        game = cm;

    }

    // Set the difficulty of the game
    public void setDifficulty(String difficulty) {
        switch (difficulty) {
            case "Facile":
                game.setComputer(new ComputerEasy());

                break;
            case "Moyen":
                game.setComputer(new ComputerNormal());

                break;
            default:
                game.setComputer(new ComputerEasy());

                break;
        }
    }

    @FXML
    private void screenClick(MouseEvent mouseEvent) {
        if (mouseEvent.isSecondaryButtonDown()) {
            game.setHorizontal(!game.isHorizontal()); // Change the orientation of the boat
        }
        previsualisation.refreshPrevisualisation(lastX, lastY);// Refresh the previsalisation to show the the new orientation
    }
    public void setRestartButtonVisible()
    {
        restartButton.setVisible(true);
    }


    @FXML
    private void clickPlayerGrid(MouseEvent mouseEvent) { // Bottom Grid click
        if (game.getPartOfGame() == ComputerManager.jeu.place && mouseEvent.isPrimaryButtonDown()) { // Check condition to place a boat

            // get coordinates of the mouse relative to the grid
            int x = (int) floor(mouseEvent.getX() / playerGrid.getHeight() * 10);
            int y = (int) floor(mouseEvent.getY() / playerGrid.getWidth() * 10);

            game.placeBoats(x, y, game.isHorizontal()); //Place the boat

            if (game.getNbBoatToPlace() <= 0) { // If there are no more boat to place
                previsualisation.destroyPrevisualisation();

                dialogu.setStyle("-fx-font: 24 Verdana;");

            }
        }


    }

    @FXML
    private void clickComputerGrid(MouseEvent mouseEvent) {
        if (game.getPartOfGame() == ComputerManager.jeu.joue && game.isPlayerTurn()) { // Check condition to shoot
            Thread threadComputer = new Thread() {
                public void run() {
                    while (!game.isPlayerTurn()) { // While it is the computer turn, it shoots
                        game.computerShoot();
                        if(game.isEnding()) // End
                        {
                            setRestartButtonVisible();
                        }
                    }
                }
            };
            // get coordinates of the mouse relative to the grid
            int x = (int) floor(mouseEvent.getX() / computerGrid.getHeight() * 10);
            int y = (int) floor(mouseEvent.getY() / computerGrid.getWidth() * 10);

            Manager.shootResult sR = game.playerShoot(x, y); // Player try to shoot


            threadComputer.start(); // Start computer turn to play

            if(game.isEnding())
            {
                setRestartButtonVisible();
            }


        }


    }

    /// For  a grid, it binds an imageView and the rotation of the image view for every cell
    private void bindGrid(GridPane gp, Board b) {
        ObservableList<Node> children = gp.getChildren();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                StackPane stackPane = (StackPane) children.get(y * 10 + x + 1);
                ImageView imageView = (ImageView) stackPane.getChildren().get(0);


                final Cell c = b.getCell(x,y);
                imageView.imageProperty().bind(Bindings.createObjectBinding(() ->{ if(c.getBoatImage() ==null) return null;return new Image(c.getBoatImage()); } ,c.boatImageProperty()));
                imageView.rotateProperty().bind(b.getCell(x, y).imageRotationProperty());
            }
        }
    }

    private void setBoards() {
        /// In every cell in the two grid, it creates a stack pane and an imageView
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                //For player grid
                ImageView iv = new ImageView();
                iv.setFitHeight(30);
                iv.setFitWidth(30);
                StackPane sp = new StackPane();
                sp.getChildren().add(iv);
                playerGrid.add(sp, j, i);

                // For computer grid
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
    private void mainMenu(MouseEvent mouseEvent) {
        // Go back to the main menu
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image("images/ph.gif"));
            Scene sc = new Scene(root);
            stage.setTitle("Battleship");
            stage.setScene(sc);
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());
            ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void movedMouseOverPlayerGrid(MouseEvent mouseEvent) {
        // get coordinates of the mouse relative to the grid
        int x = (int) floor(mouseEvent.getX() / computerGrid.getHeight() * 10);
        int y = (int) floor(mouseEvent.getY() / computerGrid.getWidth() * 10);

        if ((x == lastX && y == lastY) || game.getPartOfGame() != Manager.jeu.place) //check if the mouse is in the same cell
            return;
        lastY = y;
        lastX = x;

        if (x >= 10) x = 9; // Avoid out of range errors
        if (y >= 10) y = 9;
        previsualisation.refreshPrevisualisation(x, y); // Set the previsualisation


    }
    @FXML
    private void clickSaveButton(MouseEvent mouseEvent)
    {
        Serialization s = new NormalGameSerialization();

        s.save(game);
        mainMenu(mouseEvent);
    }


}