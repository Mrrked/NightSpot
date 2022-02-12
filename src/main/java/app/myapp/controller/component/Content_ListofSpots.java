package app.myapp.controller.component;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class Content_ListofSpots extends VBox {

    @FXML
    private Label cListSpotTitle;
    @FXML
    public Pagination cPageSpots;

    public Content_ListofSpots() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/contentListofSpots.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }
}