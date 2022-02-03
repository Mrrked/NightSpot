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
        Stage loginStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/userLoginView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("objects\\logo_ph.png"))));
        loginStage.setScene(scene);
        loginStage.setTitle("NightSpot");
        loginStage.setResizable(false);
        loginStage.resizableProperty().setValue(Boolean.FALSE);

        FadeTransition transition = new FadeTransition(Duration.seconds(2),txt_Title);
        transition.setFromValue(1.0);
        transition.setToValue(1.0);
        transition.play();

        transition.setOnFinished(event -> {
            Stage splashStage = (Stage) txt_Title.getScene().getWindow();
            splashStage.close();
            loginStage.show();
        });
    }
}
