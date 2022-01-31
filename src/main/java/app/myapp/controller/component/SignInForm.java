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

public class SignInForm extends VBox {
    @FXML
    public Button btnSignIn;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public TextField txtUsername;
    @FXML
    public HBox paneNotif;

    public SignInForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/SignInForm.fxml"));
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
