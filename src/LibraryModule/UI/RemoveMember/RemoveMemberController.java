package LibraryModule.UI.RemoveMember;

import LibraryModule.UI.Database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RemoveMemberController implements Initializable {
    public JFXTextArea memberID;
    public JFXButton Remove;
    public Label rmLabel;

    private DatabaseHandler DBH = DatabaseHandler.getInstance();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void RemoveMember(ActionEvent event) {
        String mID = memberID.getText();
        Alert alert;
        if(DBH.execAction("DELETE FROM memberlist where id='" + mID + "';")) {
           alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setContentText("Member Removed");
           alert.showAndWait();
        }else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Remove Operation failed");
            alert.showAndWait();
        }
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        String mID = memberID.getText();
        ResultSet rs = DBH.execQuery("SELECT * FROM memberlist where id='"+mID+"';");
        try {
            if(rs.next()){
                rmLabel.setText("Member Found");
                rmLabel.setStyle("-fx-background-color: #0c9463");
            }else{
                rmLabel.setText("Member Not Found");
                rmLabel.setStyle("-fx-background-color: #d32f2f");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
