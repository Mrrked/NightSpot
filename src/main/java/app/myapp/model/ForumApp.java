package app.myapp.model;

import app.myapp.Main;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class ForumApp extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage splashStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/splashView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        splashStage.setScene(scene);
        splashStage.initStyle(StageStyle.TRANSPARENT);
        splashStage.centerOnScreen();
        splashStage.show();
    }

}