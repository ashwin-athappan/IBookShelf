package LibraryModule.UI.DisplayMember;

import LibraryModule.UI.Database.DatabaseHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DisplayMemberController implements Initializable {

    public TableView<Member> MemberTable;
    public TableColumn<Member, String> MemberID;
    public TableColumn<Member, String> MemberName;
    public TableColumn<Member, String> MemberMobileNumber;
    public TableColumn<Member, String> MemberEmail;
    public MenuItem Delete;

    ObservableList<Member> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        associateMember();
        loadData();
    }

    public void associateMember(){
        MemberID.setCellValueFactory(new PropertyValueFactory<>("id"));
        MemberName.setCellValueFactory(new PropertyValueFactory<>("name"));
        MemberMobileNumber.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        MemberEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    public void loadData(){
        DatabaseHandler DBH = DatabaseHandler.getInstance();
        String q = "select * from memberlist";
        ResultSet rs = DBH.execQuery(q);

        while(true){
            try {
                if (!rs.next())
                    break;
                else{
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String mobile = rs.getString("mobile");
                    String email = rs.getString("email");
                    observableList.add(new Member(id,name,mobile,email));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MemberTable.setItems(observableList);
    }

    public void DeleteMemberContext(ActionEvent event) {
        Member SelectedItem = MemberTable.getSelectionModel().getSelectedItem();
        Alert alert;
        if(SelectedItem == null){
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("Select An entry");
            alert.showAndWait();
            return;
        }
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure you want to delete the book "+SelectedItem.getName());
        Optional<ButtonType> Button = alert.showAndWait();
        if(Button.get() == ButtonType.OK){
            String Mid = SelectedItem.getId();
            boolean res = DatabaseHandler.getInstance().DeleteMemberOperation(Mid);
            if(res){
                observableList.remove(SelectedItem);
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Deletion Success");
            }else{
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Deletion Failed");
            }
        }else{
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Canceled");
            alert.setContentText("Deletion Canceled");
        }
    }

    public static class Member{
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;

        public Member(String id, String name, String mobile, String email) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.mobile = new SimpleStringProperty(mobile);
            this.email = new SimpleStringProperty(email);
        }

        public String getId() {
            return id.get();
        }


        public String getName() {
            return name.get();
        }


        public String getMobile() {
            return mobile.get();
        }

        public String getEmail() {
            return email.get();
        }

    }
}
