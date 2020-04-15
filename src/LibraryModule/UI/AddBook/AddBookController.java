package LibraryModule.UI.AddBook;

import LibraryModule.UI.Database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {

    public AnchorPane AddBookPane;
    public JFXTextField BookTitle;
    public JFXTextField BookID;
    public JFXTextField BookAuthor;
    public JFXTextField Publisher;
    public JFXButton SaveButton;
    public JFXButton CancelButton;
    public Button ChooseImage;
    private boolean addImageCalled = false;
    boolean fileExists = false;
    private String imgPath;

    DatabaseHandler DBH;

    Stage AddBookStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBH = DatabaseHandler.getInstance();
    }

    @FXML
    private void addBook(ActionEvent event){
        String title = BookTitle.getText();
        String id = BookID.getText();
        String author = BookAuthor.getText();
        String publisher = Publisher.getText();
        String status = "true";
        String imPath = "";
        if(title.isEmpty() || id.isEmpty() || author.isEmpty() || publisher.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Please Enter All The Fields");
            alert.showAndWait();
        }else {

            String query ="insert into book values('" + id + "','" + title + "','" + author + "','" + publisher + "'," + status + ");";


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

            String query1 = "insert into img values('"+id+"','"+imPath+"');";
            if(DBH.execAction(query) && DBH.execAction(query1)){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("Success");
                alert.setContentText("Successfully Added");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Failed");
                alert.setContentText("Failed to Add");
                alert.showAndWait();
            }
            AddBookStage.close();
        }
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

    @FXML
    private void cancel(ActionEvent event){
        Stage stage = (Stage) AddBookPane.getScene().getWindow();
        stage.close();
    }

    public void addImage(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File selected = fc.showOpenDialog(null);
        String path = "";
            if (selected != null) {
                path=selected.getAbsolutePath();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
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

    public String AddImageIsNotCalled(){
        FileChooser fc = new FileChooser();
        File selected = fc.showOpenDialog(null);
        String path = "";
        String fileName = "";
        if (selected != null) {
            path = selected.getAbsolutePath();
            fileName = selected.getName();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("Invalid file");
            alert.setTitle("Error");
            alert.showAndWait();
        }
        return copyFile(path,fileName).replace("\\","/");
    }

    public void setWindow(Stage stage) {
        AddBookStage = stage;
    }
}
