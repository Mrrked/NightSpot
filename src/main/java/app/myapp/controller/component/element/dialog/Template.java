package app.myapp.controller.component.element.dialog;

import app.myapp.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

import java.io.IOException;

public class Template extends DialogPane {

    public Template() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("fxml/component/elements/dialogs/template.fxml")
        );
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

}