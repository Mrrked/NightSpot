package app.myapp.controller.component.element;

import app.myapp.Main;
import app.myapp.model.user.data.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class Spot_Info extends VBox {

    public String ownerID, spotID;
    public int countMember;

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

    public Spot_Info(User user, String id, String ownerID, String name, String member, String post, String desc, String rules) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/spotInfo.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
        this.spotID = id;
        this.ownerID = ownerID;
        this.spotName.setText(name);
        this.spotCountMembers.setText(member + " members");
        this.countMember = Integer.parseInt(member);
        this.spotDescription.setText(desc);
        this.spotCountPosts.setText(post + " posts");
        this.spotRulesText.setText(rules);

        if(!user.getJoinedSpots().isEmpty() && user.getJoinedSpots().contains(spotID) ){
            spotInfoIcon.setIconColor(Paint.valueOf("#DC4731"));
            spotJoinBtn.setText("JOINED");
        }
    }

    public void switchState(){
        if (spotJoinBtn.getText().equals("JOIN")){
            spotInfoIcon.setIconColor(Paint.valueOf("#DC4731"));
            spotJoinBtn.setText("JOINED");
            this.spotCountMembers.setText((++countMember ) + " members");
        }else if(spotJoinBtn.getText().equals("JOINED")){
            spotInfoIcon.setIconColor(Paint.valueOf("#E0D3DE"));
            spotJoinBtn.setText("JOIN");
            this.spotCountMembers.setText((--countMember ) + " members");
        }
    }

}