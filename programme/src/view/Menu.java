package view;



import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.net.*;
import java.security.spec.ECField;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;

public class Menu implements Initializable {

    @FXML
    private TextField joinIp;
    @FXML
    private Button joinButton;
    @FXML
    private RadioButton RBmoyen;
    @FXML
    private RadioButton RBfacile;
    @FXML
    private Button playButton;
    @FXML
    private Label erreur;

    @FXML
    private ToggleGroup difficulte;

    private String ip;

    private Server serveur;

    private StringProperty errorMessage = new SimpleStringProperty("");
        public String getErrorMessage() { return errorMessage.get(); }
        public StringProperty errorMessageProperty() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage.set(errorMessage); }

    private StringProperty buttonText = new SimpleStringProperty("Play");
        public String getButtonText() { return buttonText.get(); }
        public StringProperty buttonTextProperty() { return buttonText; }
        public void setButtonText(String buttonText) { this.buttonText.set(buttonText); }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        RBfacile.setToggleGroup(difficulte); // Set toogles button in the same group
        RBmoyen.setToggleGroup(difficulte);

        playButton.textProperty().bindBidirectional(buttonText);// Bind messages

        erreur.textProperty().bindBidirectional(errorMessage);
        ip="";


        try(final DatagramSocket socket = new DatagramSocket()){  // get local ip address
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }



        difficulte.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {  // Listen when change the radioButton
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (difficulte.getSelectedToggle() != null) {
                    RadioButton rb = (RadioButton)difficulte.getSelectedToggle();

                    setButtonText(rb.getText());
                }
            }
        });
    }

    @FXML
    private void boutonClick(ActionEvent actionEvent) {
        RadioButton rb = (RadioButton)difficulte.getSelectedToggle(); // get the selected difficulty



        try {
            GameViewComputer gvc = new GameViewComputer(); // Create a controller
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
            gvc.setDifficulty(rb.getText() ); // set the computer difficulty
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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


    @FXML
    private void clickCreateServer(ActionEvent actionEvent) {
        Client c = null;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                serveur.ClientConnection(); // Run the method that wait for clients
            }
        });
        serveur = new Server(0); // 0 checks for a free port and create a local server on the user computer
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

    private void closeThisWindow(ActionEvent actionEvent) // Close the current window
    {
        Stage s = (Stage) ((Node) (actionEvent.getSource())).getScene().getWindow();
        s.close();
    }

}
