package diary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DiaryApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Diary App");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("DiaryUi.fxml"))));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
