package LibraryModule.UI.Main;

import LibraryModule.UI.Utilities.Utilities;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        final String MainImage = "/LibraryModule/UI/Main/PNG/icons8-read-50-7.png";

        Parent root = FXMLLoader.load(getClass().getResource("/LibraryModule/UI/Login/Login.fxml"));
        primaryStage.setTitle("Library");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Utilities.setImage(primaryStage,MainImage);
        primaryStage.show();
    }
}
