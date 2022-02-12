package app.myapp.controller.component.element.dialog;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class Comment extends DialogPane {
    @FXML
    public TextArea txtMessage;

    public Comment() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("fxml/component/elements/dialogs/Dialog_Comment.fxml")
        );
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

}