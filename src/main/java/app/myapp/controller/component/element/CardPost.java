package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CardPost extends HBox {

    public String postID, spotID, authorID;

    @FXML
    public Label cPostCardAuthor;
    @FXML
    private Label cPostCardComments;
    @FXML
    private Label cPostCardDate;
    @FXML
    public Label cPostCardTitle;
    @FXML
    private Label cPostCardVotes;
    @FXML
    public FontIcon cPostCardUpBtn;
    @FXML
    public FontIcon cPostCardDownBtn;
    @FXML
    public VBox cPostLink;

    public CardPost(
            String spotID,
            String postID,
            String authorID,
            String title,
            String authorName,
            String votes,
            String comments,
            String date
    ) throws IOException, ParseException {
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
        this.cPostCardAuthor.setText(authorName);

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = dt.parse(date);
        SimpleDateFormat dt1 = new SimpleDateFormat("d MMM yyyy hh:mm a");

        String strDate = dt1.format(newDate);
        this.cPostCardDate.setText(strDate);
    }




}