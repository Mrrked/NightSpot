package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class CardSpot extends HBox {
    @FXML
    private Label cardSpotDescription;
    @FXML
    private Label cardSpotJoinBtn;
    @FXML
    private Label cardSpotMembers;
    @FXML
    private Label cardSpotPosts;
    @FXML
    private Label cardSpotTitle;
    @FXML
    private FontIcon cardSpotJoinIcon;

    public CardSpot() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/contentSpotCard.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }
}