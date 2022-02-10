package app.myapp.controller.component.element.dialog;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Spot extends DialogPane {
    @FXML
    public Label dSpotsTitle;
    @FXML
    public TextArea txtDesc;
    @FXML
    public TextField txtName;
    @FXML
    public TextArea txtRules;

    public Spot() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("fxml/component/elements/dialogs/Dialog_Spots.fxml")
        );
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

}