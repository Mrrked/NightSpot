package app.myapp.controller.component;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Content_ListofPosts extends VBox {

    @FXML
    private Label cLatestBtn;
    @FXML
    private Label cListPostTitle;
    @FXML
    private Pagination cPagePosts;
    @FXML
    private Label cPopularBtn;

    public Content_ListofPosts() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/contentListofPosts.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }
}