package app.myapp.model;

import app.myapp.Main;
import app.myapp.database.Driver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class AppWindow extends Application {
    public static void main(String[] args) {
        Driver db_con = new Driver();
        db_con.startConnection();
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/splash-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.centerOnScreen();
        primaryStage.show();

    }

}