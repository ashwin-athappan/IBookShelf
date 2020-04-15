package LibraryModule.UI.DisplayBook;

import LibraryModule.UI.Database.DatabaseHandler;
import LibraryModule.UI.EditBook.EditBookController;
import LibraryModule.UI.Utilities.Utilities;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookListController implements Initializable {

    public ContextMenu Context;
    public MenuItem ContextDelete;
    public MenuItem ContextEdit;
    public MenuItem Refresh;
    ObservableList<Book> observableList = FXCollections.observableArrayList();

    @FXML
    private TableView<Book> BookListTable;
    @FXML
    private TableColumn<Book, String> TitleColumn;
    @FXML
    private TableColumn<Book, String> IDColumn;
    @FXML
    private TableColumn<Book, String> AuthorColumn;
    @FXML
    private TableColumn<Book, String> PublisherColumn;
    @FXML
    private TableColumn<Book, Boolean> AvailabilityColumn;

    Alert alert;

    @Override
    public void initialize (URL location, ResourceBundle resources){
        associateColumns();
        loadData();
    }

    private void associateColumns() {
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        PublisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        AvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
    }

    public void loadData(){
        observableList.clear();
        DatabaseHandler DBH = DatabaseHandler.getInstance();
        String q = "select * from book";
        ResultSet rs = DBH.execQuery(q);

            while (true) {
                try {
                    if (!rs.next())
                        break;
                    else{
                        String title = rs.getString("title");
                        String id = rs.getString("id");
                        String author = rs.getString("author");
                        String publisher = rs.getString("publisher");
                        Boolean availability = rs.getBoolean("isAvailable");
                        observableList.add(new Book(title,id,author,publisher,availability));


                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        BookListTable.setItems(observableList);
    }

    public void DeleteBook(ActionEvent event) {
        Book SelectedForDeletion = BookListTable.getSelectionModel().getSelectedItem();
        if(SelectedForDeletion == null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Book Selected");
            alert.setContentText("Please Select a Book");
            alert.showAndWait();
            return;
        }

        if(DatabaseHandler.getInstance().isBookIssued(SelectedForDeletion.getId())){
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setContentText("This Book has been issued");
            alert.showAndWait();
            return;
        }
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure you want to delete the book "+SelectedForDeletion.getTitle());
        Optional<ButtonType> optionalButton = alert.showAndWait();
        if(optionalButton.get() == ButtonType.OK){
            String id = SelectedForDeletion.getId();
            boolean r = DatabaseHandler.getInstance().DeleteBookOperation(id);
            if(r){
                observableList.remove(SelectedForDeletion);
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Deletion Success");
            }else{
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Deletion Failed");
            }
        }else{
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Canceled");
            alert.setContentText("Deletion Canceled");
        }

    }

    public void EditBook(ActionEvent event) {
        Book SelectedForEditing = BookListTable.getSelectionModel().getSelectedItem();
        if(SelectedForEditing == null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Book Selected");
            alert.setContentText("Please Select a Book");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LibraryModule/UI/EditBook/EditBook.fxml"));
            Parent parent = loader.load();
            EditBookController controller = loader.getController();
            controller.inflateUI(SelectedForEditing);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Book");
            stage.setScene(new Scene(parent));
            Utilities.setImage(stage,"/LibraryModule/UI/Main/PNG/icons8-add-file-100-8.png");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRefresh(ActionEvent event) {
        loadData();
    }

    public static class Book{
        private final SimpleStringProperty id;
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleBooleanProperty isAvailable;

        public Book( String title, String id, String author, String publisher, Boolean availability) {
            this.id = new SimpleStringProperty(id);
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.isAvailable = new SimpleBooleanProperty(availability);
        }

        public String getId() {
            return id.get();
        }

        public String getTitle() {
            return title.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public boolean getIsAvailable() {
            return isAvailable.get();
        }

    }

}
