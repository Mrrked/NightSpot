package app.myapp.controller.component;

import app.myapp.Main;
import app.myapp.controller.component.element.NotifLabel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SignUpForm extends VBox {
    @FXML
    public Button btnSignUp;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public PasswordField txtConfirmPassword;
    @FXML
    public TextField txtUsername;
    @FXML
    public HBox paneNotif;

    public SignUpForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/SignUpForm.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    public void showNotificationPane(String message) throws IOException {
        paneNotif.getChildren().clear();

        NotifLabel myNotifPane = new NotifLabel();
        myNotifPane.setText(message);

        paneNotif.getChildren().add(myNotifPane);
    }

    public void hideNotificationPane(){
        paneNotif.getChildren().clear();
    }
}
