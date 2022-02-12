package app.myapp.controller.component.element.dialog;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Reply extends DialogPane {
    @FXML
    public TextArea txtMessage;

    public Reply() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("fxml/component/elements/dialogs/Dialog_Reply.fxml")
        );
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

}