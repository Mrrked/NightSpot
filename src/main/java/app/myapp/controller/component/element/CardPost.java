package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class CardPost extends HBox {

    public String postID, spotID, authorID;

    @FXML
    public Label cPostCardAuthor;
    @FXML
    private Label cPostCardComments;
    @FXML
    private Label cPostCardDate;
    @FXML
    private Label cPostCardTitle;
    @FXML
    private Label cPostCardVotes;
    @FXML
    private FontIcon cPostCardUpBtn;
    @FXML
    private FontIcon cPostCardDownBtn;

    public CardPost(
            String spotID,
            String postID,
            String authorID,
            String title,
            String authorName,
            String votes,
            String comments,
            String date
    ) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/contentPostCard.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();

        this.spotID = spotID;
        this.authorID = authorID;
        this.postID = postID;
        this.cPostCardTitle.setText(title);
        this.cPostCardVotes.setText(votes);
        this.cPostCardComments.setText(comments + " comments");
        this.cPostCardDate.setText(date);
        this.cPostCardAuthor.setText(authorName);
    }




}