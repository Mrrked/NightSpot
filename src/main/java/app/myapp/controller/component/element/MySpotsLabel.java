package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class MySpotsLabel extends HBox {

    @FXML
    private Text mySpotText;

    public MySpotsLabel(String text) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/MySpotsLabel.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
        mySpotText.setText(text);
    }

}