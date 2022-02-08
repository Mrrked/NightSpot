package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PageContainer extends VBox {
    @FXML
    public VBox container;

    public PageContainer() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/pageContainer.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }
}