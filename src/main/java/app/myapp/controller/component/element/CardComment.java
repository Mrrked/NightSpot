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

public class CardComment extends VBox {

    public String commentID, postID, spotID, authorID;

    @FXML
    private Label cComAuthor;
    @FXML
    private Label cComDate;
    @FXML
    public FontIcon cComDownBtn;
    @FXML
    private Label cComMessage;
    @FXML
    public Label cComRepliesCount;
    @FXML
    public Label cComReplyBtn;
    @FXML
    public Label cComShowBtn;
    @FXML
    public FontIcon cComUpBtn;
    @FXML
    private Label cComVotes;
    @FXML
    public VBox replyPane;

    public CardComment( String commentID, String spotID, String postID, String authorID, String vote, String authorName, String reply, String message, String date) throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/contentCommentCard.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
        this.commentID = commentID;
        this.spotID = spotID;
        this.postID = postID;
        this.authorID = authorID;
        this.cComVotes.setText(vote);
        this.cComAuthor.setText(authorName);
        this.cComRepliesCount.setText(reply + " replies");
        this.cComMessage.setText(message);

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = dt.parse(date);
        SimpleDateFormat dt1 = new SimpleDateFormat("d-MMM-yy HH:mm");

        String strDate = dt1.format(newDate);
        this.cComDate.setText(strDate);
    }


}