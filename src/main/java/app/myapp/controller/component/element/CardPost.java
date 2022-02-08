package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class CardPost extends HBox {
    @FXML
    private Label cPostCardAuthor;
    @FXML
    private Label cPostCardComments;
    @FXML
    private Label cPostCardDate;
    @FXML
    private Label cPostCardTitle;
    @FXML
    private FontIcon cPostCardUpBtn;
    @FXML
    private Label cPostCardVotes;


    public CardPost() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/contentPostCard.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }


}