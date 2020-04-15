package LibraryModule.UI.AddMember;

import LibraryModule.UI.Database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class AddMemberController implements Initializable {
    public AnchorPane AddMemberPane;
    public JFXTextField Name;
    public JFXTextField MemberID;
    public JFXTextField MobileNumber;
    public JFXTextField Email;
    public JFXButton AddButton;
    public JFXButton CancelButton;
    DatabaseHandler DBH;
    private Stage AddMemberStage;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBH = DatabaseHandler.getInstance();
    }

    public void setWindow(Stage stage){
        AddMemberStage = stage;
    }

    public void addMember(ActionEvent event) {
        String name = Name.getText();
        String id = MemberID.getText();
        String mobileNumber = MobileNumber.getText();
        String email = Email.getText();

        if(name.isEmpty() || id.isEmpty() || mobileNumber.isEmpty() || email.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Please Enter All The Fields");
            alert.showAndWait();
        }else {

            String query ="insert into memberlist values('" + id + "','" + name + "','" + mobileNumber + "','" + email + "');";
            if(DBH.execAction(query)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Success");
                alert.setContentText("Successfully Added");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Failed");
                alert.setContentText("Failed to Add");
                alert.showAndWait();
            }

        }
        AddMemberStage.close();
    }

    public void cancel(ActionEvent event) {
        Stage stage = (Stage) AddMemberPane.getScene().getWindow();
        stage.close();
    }
}
