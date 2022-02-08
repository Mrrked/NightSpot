package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class CardReply extends HBox {
    @FXML
    private Label cReplyAuthor;
    @FXML
    private Label cReplyDate;
    @FXML
    private Label cReplyMessage;
    @FXML
    private Label cReplyTime;

    public CardReply() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/contentReplyCard.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }


}