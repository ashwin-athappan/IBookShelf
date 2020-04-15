package LibraryModule.UI.EditBook;

import LibraryModule.UI.Database.DatabaseHandler;
import LibraryModule.UI.DisplayBook.BookListController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


public class EditBookController implements Initializable {
    public JFXTextField newBookTitle;
    public JFXTextField BookID;
    public JFXTextField newBookAuthor;
    public JFXTextField newPublisher;
    public Button ChooseImage;
    public JFXButton UpdateButton;
    public JFXButton CancelButton;
    public AnchorPane EditBookPane;
    String imgPath;
    boolean addImageCalled = false;
    boolean fileExists = false;
    Alert alert;
    BookListController.Book oldBook;
    String oldBookID;

    public void addNewImage(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File selected = fc.showOpenDialog(null);
        String path = "";
        if (selected != null) {
            path=selected.getAbsolutePath();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid file");
            alert.setTitle("Error");
            alert.showAndWait();
        }
        if(path.equals("")){
            return;
        }
        addImageCalled = true;
        imgPath = copyFile(path,selected.getName()).replace("\\","/");
    }

    public String copyFile(String imPath, String fileName){
        File sourceFile = new File(imPath);

        File destinationFile = new File("C:\\Users\\ashwi\\IdeaProjects\\Library\\src\\LibraryModule\\UI\\Main\\BookImages\\"+fileName);
        String newPath = "C:\\Users\\ashwi\\IdeaProjects\\Library\\src\\LibraryModule\\UI\\Main\\BookImages\\"+fileName;
        if(destinationFile.exists()){
            boolean deletion = destinationFile.delete();
            if(deletion){
                try {
                    Files.copy(sourceFile.toPath(),destinationFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fileExists = true;
            imgPath = "file:///C:\\Users\\ashwi\\IdeaProjects\\Library\\src\\LibraryModule\\UI\\Main\\BookImages\\"+fileName;
            newPath = imgPath.replace("\\","/");
        }else{
            try {
                Files.copy(sourceFile.toPath(),destinationFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newPath;
    }

    void getImgLoc(String bID){
        ResultSet rs = DatabaseHandler.getInstance().execQuery("select * from img where id='"+bID+"';");
        while(true){
            try {
                if (!rs.next())
                    break;
                else{
                    imgPath = rs.getString(2);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void AddImageIsNotCalled(){
        getImgLoc(oldBookID);
    }

    public void UpdateBook(ActionEvent event) {
        alert = new Alert(AlertType.CONFIRMATION);
        BookListController.Book book = new BookListController.Book(newBookTitle.getText(),BookID.getText(),newBookAuthor.getText(),newPublisher.getText(),true);
        String imPath = "";
        alert.setTitle("Are you sure you want to update");
        alert.setContentText(oldBook.getTitle()+" to "+book.getTitle()+ "\n"+
                             oldBook.getAuthor()+" to "+book.getAuthor()+"\n"+
                             oldBook.getPublisher()+" to "+book.getPublisher());
        Optional<ButtonType> b = alert.showAndWait();

        if(b.get() == ButtonType.OK){
            if(fileExists && addImageCalled){
                imPath = imgPath;
            }else if(fileExists){
                AddImageIsNotCalled();
                imPath = imgPath;
            }else{
                if(addImageCalled){
                    imPath = "file:///"+imgPath;
                }else{
                    AddImageIsNotCalled();
                    imPath = "file:///"+imgPath;
                }
            }


            if(DatabaseHandler.getInstance().updateBook(book,imPath)){
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Updated Successfully");
                alert.showAndWait();
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Updated Failed");
                alert.showAndWait();
            }

        }else{
            alert.setTitle("Update operation canceled");
            alert.setContentText("You cancelled the update");
            alert.showAndWait();
        }
    }


    public void cancel(ActionEvent event) {
        Stage stage = (Stage) EditBookPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(BookListController.Book book){
        newBookTitle.setText(book.getTitle());
        BookID.setText(book.getId());
        oldBookID = book.getId();
        newBookAuthor.setText(book.getAuthor());
        newPublisher.setText(book.getPublisher());
        BookID.setDisable(true);
        oldBook = new BookListController.Book(newBookTitle.getText(),BookID.getText(),newBookAuthor.getText(),newPublisher.getText(),true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
