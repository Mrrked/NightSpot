package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class Spot_Info extends VBox {
    @FXML
    private Button spotCPostBtn;
    @FXML
    private Label spotCountMembers;
    @FXML
    private Label spotCountPosts;
    @FXML
    private Label spotDescription;
    @FXML
    private FontIcon spotInfoIcon;
    @FXML
    private Button spotJoinBtn;
    @FXML
    private Label spotName;
    @FXML
    public VBox spotRulesPane;

    public Spot_Info() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/spotInfo.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

}