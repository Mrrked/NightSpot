package app.myapp.controller.component;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class Content_MainPost extends VBox {

    @FXML
    private Label cMainPostAddCommentBtn;
    @FXML
    private Label cMainPostAuthor;
    @FXML
    private Label cMainPostComments;
    @FXML
    private Label cMainPostDate;
    @FXML
    private FontIcon cMainPostDownBtn;
    @FXML
    private Label cMainPostTime;
    @FXML
    private Label cMainPostTitle;
    @FXML
    private FontIcon cMainPostUpBtn;
    @FXML
    private Label cMainPostVotes;
    @FXML
    private WebView cMainPostWebView;
    @FXML
    private HBox commentPane;

    public Content_MainPost() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/contentMainPost.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

}