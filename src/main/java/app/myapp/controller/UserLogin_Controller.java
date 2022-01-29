package app.myapp.controller;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class UserLogin_Controller {
    @FXML
    AnchorPane dynamic_pane;
    @FXML
    Text btnSwitch;
    @FXML
    Text lbl_Text;
    @FXML
    Text lbl_Title;

    FXMLLoader fxmlLoader;

    @FXML
    void initialize() throws IOException {
        fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/component/SignInForm.fxml"));
        Node node = fxmlLoader.load();
        dynamic_pane.getChildren().add(node);
    }
    @FXML
    void switchForm(MouseEvent event) throws IOException {
        String txt = lbl_Text.getText();
        if(txt.equals("Not yet a member?")){
            fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/component/SignUpForm.fxml"));
            lbl_Text.setText("Already a member?");
            btnSwitch.setText("Sign In");
            lbl_Title.setText("Create an account");
        }else if(txt.equals("Already a member?")){
            fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/component/SignInForm.fxml"));
            lbl_Text.setText("Not yet a member?");
            btnSwitch.setText("Create an account");
            lbl_Title.setText("Sign In to NightSpot");
        }
        Node node = fxmlLoader.load();
        dynamic_pane.getChildren().clear();
        dynamic_pane.getChildren().add(node);
    }
}
