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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Content_MainPost extends VBox {

    public String ownerID, spotID, postID;

    @FXML
    public Label cMainPostAddCommentBtn;
    @FXML
    private Label cMainPostAuthor;
    @FXML
    public Label cMainPostComments;
    @FXML
    private Label cMainPostDate;
    @FXML
    private FontIcon cMainPostDownBtn;
    @FXML
    private Label cMainPostTitle;
    @FXML
    private FontIcon cMainPostUpBtn;
    @FXML
    private Label cMainPostVotes;
    @FXML
    private WebView cMainPostWebView;
    @FXML
    public VBox commentPane;
    @FXML
    public FontIcon cPostReturn;

    public Content_MainPost(String id, String authorID, String spotID, String authorName, String title, String vote, String comment, String message, String date) throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/contentMainPost.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
        this.postID = id;
        this.ownerID = authorID;
        this.spotID = spotID;
        this.cMainPostAuthor.setText(authorName);
        this.cMainPostTitle.setText(title);
        this.cMainPostVotes.setText(vote);
        this.cMainPostComments.setText(comment + " COMMENTS");

        if(message.contains("contenteditable=\"true\"")){
            message=message.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
        }

        this.cMainPostWebView.getEngine().loadContent(message);

        System.out.println("Comments Count: " + comment);
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = dt.parse(date);
        SimpleDateFormat dt1 = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
        String strDate = dt1.format(newDate);
        this.cMainPostDate.setText(strDate);
    }

}