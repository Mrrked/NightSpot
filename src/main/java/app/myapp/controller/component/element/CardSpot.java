package app.myapp.controller.component.element;

import app.myapp.Main;
import app.myapp.model.user.data.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Objects;

public class CardSpot extends HBox {

    public String spotID;
    public int countMember;


    @FXML
    public Label cardSpotJoinBtn;
    @FXML
    public FontIcon cardSpotJoinIcon;
    @FXML
    public Label cardSpotTitle;

    @FXML
    public Label cardSpotMembers;
    @FXML
    private Label cardSpotPosts;
    @FXML
    private Label cardSpotDescription;

    public CardSpot(User user, String id, String title, String desc, String member, String post) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/contentSpotCard.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
        this.spotID = id;
        this.cardSpotTitle.setText(title);
        this.cardSpotDescription.setText(desc);
        this.countMember = Integer.parseInt(member);
        this.cardSpotMembers.setText(addCommasToNumericString(member) + " members");
        this.cardSpotPosts.setText(post + " posts");

        if(!user.getJoinedSpots().isEmpty() && user.getJoinedSpots().contains(spotID) ){
            cardSpotJoinIcon.setIconColor(Paint.valueOf("#DC4731"));
            cardSpotJoinBtn.setText("JOINED");
        }
    }

    public void switchState(){
        if (cardSpotJoinBtn.getText().equals("JOIN")){
            cardSpotJoinIcon.setIconColor(Paint.valueOf("#DC4731"));
            cardSpotJoinBtn.setText("JOINED");
            this.cardSpotMembers.setText(addCommasToNumericString(String.valueOf(++countMember)) + " members");
        }else if(cardSpotJoinBtn.getText().equals("JOINED")){
            cardSpotJoinIcon.setIconColor(Paint.valueOf("#E0D3DE"));
            cardSpotJoinBtn.setText("JOIN");
            this.cardSpotMembers.setText(addCommasToNumericString(String.valueOf(--countMember)) + " members");
        }
    }
    private String addCommasToNumericString (String digits)
    {
        String result = "";
        int len = digits.length();
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = digits.charAt(i) + result;
            nDigits++;
            if (((nDigits % 3) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result);
    }
}