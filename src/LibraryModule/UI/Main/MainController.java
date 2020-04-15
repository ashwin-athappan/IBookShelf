package LibraryModule.UI.Main;

import LibraryModule.UI.AddBook.AddBookController;
import LibraryModule.UI.AddMember.AddMemberController;
import LibraryModule.UI.Database.DatabaseHandler;
import LibraryModule.UI.Settings.SettingsController;
import LibraryModule.UI.Utilities.Utilities;
import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public StackPane MainPane;
    public BorderPane MainBorder;
    public javafx.scene.control.MenuBar MenuBar;
    public Button AddMember;
    public Button AddBook;
    public Button ViewMember;
    public Button ViewBook;
    public Button Settings;
    public TextField NewBookID;
    public TextField MemberID;
    public Button Issue;
    public TextField OldBookID;
    public Button Renew;
    public Button Submit;
    public Text BookName;
    public Text BookAuthor;
    public Text MemberNameField;
    public Text MemberMobile;
    public Text BookStatus;
    public HBox MemberBox;
    public HBox BookBox;
    public ImageView BookImage;
    public ImageView StatusIndicator;
    public ListView<String> DisplayListDetails;
    public boolean isReadyForSubmission = false;
    public Menu File;
    public Menu Add;
    public Menu Remove;
    public Menu About;
    public MenuItem MenuAbout;
    public MenuItem close;
    public MenuItem MenuAddMember;
    public MenuItem MenuAddBook;
    public MenuItem MenuRemoveMember;
    public Button IntimateUser;

    DatabaseHandler DBH;

    int numOfDays;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXDepthManager.setDepth(BookBox,2);
        JFXDepthManager.setDepth(MemberBox,2);
//        AddMember.setDisable(true);
//        AddBook.setDisable(true);
//        ViewBook.setDisable(true);
//        ViewMember.setDisable(true);
//        Issue.setDisable(true);
//        Renew.setDisable(true);
//        Submit.setDisable(true);
        new Thread(() -> DBH = DatabaseHandler.getInstance()).start();
    }

    public void loadAddMember(ActionEvent event) {
        String location = "/LibraryModule/UI/AddMember/AddMember.fxml";
        String Icon = "/LibraryModule/UI/Main/PNG/icons8-collaborator-male-100.png";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            AddMemberController controller = loader.getController();
            controller.setWindow(stage);
            stage.setTitle("Add New Member");
            stage.setScene(new Scene(parent));
            Utilities.setImage(stage,Icon);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAddBook(ActionEvent event) {
        String location = "/LibraryModule/UI/AddBook/AddBook.fxml";
        String Icon = "/LibraryModule/UI/Main/PNG/icons8-add-file-100-8.png";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            AddBookController controller = loader.getController();
            controller.setWindow(stage);
            stage.setTitle("Add New Book");
            stage.setScene(new Scene(parent));
            Utilities.setImage(stage,Icon);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadViewMember(ActionEvent event) {
        String location = "/LibraryModule/UI/DisplayMember/DisplayMember.fxml";
        String Icon = "/LibraryModule/UI/Main/PNG/icons8-manager-100.png";
        loadWindow(location,"Member List",Icon);
    }

    public void loadViewBook(ActionEvent event) {
        String location = "/LibraryModule/UI/DisplayBook/BookList.fxml";
        String Icon = "/LibraryModule/UI/Main/PNG/icons8-view-100-6.png";
        loadWindow(location,"Book List",Icon);
    }

    public void loadSettings(ActionEvent event) {
        String location = "/LibraryModule/UI/Settings/Settings.fxml";
        String Icon = "/LibraryModule/UI/Main/PNG/settings [#1491].png";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            SettingsController controller = loader.getController();
            controller.setWindow(stage);
            stage.setTitle("Add New Book");
            stage.setScene(new Scene(parent));
            Utilities.setImage(stage,Icon);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void IssueOperation(ActionEvent event) {
        String mID = MemberID.getText();
        String bID = NewBookID.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue");
        alert.setHeaderText(null);
        alert.setContentText("Are you Sure you want to issue the book "+BookName.getText()+" to "+MemberNameField.getText());
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK){
            String query1 = "INSERT INTO issue(BookId,MemberId) values('"+bID+"','"+mID+"')";
            String query2 = "UPDATE BOOK SET isAvailable = false WHERE id='"+bID+"';";
            if(DBH.execAction(query1) && DBH.execAction(query2)){
                Alert alert1 = new Alert(AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setContentText("Book "+BookName.getText()+" Issued");
                alert1.showAndWait();
            }else{
                Alert alert1 = new Alert(AlertType.INFORMATION);
                alert1.setTitle("Failed");
                alert1.setContentText("Book "+BookName.getText()+" Issue Failed");
                alert1.showAndWait();
            }
        }else{
            Alert alert1 = new Alert(AlertType.INFORMATION);
            alert1.setTitle("Canceled");
            alert1.setHeaderText(null);
            alert1.setContentText("Issue Operation Canceled");
        }
    }

    public void setStatusIndicator(String s){
        String bID = NewBookID.getText();
        String path = "";
            if(!s.equals("Does not exist")){
                if(s.equals("Available")){
                    path = "/LibraryModule/UI/Main/Status Images/green.png";
                }else if(s.equals("Not Available")){
                    //file:///C:/Users/Ashwin/IdeaProjects/Library/src/LibraryModule/UI/Main
                    path = "/LibraryModule/UI/Main/Status Images/red.png";
                }
            }else{
                path = "file:///I:/JAVA PROJECTS REQUIREMENTS/Icon/BookImages/222831.png";
            }
        Image img = new Image(path);
        StatusIndicator.setImage(img);
    }

    public void setImagePath(){
        String bID = NewBookID.getText();
        ResultSet rs = DBH.execQuery("SELECT * FROM img WHERE id='"+bID+"';");
        String path="";
        try {
            if(rs.next()){
                path = rs.getString("ImagePath");
            }else{
                path = "file:///I:/JAVA PROJECTS REQUIREMENTS/Icon/BookImages/222831.png";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Image img = new Image(path);
        BookImage.setImage(img);
    }

    public void Renew(ActionEvent event) {
        if(!isReadyForSubmission){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Failed");
            alert.setContentText("Please Select a book for renewal");
            alert.showAndWait();
            return;
        }
        int c = 0;
        String qCheck = "select * from issue  where BookId='"+OldBookID.getText()+"';";
        ResultSet rs = DBH.execQuery(qCheck);
        try {
            if(rs.next()){
                Date currentDate = new Date();
                Date issueDate = rs.getDate("issueTime");
                int days = SettingsController.getDays();
                float fine = SettingsController.getFine();
                long diff = Math.abs((currentDate.getTime() - issueDate.getTime())/86400000);
                if(diff >= days){
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setContentText("Pay the fine of "+fine * diff);
                    Optional<ButtonType> response = alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to renew");
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK) {
            String q1 = "UPDATE issue SET issueTime=CURRENT_TIMESTAMP where BookId='"+OldBookID.getText()+"';";
            if(DBH.execAction(q1)){
                rs = DBH.execQuery("SELECT * FROM issue where BookId='"+OldBookID.getText()+"';");
                try {
                    if(rs.next()){
                        c = rs.getInt("renewCount");
                        c+=1;
                        String q2 = "UPDATE issue SET renewCount="+c+" where BookID='"+OldBookID.getText()+"';";
                        DBH.execAction(q2);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Renewal Success");
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setContentText("Renewal Failed");
                alert.showAndWait();
            }
            LoadIssuedBookInfo(event);
        }else{
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Failed");
            alert.setContentText("Renewal Canceled");
            alert.showAndWait();
        }
    }

    public void Submit(ActionEvent event) {
        if(!isReadyForSubmission){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Failed");
            alert.setContentText("Please Select a book for submission");
            alert.showAndWait();
            return;
        }

        String qCheck = "select * from issue  where BookId='"+OldBookID.getText()+"';";
        ResultSet rs = DBH.execQuery(qCheck);
        try {
            if(rs.next()){
                Date currentDate = new Date();
                Date issueDate = rs.getDate("issueTime");
                int days = SettingsController.getDays();
                float fine = SettingsController.getFine();
                long diff = Math.abs((currentDate.getTime() - issueDate.getTime())/86400000);
                if(diff >= days){
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setContentText("Pay the fine of "+fine * diff);
                    Optional<ButtonType> response = alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to submit");
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK) {
            String bookID = OldBookID.getText();
            String q1 = "DELETE FROM issue where BookId='" + bookID + "';";
            String q2 = "UPDATE book SET isAvailable = true where id='" + bookID + "';";

            if (DBH.execAction(q1) && DBH.execAction(q2)) {
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Book has been submitted");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Failed");
                alert.setContentText("Book couldn't be submitted");
                alert.showAndWait();
            }
        }else{
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Failed");
            alert.setContentText("Submission Canceled");
            alert.showAndWait();
        }
    }

    public void loadWindow(String location, String title, String IconLoc){
        try {
            Parent parent =FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            Utilities.setImage(stage,IconLoc);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMemberInfo(ActionEvent event) {
        String MID = MemberID.getText();
        ResultSet rs = DBH.execQuery("SELECT * FROM MEMBERLIST WHERE id='"+MID+"'");
        boolean flag = false;
        try {
            if(rs.next()){
               String MName = rs.getString("name");
               String MMobile = rs.getString("mobile");
               MemberNameField.setText(MName);
               MemberMobile.setText(MMobile);
               flag = true;
            }

            if(!flag){
                MemberNameField.setText("Please Register");
                MemberMobile.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadBookInfo(ActionEvent event) {
        String BookID = NewBookID.getText();
        ResultSet rs = DBH.execQuery("SELECT * FROM BOOK WHERE id='"+BookID+"'");
        boolean flag = false;
        try {
            if(rs.next()){
                String BName = rs.getString("title");
                String BAuthor = rs.getString("author");
                Boolean Avail = rs.getBoolean("isAvailable");
                setImagePath();
                BookName.setText(BName);
                BookAuthor.setText(BAuthor);
                if(Avail){
                    BookStatus.setText("Available");
                    setStatusIndicator("Available");
                }else{
                    BookStatus.setText("Not Available");
                    setStatusIndicator("Not Available");
                }

                flag = true;
            }

            if(!flag){
                BookName.setText("No Such Book Available");
                BookAuthor.setText("");
                BookStatus.setText("");
                setImagePath();
                setStatusIndicator("Does not exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void LoadIssuedBookInfo(ActionEvent event) {
        ObservableList<String> issueData = FXCollections.observableArrayList();
        String bookId = OldBookID.getText();
        ResultSet rs = DBH.execQuery("SELECT * from issue where BookId='"+bookId+"';");
        try {
            if(rs.next()){
                String mBookID = rs.getString("BookId");
                String mMemberID = rs.getString("MemberId");
                int mRenewCount = rs.getInt("renewCount");
                Timestamp IssueTime = rs.getTimestamp("issueTime");

                issueData.add("Issue Date and Time : "+IssueTime.toGMTString());
                issueData.add("Renew Count : "+mRenewCount);


                String q1 = "SELECT * from book where id='"+mBookID+"';";
                issueData.add("Book Information:");
                ResultSet rs1 = DBH.execQuery(q1);
                if(rs1.next()){
                    issueData.add("    Book Name : "+rs1.getString("title"));
                    issueData.add("    Book ID : "+rs1.getString("id"));
                    issueData.add("    Book Author : "+rs1.getString("author"));
                    issueData.add("    Book Publisher : "+rs1.getString("publisher"));
                }

                String q2 = "SELECT * from memberlist where id='"+mMemberID+"';";
                rs1 = DBH.execQuery(q2);
                issueData.add("Member Information:");
                if(rs1.next()){
                    issueData.add("    Member Name : "+rs1.getString("name"));
                    issueData.add("    Member ID : "+rs1.getString("id"));
                    issueData.add("    Member Mobile : "+rs1.getString("mobile"));
                    issueData.add("    Member Email : "+rs1.getString("email"));
                }

                isReadyForSubmission = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DisplayListDetails.getItems().setAll(issueData);
    }

    public void handleClose(ActionEvent event) {
        System.exit(0);
    }

    public void handleAddMember(ActionEvent event) {
        loadAddMember(event);
    }

    public void handleAddBook(ActionEvent event) {
        loadAddBook(event);
    }

    public void handleRemoveMember(ActionEvent event) {
        String location = "/LibraryModule/UI/RemoveMember/RemoveMember.fxml";
        loadWindow(location,"Remove Member","/LibraryModule/UI/Main/PNG/icons8-anti-trump-100.png");
    }

    public void handleAbout(ActionEvent event) {
    }

    public void IntimateUser(ActionEvent actionEvent) {
        String location = "/LibraryModule/UI/IntimateUser/IntimateUser.fxml";
        loadWindow(location,"Intimate User","/LibraryModule/UI/Main/PNG/icons8_notification_32px.png");
    }
}
