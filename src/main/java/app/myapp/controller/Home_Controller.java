package app.myapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Home_Controller {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private AnchorPane container;

    @FXML
    void initialize() throws IOException {

    }

}