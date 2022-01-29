package app.myapp.controller;

import app.myapp.Main;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Splash_Controller {
    @FXML
    private Text txt_Title;

    @FXML
    void initialize() {
        FadeTransition transition = new FadeTransition(Duration.millis(1000),txt_Title);
        transition.setFromValue(1.0);
        transition.setToValue(1.0);
        transition.play();

        transition.setOnFinished(event -> {
            Stage primaryStage = (Stage) txt_Title.getScene().getWindow();
            primaryStage.hide();

            Stage loginStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/userlogin-view.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            loginStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("objects\\logo_ph.png"))));
            loginStage.setScene(scene);
            loginStage.setTitle("NightSpot");
            loginStage.setResizable(true);
            loginStage.resizableProperty().setValue(Boolean.FALSE);
            loginStage.show();
        });
    }
}
