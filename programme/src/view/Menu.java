package view;



import game.manager.ComputerManager;
import game.serialization.NormalGameSerialization;
import game.serialization.Serialization;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Client;
import network.Server;

import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Menu implements Initializable {

    /**
     * TextField for the code of the network Game
     */
    @FXML
    private TextField joinIp;

    /**
     *Button to join a network game
     */
    @FXML
    private Button joinButton;

    /**
     * RadioButton for normal Difficulty
     */
    @FXML
    private RadioButton RBmoyen;
    /**
     * RadioButton for easy Difficulty
     */
    @FXML
    private RadioButton RBfacile;

    /**
     * Button to start a game against computer
     */
    @FXML
    private Button playButton;

    /**
     * Label to display errors
     */
    @FXML
    private Label erreur;

    /**
     * Toggle group for the difficulty
     */
    @FXML
    private ToggleGroup difficulte;

    /**
     * Local ip of the player
     */
    private String ip;


    /**
     * Property for the error message
     */
    private StringProperty errorMessage = new SimpleStringProperty("");
        public String getErrorMessage() { return errorMessage.get(); }
        public StringProperty errorMessageProperty() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage.set(errorMessage); }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RBfacile.setToggleGroup(difficulte);
        RBmoyen.setToggleGroup(difficulte);

        erreur.textProperty().bindBidirectional(errorMessage);
        ip="";


        try(final DatagramSocket socket = new DatagramSocket()){  // get local ip address
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }


    }

    /**
     * Click on the button to create game against computer
     * @param actionEvent
     */
    @FXML
    private void boutonClick(ActionEvent actionEvent) {
        RadioButton rb = (RadioButton)difficulte.getSelectedToggle(); // get the selected difficulty
        GameViewComputer gvc = new GameViewComputer(); // Create a controller
        loadClassicView(actionEvent,gvc);

        gvc.setDifficulty(rb.getText() ); // set the computer difficulty


    }

    /**
     * Join a network game with the code on the textfield
     * @param actionEvent
     */
    @FXML
    private void clickJoinButton(ActionEvent actionEvent) {
        String code = joinIp.getText();
        String[] codeSliced;
        try
        {
            codeSliced = code.split(Pattern.quote("."));
            if(codeSliced.length != 3 )
            {
                throw new Exception();
            }

        }catch (Exception e)
        {
            setErrorMessage("Invalid code, must be an integer");
            return;
        }

        String ip = "192.168."+codeSliced[0]+"."+codeSliced[1]; // First two numbers -> end of ip address
        Client c = null;
        int port;
        try {
            port = Integer.parseInt(codeSliced[2]); //get the port from the code
        }catch (Exception e)
        {
            setErrorMessage("Invalid code, must be an integer");
            return;
        }

        try
        {
            c = new Client(ip,port); // Create a client to the server from the code
        }catch (Exception e)
        {
            setErrorMessage("Invalid code");
            return;
        }


        loadNetworkView("Battleship",actionEvent,c); // Load the view
    }


    /**
     * Create a network game
     * @param actionEvent
     */
    @FXML
    private void clickCreateServer(ActionEvent actionEvent) {
        Client c = null;
        Server serveur = new Server(0);// 0 checks for a free port and create a local server on the user computer
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                serveur.ClientConnection(); // Run the method that wait for clients
            }
        });

        int port = serveur.getServerPort();
        t.start();
        try {
            c = new Client(ip, port); //create a client on the server
        }
        catch (IOException e) {
            System.out.println("Can't connect to our own server");
            return;
        }


        String[] ipSliced = ip.split(Pattern.quote(".")); // get the two last number of our ip to ccreate the game code

        String title = "Battleship Code: "+ipSliced[ipSliced.length-2]+"."+ipSliced[ipSliced.length-1]+"."+port; // Code to join the server is in the window title

        loadNetworkView(title,actionEvent,c); // Load the view
    }

    /**
     * Load the network view
     * @param title     title of the window
     * @param actionEvent
     * @param c client to set to the controller
     */
    private void loadNetworkView(String title, ActionEvent actionEvent,Client c) { // Load the view with the network controller to play on LAN
        try {
            GameViewNetwork gv = new GameViewNetwork();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameViewV2.fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            loader.setController(gv);
            Scene sc = new Scene(loader.load());
            stage.getIcons().add(new Image("images/ph.gif"));
            stage.setScene(sc);
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());
            closeThisWindow(actionEvent);
            gv = loader.getController();

            gv.setC(c);
            c.setGv(gv);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() { // When the user close the game window
                @Override
                public void handle(WindowEvent windowEvent) {

                    c.sendCoordinates(-1,-1,false); // Send to the other player the end of connexion message
                    Platform.exit(); // Close window
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Load game window for game against computer
     * @param actionEvent
     * @param gvc   controller to set to the view
     */
    private void loadClassicView(ActionEvent actionEvent,GameViewComputer gvc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameViewV2.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Battleship");
            loader.setController(gvc); // Because we use different controller on the same view, we must set one
            Scene sc = new Scene(loader.load());
            stage.getIcons().add(new Image("images/ph.gif"));
            stage.setScene(sc);
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());
            closeThisWindow(actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

    /**
     * Close the window
     * @param actionEvent
     */
    private void closeThisWindow(ActionEvent actionEvent) // Close the current window
    {
        Stage s = (Stage) ((Node) (actionEvent.getSource())).getScene().getWindow();
        s.close();
    }

    /**
     * Click on button to load a game then load saved game
     * @param actionEvent
     */
    @FXML
    private void clickLoadGame(ActionEvent actionEvent) {
        Serialization serialization = new NormalGameSerialization();
        ComputerManager cm =new ComputerManager();
        try {
            cm = serialization.load();
        }catch (Exception e)
        {
            setErrorMessage(e.getMessage());
            return;
        }

        GameViewComputer gvc = new GameViewComputer(cm); // Create a controller
        loadClassicView(actionEvent,gvc);
    }
}
