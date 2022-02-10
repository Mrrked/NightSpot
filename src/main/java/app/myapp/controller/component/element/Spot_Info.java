package app.myapp.controller.component.element;

import app.myapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class Spot_Info extends VBox {

    public String ownerID, spotID;

    @FXML
    public Button spotCPostBtn;
    @FXML
    public Button spotJoinBtn;
    @FXML
    private Label spotCountMembers;
    @FXML
    private Label spotCountPosts;
    @FXML
    private Label spotDescription;
    @FXML
    private FontIcon spotInfoIcon;
    @FXML
    public Label spotName;
    @FXML
    private TextArea spotRulesText;

    public Spot_Info(String id, String ownerID, String name, String member, String post, String desc, String rules) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/spotInfo.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
        this.spotID = id;
        this.ownerID = ownerID;
        this.spotName.setText(name);
        this.spotCountMembers.setText(member + " members");
        this.spotDescription.setText(desc);
        this.spotCountPosts.setText(post + " posts");
        this.spotRulesText.setText(rules);

    }

}