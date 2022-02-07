package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Spot_JoinedList extends VBox {

    @FXML
    public VBox paneList;

    public Spot_JoinedList() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/spotJoinedList.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

}