package controller;



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
import jdk.jshell.spi.ExecutionControl;
import network.Client;
import network.Server;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.net.*;
import java.security.spec.ECField;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

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

    private StringProperty myIp= new SimpleStringProperty("Your IP address: No Ip adress");
        public String getMyIp() { return myIp.get(); }
        public StringProperty myIpProperty() { return myIp; }
        public void setMyIp(String myIp) { this.myIp.set(myIp); }

    private StringProperty buttonText = new SimpleStringProperty("Play");
        public String getButtonText() { return buttonText.get(); }
        public StringProperty buttonTextProperty() { return buttonText; }
        public void setButtonText(String buttonText) { this.buttonText.set(buttonText); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RBfacile.setToggleGroup(difficulte);
        RBmoyen.setToggleGroup(difficulte);

        playButton.textProperty().bindBidirectional(buttonText);
        erreur.textProperty().bindBidirectional(errorMessage);
        ip="";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        setMyIp("Your IP address: "+ ip);
        difficulte.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
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
        RadioButton rb = (RadioButton)difficulte.getSelectedToggle();
        //GameViewComputer gv = new GameViewComputer();
        //FXMLLoader loader = loadView("Battleship","/fxml/GameViewV2.fxml",actionEvent);
        //gv = loader.getController();

        //gv.setDifficulty(rb.getText() );
        try {
            GameViewComputer gv = new GameViewComputer();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameViewV2.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Battleship");
            loader.setController(gv);
            Scene sc = new Scene(loader.load());
            stage.getIcons().add(new Image("images/ph.gif"));
            stage.setScene(sc);
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            gv.setDifficulty(rb.getText() );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void clickJoinButton(ActionEvent actionEvent) {
        String code = joinIp.getText();
        String ip = "192.168.1."+code.substring(0,2);
        Client c = null;
        int port;
        try {
            port = Integer.parseInt(code.substring(2));
        }catch (Exception e)
        {
            setErrorMessage("Code invalide, le code est un nombre");
            return;
        }

        try
        {
            c = new Client(ip,port);
        }catch (IOException e)
        {
            setErrorMessage("Code invalide");
            return;
        }


        try {
            System.out.println(ip + " port "+ port);
            GameViewNetwork gv = new GameViewNetwork();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameViewV2.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Battleship");
            loader.setController(gv);
            Scene sc = new Scene(loader.load());
            stage.getIcons().add(new Image("images/ph.gif"));
            stage.setScene(sc);
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            gv = loader.getController();
            gv.setC(c);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }


    }

    public void clickCreateServer(ActionEvent actionEvent) {
        Client c = null;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                serveur.ClientConnection();
            }
        });
        serveur = new Server(0); // 0 pour chercher automatiquement un port libre
        int port = serveur.getServerPort();
        t.start();
        try {
            c = new Client(ip, port);
        }
        catch (IOException e) {
            return;
        }

        try {
            GameViewNetwork gv = new GameViewNetwork();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameViewV2.fxml"));
            Stage stage = new Stage();

            String ipSliced = ip.substring(10);
            stage.setTitle("Battleship game Number: "+ipSliced+""+port);
            loader.setController(gv);
            Scene sc = new Scene(loader.load());
            stage.getIcons().add(new Image("images/ph.gif"));
            stage.setScene(sc);
            gv = loader.getController();
            gv.setC(c);
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    Platform.exit();
                    System.exit(0);
                }
            });

            Stage s = (Stage)((Node)(actionEvent.getSource())).getScene().getWindow();
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private FXMLLoader loadView(String title, String view, ActionEvent actionEvent)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene sc = new Scene(loader.load());
            stage.getIcons().add(new Image("images/ph.gif"));
            stage.setScene(sc);
            stage.show();
            stage.setHeight(sc.getHeight());
            stage.setWidth(sc.getWidth());
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            return loader;

        }catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }
}
