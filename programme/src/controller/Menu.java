package controller;



import game.Computer.Computer;
import game.Computer.ComputerEasy;
import game.Computer.ComputerNormal;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import network.Client;
import network.Server;

import java.net.*;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class Menu implements Initializable {
    @FXML
    private Label createIp;
    @FXML
    private TextField createPort;
    @FXML
    private TextField joinIp;
    @FXML
    private TextField joinPort;
    @FXML
    private Button joinButton;
    @FXML
    private RadioButton RBmoyen;
    @FXML
    private RadioButton RBfacile;
    @FXML
    private Button playButton;

    @FXML
    private ToggleGroup difficulte;

    private String ip;

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
        createIp.textProperty().bindBidirectional(myIpProperty());
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
    private void boutonClick(ActionEvent actionEvent) throws Exception {
        RadioButton rb = (RadioButton)difficulte.getSelectedToggle();

        FXMLLoader loader = loadView("Battleship","/fxml/GameView.fxml",actionEvent);
        GameView gv = loader.getController();
        gv.setDifficulty(rb.getText() );
    }


    public void clickJoinButton(ActionEvent actionEvent) {
        int port = Integer.parseInt(joinPort.getText());
        if(port >1024 && port  < 49151)
        {
            FXMLLoader loader = loadView("Battleship","/fxml/GameViewV2.fxml",actionEvent);
            GameViewV2 gv = loader.getController();
            gv.setC(new Client(joinIp.getText(),port));
        }

    }

    public void clickCreateeServer(ActionEvent actionEvent) {
        int port = Integer.parseInt(createPort.getText());
        if(port >1024 && port  < 49151)
        {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Server s = new Server(port);
                    s.ClientConnection();
                }
            });
            t.start();

            FXMLLoader loader = loadView("Battleship","/fxml/GameViewV2.fxml",actionEvent);
            GameViewV2 gv = loader.getController();
            gv.setC(new Client(ip,port));
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
