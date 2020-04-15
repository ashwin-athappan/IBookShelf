package LibraryModule.UI.Login;

import LibraryModule.UI.Settings.Preferences;
import LibraryModule.UI.Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public JFXTextArea UName;
    public JFXButton login;
    public JFXButton cancel;
    public Label Label;
    public JFXPasswordField pass;
    public Circle closeButton;
    public Circle maximizeButton;
    public Circle minimizeButton;

    private double xOffset = 0;
    private double yOffset = 0;

    Preferences pref;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pref = Preferences.getPreference();
    }

    public void handleLogin(ActionEvent event) {
        Label.setText("Library Login");
        Label.setStyle("-fx-background-color: #222831");
        String userName = UName.getText();
        String password = DigestUtils.sha1Hex(pass.getText());

        if(pref.getUserName().equals(userName) && pref.getPassword().equals(password)){
            closeStage();
            loadMain();
        }else{
//            Label.setText("Invalid Credentials");
//            Label.setStyle("-fx-background-color: #d32f2f");
            UName.getStyleClass().add("wrong-credentials");
            pass.getStyleClass().add("wrong-credentials");
        }

    }

    public void loadMain(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/LibraryModule/UI/Main/Main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Main");
            stage.setScene(new Scene(parent));
            stage.show();
            Utilities.setImage(stage,"/LibraryModule/UI/Main/PNG/icons8-read-50.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeStage(){
        ((Stage)UName.getScene().getWindow()).close();
    }

    public void handleCancel(ActionEvent event) {
        System.exit(0);
    }

    public void handleClose(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void handleMaximize(MouseEvent mouseEvent) {
        Stage s = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        s.setFullScreen(true);
    }

    public void handleMinimize(MouseEvent mouseEvent) {
        Stage s = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    public void setStageDraggable(MouseEvent mouseEvent) {

        Stage CurrentStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();

        Label.setOnMousePressed( event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        Label.setOnMouseDragged(event ->{
            CurrentStage.setX(event.getScreenX() - xOffset);
            CurrentStage.setY(event.getScreenY() - yOffset);
            CurrentStage.setOpacity(0.9f);
        });

        Label.setOnDragDone(event -> {
            CurrentStage.setOpacity(1.0f);
        });

        Label.setOnMouseReleased(event -> {
            CurrentStage.setOpacity(1.0f);
        });
    }
}
