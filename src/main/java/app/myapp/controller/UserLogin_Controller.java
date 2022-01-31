package app.myapp.controller;

import app.myapp.Main;
import app.myapp.controller.component.SignInForm;
import app.myapp.controller.component.SignUpForm;
import app.myapp.model.user.method.Method;
import app.myapp.model.user.method.UserAuth;
import app.myapp.model.user.method.UserCreate;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

public class UserLogin_Controller {
    @FXML
    AnchorPane dynamic_pane;
    @FXML
    Text btnSwitch;
    @FXML
    Text lbl_Text;
    @FXML
    Text lbl_Title;

    private final SignInForm signInForm;
    private final SignUpForm signUpForm;

    public UserLogin_Controller() throws IOException {
        signInForm = new SignInForm();
        signUpForm = new SignUpForm();
    }

    @FXML
    void initialize() {
        dynamic_pane.getChildren().add(signInForm);

        signInForm.btnSignIn.setOnAction(e -> {
            System.out.println("SignIn Button is Clicked");
            try {
                signIn(e);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        });

        signUpForm.btnSignUp.setOnAction(e -> {
            System.out.println("SignUp Button is Clicked");
            try {
                signUp(e);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML
    void switchForm() {
        Node node = null;
        String txt = lbl_Text.getText();
        if(txt.equals("Not yet a member?")){
            signInForm.txtUsername.clear();
            signInForm.hideNotificationPane();
            node = signUpForm;
            lbl_Text.setText("Already a member?");
            btnSwitch.setText("Sign In");
            lbl_Title.setText("Create an account");
        }else if(txt.equals("Already a member?")){
            signUpForm.txtUsername.clear();
            signUpForm.hideNotificationPane();
            node = signInForm;
            lbl_Text.setText("Not yet a member?");
            btnSwitch.setText("Create an account");
            lbl_Title.setText("Sign In to NightSpot");
        }

        dynamic_pane.getChildren().clear();
        dynamic_pane.getChildren().add(node);
    }

    void signIn(ActionEvent event) throws IOException, SQLException {
        String username = signInForm.txtUsername.getText();
        String password = signInForm.txtPassword.getText();

        signInForm.txtPassword.clear();

        if(username.isEmpty() || password.isEmpty()){
            signInForm.showNotificationPane("Please fill up all fields!");
        }else{
            Method userAuth = new UserAuth(username, password);
            if(userAuth.isExist()){
                signInForm.showNotificationPane("Success");
                onSuccess();
                switchToMainApp();
            }else{
                signInForm.showNotificationPane("Incorrect username or password!");
            }
        }

    }

    void signUp(ActionEvent event) throws IOException, SQLException {

        String username = signUpForm.txtUsername.getText();
        String password1 = signUpForm.txtPassword.getText();
        String password2 = signUpForm.txtConfirmPassword.getText();

        signUpForm.txtConfirmPassword.clear();
        signUpForm.txtPassword.clear();

        if(username.isEmpty() || password1.isEmpty() || password2.isEmpty()){
            signUpForm.showNotificationPane("Please fill up all fields!");
        }else if(!password1.equals(password2)){
            signUpForm.showNotificationPane("Password does not match!");
        }else {
            UserCreate user = new UserCreate(username,password1);
            if(user.isExist()){
                signUpForm.showNotificationPane("Username already exists!");
                signUpForm.txtUsername.requestFocus();
            }else{
                signInForm.txtUsername.setText(username);
                switchForm();
                signInForm.txtPassword.requestFocus();

//                user.insertUser();
            }
        }

    }

    private void onSuccess(){

    }

    private void switchToMainApp() {
        Stage loginStage = (Stage) this.btnSwitch.getScene().getWindow();

        Stage mainStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/mainView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("objects\\logo_ph.png"))));
        mainStage.setScene(scene);
        mainStage.setTitle("NightSpot");
        mainStage.setResizable(true);
        mainStage.resizableProperty().setValue(Boolean.FALSE);

        loginStage.close();
        mainStage.show();
    }


}
