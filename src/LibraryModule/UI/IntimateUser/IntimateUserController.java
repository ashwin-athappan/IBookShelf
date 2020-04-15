package LibraryModule.UI.IntimateUser;

import LibraryModule.UI.Database.DatabaseHandler;
import LibraryModule.UI.Mail.MailUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class IntimateUserController implements Initializable {
    public AnchorPane IntimateUserPane;
    private DatabaseHandler DBH;
    public JFXTextField BookID;
    public JFXButton CheckButton;
    public JFXTextArea Message;
    public JFXButton Send;
    private String userID;
    private String userEmail;
    private String myMessage;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Message.setDisable(true);
        Send.setDisable(true);
        DBH = DatabaseHandler.getInstance();
    }

    public void doCheck(ActionEvent actionEvent) {
        String id = BookID.getText();
        ResultSet rs = DBH.execQuery("SELECT * FROM BOOK WHERE id='"+id+"'");
        try {
            if(rs.next()){
                String BName = rs.getString("title");
                String BAuthor = rs.getString("author");
                Boolean Avail = rs.getBoolean("isAvailable");

                if(Avail){
                    Alert alert =new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("ERROR");
                    alert.setContentText("Book "+id+" is not issued to anyone");
                    alert.showAndWait();
                }else{
                    Message.setDisable(false);
                    Send.setDisable(false);
                    rs = DBH.execQuery("SELECT * FROM issue WHERE BookId='"+id+"'");
                    if(rs.next()){
                        userID = rs.getString("MemberId");
                    }

                    rs = DBH.execQuery("SELECT * FROM memberlist WHERE id='"+userID+"'");
                    if(rs.next()){
                        userEmail = rs.getString("email");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Book "+id+" does not exist");
            alert.showAndWait();
        }

    }

    public void send(ActionEvent actionEvent) {
        myMessage = Message.getText();
        MailUtil.sendMail(userEmail,myMessage);
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("ERROR");
        alert.setContentText("Mail Sent to "+userEmail+" Successfully");
        alert.showAndWait();

        Stage stage = (Stage) IntimateUserPane.getScene().getWindow();
        stage.close();
    }


}
