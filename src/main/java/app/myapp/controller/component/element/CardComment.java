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

public class CardComment extends VBox {

    public String commentID, postID, spotID, authorID;
    public int countVotes;
    public int v = 0;

    @FXML
    private Label cComAuthor;
    @FXML
    private Label cComDate;
    @FXML
    public FontIcon cComDownBtn;
    @FXML
    private Label cComMessage;
    @FXML
    public Label cComRepliesCount;
    @FXML
    public Label cComReplyBtn;
    @FXML
    public Label cComShowBtn;
    @FXML
    public FontIcon cComUpBtn;
    @FXML
    private Label cComVotes;
    @FXML
    public VBox replyPane;

    public CardComment(User user, String commentID, String spotID, String postID, String authorID, String vote, String authorName, String reply, String message, String date) throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/component/elements/contentCommentCard.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
        this.commentID = commentID;
        this.spotID = spotID;
        this.postID = postID;
        this.authorID = authorID;
        this.cComVotes.setText(vote);
        this.cComAuthor.setText(authorName);
        this.cComRepliesCount.setText(reply + " replies");
        this.cComMessage.setText(message);
        this.countVotes = Integer.parseInt(vote);

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = dt.parse(date);
        SimpleDateFormat dt1 = new SimpleDateFormat("d-MMM-yy HH:mm");

        String strDate = dt1.format(newDate);
        this.cComDate.setText(strDate);


        if(user.getUpComment().size() != 0 && user.getUpComment().contains(commentID)){
            switchColor(cComUpBtn,true);
            switchColor(cComDownBtn,false);
            v = 1;
        }else if(user.getDownComment().size() != 0 && user.getDownComment().contains(commentID)){
            switchColor(cComUpBtn,false);
            switchColor(cComDownBtn,true);
            v = -1;
        }
    }

    public void switchVotes(User user, int u, String commentID) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String sql1,sql2;
        String up, down;
        ArrayList<String> upComment = user.getUpComment();
        ArrayList<String> downComment = user.getDownComment();

        //UP|DOWN
        switch(v){
            case -1:    //FROM DOWN
                if(u == 1){ //TO UP
                    //INSERTION
                    if(!upComment.isEmpty()){
                        downComment.add(commentID);
                        up = String.join(",", upComment);
                    }else{
                        up = commentID;
                    }
                    //REMOVAL
                    downComment.remove(commentID);
                    if(!downComment.isEmpty()){
                        down = String.join(",", downComment);
                    }else{
                        down = "";
                    }
                    sql1 = "UPDATE `user_tbl` SET `upComment` = '" + up + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    sql2 = "UPDATE `user_tbl` SET `downComment` = '" + down + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);
                    db_con.executeSQL(sql2);

                    countVotes += 2;
                    switchColor(cComUpBtn,true);
                    switchColor(cComDownBtn,false);
                    v = 1;
                }else if(u == -1){ //DOWN
                    //REMOVAL
                    downComment.remove(commentID);
                    if(!downComment.isEmpty()){
                        down = String.join(",", downComment);
                    }else{
                        down = "";
                    }
                    sql2 = "UPDATE `user_tbl` SET `downComment` = '" + down + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql2);

                    countVotes += 1;
                    switchColor(cComUpBtn,false);
                    switchColor(cComDownBtn,false);
                    v = 0;
                }
                break;

            case 0:     //FROM NEITHER
                if(u == 1){

                    //INSERTION
                    if(upComment.size() != 0 ){
                        upComment.add(commentID);
                        up = String.join(",", upComment);
                    }else{
                        up = commentID;
                    }
                    sql1 = "UPDATE `user_tbl` SET `upComment` = '" + up + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);

                    countVotes += 1;
                    switchColor(cComUpBtn,true);
                    switchColor(cComDownBtn,false);
                    v = 1;
                }else if(u == -1){
                    //INSERTION
                    if(!downComment.isEmpty()){
                        downComment.add(commentID);
                        down = String.join(",", downComment);
                    }else{
                        down = commentID;
                    }

                    sql1 = "UPDATE `user_tbl` SET `downComment` = '" + down + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);

                    countVotes -= 1;
                    switchColor(cComUpBtn,false);
                    switchColor(cComDownBtn,true);
                    v = -1;
                }
                break;

            case 1:     //FROM UP
                if(u == 1){
                    //REMOVAL
                    upComment.remove(commentID);
                    if(!upComment.isEmpty()){
                        up = String.join(",", upComment);
                    }else{
                        up = "";
                    }
                    sql1 = "UPDATE `user_tbl` SET `upComment` = '" + up + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);

                    countVotes -= 1;
                    switchColor(cComUpBtn,false);
                    switchColor(cComDownBtn,false);
                    v = 0;
                }else if(u == -1){
                    //INSERTION
                    if(!downComment.isEmpty()){
                        downComment.add(commentID);
                        down = String.join(",", downComment);
                    }else{
                        down = commentID;
                    }
                    //REMOVAL
                    upComment.remove(commentID);
                    if(!upComment.isEmpty()){
                        up = String.join(",", upComment);
                    }else{
                        up = "";
                    }
                    sql1 = "UPDATE `user_tbl` SET `upComment` = '" + up + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    sql2 = "UPDATE `user_tbl` SET `downComment` = '" + down + "' WHERE (`userID` = '" + user.getUserID() + "')";
                    db_con.executeSQL(sql1);
                    db_con.executeSQL(sql2);

                    countVotes -= 2;
                    switchColor(cComUpBtn,false);
                    switchColor(cComDownBtn,true);
                    v = -1;
                }
                break;
        }
        this.cComVotes.setText(String.valueOf(countVotes));

    }


    public void switchColor(FontIcon icon, Boolean x){
        if (x){
            icon.setIconColor(Paint.valueOf("#DC4731"));//SIGN
        }else {
            icon.setIconColor(Paint.valueOf("#E0D3DE"));//LIGHT
        }
    }


}