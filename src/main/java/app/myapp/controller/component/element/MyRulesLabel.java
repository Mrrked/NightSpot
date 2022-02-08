package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class MyRulesLabel extends Label {

    public MyRulesLabel(String text) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/MyRulesLabel.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
        this.setText(text);
    }

}