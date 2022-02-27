package app.myapp.controller.component.element;

import app.myapp.Main;
import app.myapp.database.Driver;
import app.myapp.model.user.data.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CardPost extends HBox {

    public String postID, spotID, authorID;
    public int countVotes;
    public int v = 0;

    @FXML
    public Label cPostCardAuthor;
    @FXML
    private Label cPostCardComments;
    @FXML
    private Label cPostCardDate;
    @FXML
    public Label cPostCardTitle;
    @FXML
    private Label cPostCardVotes;
    @FXML
    public FontIcon cPostCardUpBtn;
    @FXML
    public FontIcon cPostCardDownBtn;
    @FXML
    public VBox cPostLink;

    public CardPost(
            User user,
            String spotID,
            String postID,
            String authorID,
            String title,
            String authorName,
            String votes,
            String comments,
            String date
    ) throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/contentPostCard.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();

        this.spotID = spotID;
        this.authorID = authorID;
        this.postID = postID;
        this.cPostCardTitle.setText(title);
        this.cPostCardVotes.setText(addCommasToNumericString(votes));
        this.cPostCardComments.setText(addCommasToNumericString(comments) + " comments");
        this.cPostCardAuthor.setText(authorName);
        this.countVotes = Integer.parseInt(votes);

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = dt.parse(date);
        SimpleDateFormat dt1 = new SimpleDateFormat("d MMM yyyy hh:mm a");

        String strDate = dt1.format(newDate);
        this.cPostCardDate.setText(strDate);

        if(user.getUpPost().size() != 0 && user.getUpPost().contains(postID)){
            switchColor(cPostCardUpBtn,true);
            switchColor(cPostCardDownBtn,false);
            v = 1;
        }else if(user.getDownPost().size() != 0 && user.getDownPost().contains(postID)){
            switchColor(cPostCardUpBtn,false);
            switchColor(cPostCardDownBtn,true);
            v = -1;
        }

    }

    public void switchVotes(User user, int u, String postID) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String sql1,sql2;
        String up, down;
        ArrayList<String> upPosts = user.getUpPost();
        ArrayList<String> downPosts = user.getDownPost();

        //UP|DOWN
        switch(v){
            case -1:    //FROM DOWN
                if(u == 1){ //TO UP
                    //INSERTION
                    if(!upPosts.isEmpty()){
                        upPosts.add(postID);
                        up = String.join(",", upPosts);
                    }else{
                        up = postID;
                    }
                    //REMOVAL
                    downPosts.remove(postID);
                    if(!downPosts.isEmpty()){
                        down = String.join(",", downPosts);
                    }else{
                        down = "";
                    }
                    sql1 = "UPDATE `user_tbl` SET `upPost` = '" + up + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    sql2 = "UPDATE `user_tbl` SET `downPost` = '" + down + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);
                    db_con.executeSQL(sql2);

                    countVotes += 2;
                    switchColor(cPostCardUpBtn,true);
                    switchColor(cPostCardDownBtn,false);
                    v = 1;
                }else if(u == -1){ //DOWN
                    //REMOVAL
                    downPosts.remove(postID);
                    if(!downPosts.isEmpty()){
                        down = String.join(",", downPosts);
                    }else{
                        down = "";
                    }
                    sql2 = "UPDATE `user_tbl` SET `downPost` = '" + down + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql2);

                    countVotes += 1;
                    switchColor(cPostCardUpBtn,false);
                    switchColor(cPostCardDownBtn,false);
                    v = 0;
                }
                break;

            case 0:     //FROM NEITHER
                if(u == 1){

                    //INSERTION
                    if(upPosts.size() != 0 ){
                        upPosts.add(postID);
                        up = String.join(",", upPosts);
                    }else{
                        up = postID;
                    }
                    sql1 = "UPDATE `user_tbl` SET `upPost` = '" + up + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);

                    countVotes += 1;
                    switchColor(cPostCardUpBtn,true);
                    switchColor(cPostCardDownBtn,false);
                    v = 1;
                }else if(u == -1){
                    //INSERTION
                    if(!downPosts.isEmpty()){
                        downPosts.add(postID);
                        down = String.join(",", downPosts);
                    }else{
                        down = postID;
                    }

                    sql1 = "UPDATE `user_tbl` SET `downPost` = '" + down + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);

                    countVotes -= 1;
                    switchColor(cPostCardUpBtn,false);
                    switchColor(cPostCardDownBtn,true);
                    v = -1;
                }
                break;

            case 1:     //FROM UP
                if(u == 1){
                    //REMOVAL
                    upPosts.remove(postID);
                    if(!upPosts.isEmpty()){
                        up = String.join(",", upPosts);
                    }else{
                        up = "";
                    }
                    sql1 = "UPDATE `user_tbl` SET `upPost` = '" + up + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);

                    countVotes -= 1;
                    switchColor(cPostCardUpBtn,false);
                    switchColor(cPostCardDownBtn,false);
                    v = 0;
                }else if(u == -1){
                    //INSERTION
                    if(!downPosts.isEmpty()){
                        downPosts.add(postID);
                        down = String.join(",", downPosts);
                    }else{
                        down = postID;
                    }
                    //REMOVAL
                    upPosts.remove(postID);
                    if(!upPosts.isEmpty()){
                        up = String.join(",", upPosts);
                    }else{
                        up = "";
                    }
                    sql1 = "UPDATE `user_tbl` SET `upPost` = '" + up + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    sql2 = "UPDATE `user_tbl` SET `downPost` = '" + down + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);
                    db_con.executeSQL(sql2);

                    countVotes -= 2;
                    switchColor(cPostCardUpBtn,false);
                    switchColor(cPostCardDownBtn,true);
                    v = -1;
                }
                break;
        }
        this.cPostCardVotes.setText(String.valueOf(addCommasToNumericString(String.valueOf(countVotes))));

    }

    public void switchColor(FontIcon icon, Boolean x){
        if (x){
            icon.setIconColor(Paint.valueOf("#DC4731"));//SIGN
        }else {
            icon.setIconColor(Paint.valueOf("#E0D3DE"));//LIGHT
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