package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class CardComment extends VBox {
    @FXML
    private Label cComAuthor;
    @FXML
    private Label cComDate;
    @FXML
    private FontIcon cComDownBtn;
    @FXML
    private Label cComMessage;
    @FXML
    private Label cComRepliesCount;
    @FXML
    private Label cComReplyBtn;
    @FXML
    private Label cComShowBtn;
    @FXML
    private FontIcon cComUpBtn;
    @FXML
    private Label cComVotes;
    @FXML
    private VBox replyPane;

    public CardComment() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/contentCommentCard.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }


}