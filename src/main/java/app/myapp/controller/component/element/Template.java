package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class Template extends Label {
    public Template() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/NotificationPane.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }
}