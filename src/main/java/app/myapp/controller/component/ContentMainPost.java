package app.myapp.controller.component;

import app.myapp.Main;
import app.myapp.database.Driver;
import app.myapp.model.user.data.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ContentMainPost extends VBox {

    public String ownerID, spotID, postID;
    public int countVotes;
    public int v = 0;

    @FXML
    public Label cMainPostAddCommentBtn;
    @FXML
    private Label cMainPostAuthor;
    @FXML
    public Label cMainPostComments;
    @FXML
    private Label cMainPostDate;
    @FXML
    private Label cMainPostTitle;
    @FXML
    private Label cMainPostVotes;
    @FXML
    private WebView cMainPostWebView;
    @FXML
    public VBox commentPane;
    @FXML
    public FontIcon cPostReturn;

    @FXML
    public FontIcon cMainPostDownBtn;
    @FXML
    public FontIcon cMainPostUpBtn;

    public ContentMainPost(User user, String id, String authorID, String spotID, String authorName, String title, String vote, String comment, String message, String date) throws IOException, ParseException, SQLException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/contentMainPost.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
        this.postID = id;
        this.ownerID = authorID;
        this.spotID = spotID;
        this.cMainPostAuthor.setText(authorName);
        this.cMainPostTitle.setText(title);
        this.cMainPostVotes.setText(vote);
        this.cMainPostComments.setText(comment + " COMMENTS");
        this.countVotes = Integer.parseInt(vote);

        //WEBVIEW
        if(message.contains("contenteditable=\"true\"")){
            message=message.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
        }
        this.cMainPostWebView.getEngine().loadContent(message);

        //DATE
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = dt.parse(date);
        SimpleDateFormat dt1 = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
        String strDate = dt1.format(newDate);
        this.cMainPostDate.setText(strDate);

        if(user.getUpPost().size() != 0 && user.getUpPost().contains(postID)){
            switchColor(cMainPostUpBtn,true);
            switchColor(cMainPostDownBtn,false);
            v = 1;
        }else if(user.getDownPost().size() != 0 && user.getDownPost().contains(postID)){
            switchColor(cMainPostUpBtn,false);
            switchColor(cMainPostDownBtn,true);
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
                    switchColor(cMainPostUpBtn,true);
                    switchColor(cMainPostDownBtn,false);
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
                    switchColor(cMainPostUpBtn,false);
                    switchColor(cMainPostDownBtn,false);
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
                    switchColor(cMainPostUpBtn,true);
                    switchColor(cMainPostDownBtn,false);
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
                    switchColor(cMainPostUpBtn,false);
                    switchColor(cMainPostDownBtn,true);
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
                    switchColor(cMainPostUpBtn,false);
                    switchColor(cMainPostDownBtn,false);
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
                    switchColor(cMainPostUpBtn,false);
                    switchColor(cMainPostDownBtn,true);
                    v = -1;
                }
                break;
        }
        this.cMainPostVotes.setText(String.valueOf(countVotes));

    }

    public void switchColor(FontIcon icon, Boolean x){
        if (x){
            icon.setIconColor(Paint.valueOf("#DC4731"));//SIGN
        }else {
            icon.setIconColor(Paint.valueOf("#E0D3DE"));//LIGHT
        }
    }


}