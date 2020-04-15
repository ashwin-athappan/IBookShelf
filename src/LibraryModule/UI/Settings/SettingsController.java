package LibraryModule.UI.Settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    public JFXTextField NoOfDays;
    public JFXTextField Fine;
    public JFXTextField UName;
    public JFXPasswordField Password;
    public JFXButton Save;
    public JFXButton Cancel;
    public AnchorPane SettingsPane;
    private Stage SettingsStage;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDefaultValues();
    }

    private void initDefaultValues() {
        Preferences pref = Preferences.getPreference();
        NoOfDays.setText(String.valueOf(pref.getnDays()));
        Fine.setText(String.valueOf(pref.getFine()));
        UName.setText(String.valueOf(pref.getUserName()));
        Password.setText(String.valueOf(pref.getPassword()));
    }

    public void HandleSave(ActionEvent event) {
        int N = Integer.parseInt(NoOfDays.getText());
        float F = Float.parseFloat(Fine.getText());
        String U = UName.getText();
        String P = Password.getText();
        Preferences pref = Preferences.getPreference();
        pref.setnDays(N);
        pref.setFine(F);
        pref.setUserName(U);
        pref.setPassword(P);
        Preferences.rewriteConfig(pref);
        initDefaultValues();
        SettingsStage.close();
    }

    public void HandelCancel(ActionEvent event) {
        SettingsStage.close();
    }

    public static int getDays(){
        Preferences pref = Preferences.getPreference();;
        return pref.getnDays();
    }

    public static float getFine(){
        Preferences pref = Preferences.getPreference();;
        return pref.getFine();
    }

    public void setWindow(Stage stage) {
        SettingsStage = stage;
    }
}
