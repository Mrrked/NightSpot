package app.myapp.database;


import app.myapp.controller.component.ContentMainPost;
import app.myapp.controller.component.element.*;
import app.myapp.model.user.data.User;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Query {

    public Map<String,String> getJoinedSpots(User user) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        Map<String,String> spots = new HashMap<>();
        ArrayList<String> spotsID = user.getJoinedSpots();

        for (String id :spotsID) {
            String sql = "SELECT `spotName` FROM `spot_tbl` WHERE spotID = '" + id + "'";
            ResultSet rs = db_con.executeSQLRS(sql);
            while(rs.next()){
                spots.put(id,rs.getString("spotName"));
            }
        }

        db_con.endConnection();
        return spots;

    }

    //SPOTS
    public String createSpots(@NotNull User user, String name, String desc, String rules) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String newSpotList = null, spotID = null, sql;
        ResultSet rs;

        //CREATE THE SPOT
        sql = "INSERT INTO `spot_tbl` (`spotOwnerID`, `spotRules`, `spotName`, `spotDescription` ) " +
                "VALUES ('"+ user.getUserID() +"', '"+ rules +"', '"+ name +"', '"+ desc +"')";
        db_con.executeSQL(sql);
        
        //GET THE SPOT's ID and CONCAT IT TO THE OWNERS JOINED SPOT LISTS
        sql = "SELECT `spotID` FROM `spot_tbl` WHERE spotOwnerID = '" + user.getUserID() + "' && spotName = '"+ name +"' ";
        rs = db_con.executeSQLRS(sql);

        while(rs.next()) {
            spotID = rs.getString("spotID");
            if(user.getCountJoinedSpots() != 0){
                user.getJoinedSpots().add(spotID);
                newSpotList = String.join(",", user.getJoinedSpots());
            }else{
                newSpotList = spotID;
            }
        }

        System.out.println(newSpotList);

        //UPDATE THE OWNERS JOINED SPOT LIST AND INCREMENT THE COUNT
        sql = "UPDATE `user_tbl` " +
                "SET `countJoinedSpots` = '" + (user.getCountJoinedSpots() + 1) + "', " +
                "`joinedSpots` = '" + newSpotList + "' " +
                "WHERE (`userID` = '" + user.getUserID() + "')";
        db_con.executeSQL(sql);

        db_con.endConnection();
        return spotID;
    }

    public Spot_Info getSpotInfo(User user, String spotID) throws SQLException, IOException{
        Driver db_con = new Driver();
        db_con.startConnection();

        Spot_Info spot = null;

        String sql = "SELECT * FROM `spot_tbl` WHERE spotID = '" + spotID + "' ";
        ResultSet rs = db_con.executeSQLRS(sql);

        String ownerID, name, desc, member, posts, rules;

        while(rs.next()){
            ownerID = rs.getString("spotOwnerID");
            name = rs.getString("spotName");
            desc = rs.getString("spotDescription");
            member = rs.getString("spotCountMember");
            posts = rs.getString("spotCountPost");
            rules = rs.getString("spotRules");
            spot = new Spot_Info(user, spotID, ownerID, name, member, posts, desc, rules);
        }

        db_con.endConnection();
        return spot;
    }

    public void joinSpot(User user, String spotID) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String sql;
        ResultSet rs;
        if(!user.getJoinedSpots().isEmpty() && user.getJoinedSpots().contains(spotID) ){
            user.getJoinedSpots().remove(spotID);
            String newSpotList = String.join(",", user.getJoinedSpots());
            sql = "UPDATE `user_tbl` " +
                    "SET `countJoinedSpots` = '" + (user.getCountJoinedSpots() - 1) + "', " +
                    "`joinedSpots` = '" + newSpotList + "' " +
                    "WHERE (`userID` = '" + user.getUserID() + "')";
//            System.out.println("Unjoin : " + newSpotList);
            db_con.executeSQL(sql);

            int count = 0;
            //GET THE SPOT's MEMBER COUNT
            sql = "SELECT `spotCountMember` FROM `spot_tbl` WHERE spotID = '" + spotID + "' ";
            rs = db_con.executeSQLRS(sql);
            while(rs.next()){
                count = Integer.parseInt(rs.getString("spotCountMember"));
            }

            //INCREMENT THE COUNT OF COMMENT IN THE POST
            sql = "UPDATE `spot_tbl` " +
                    "SET `spotCountMember` = '" + (count - 1) + "' " +
                    "WHERE (`spotID` = '" + spotID + "')";
            db_con.executeSQL(sql);


        }else{
            String newSpotList;

            if(user.getCountJoinedSpots() != 0){
                user.getJoinedSpots().add(spotID);
                newSpotList = String.join(",", user.getJoinedSpots());
            }else{
                newSpotList = spotID;
            }

            sql = "UPDATE `user_tbl` " +
                    "SET `countJoinedSpots` = '" + (user.getCountJoinedSpots() + 1) + "', " +
                    "`joinedSpots` = '" + newSpotList + "' " +
                    "WHERE (`userID` = '" + user.getUserID() + "')";
//            System.out.println("Join : " + newSpotList);
            db_con.executeSQL(sql);

            int count = 0;
            //GET THE SPOT's MEMBER COUNT
            sql = "SELECT `spotCountMember` FROM `spot_tbl` WHERE spotID = '" + spotID + "' ";
            rs = db_con.executeSQLRS(sql);
            while(rs.next()){
                count = Integer.parseInt(rs.getString("spotCountMember"));
            }

            //INCREMENT THE COUNT OF COMMENT IN THE POST
            sql = "UPDATE `spot_tbl` " +
                    "SET `spotCountMember` = '" + (count + 1) + "' " +
                    "WHERE (`spotID` = '" + spotID + "')";
            db_con.executeSQL(sql);
        }

        db_con.endConnection();
    }

    //CARDS
    public ArrayList<CardSpot> getTopSpots(User user) throws SQLException, IOException {
        Driver db_con = new Driver();
        db_con.startConnection();

        ArrayList<CardSpot> topSpots = new ArrayList<>();

        String sql = "SELECT * FROM `spot_tbl` ORDER BY `spotCountMember` DESC LIMIT 5;";
        ResultSet rs = db_con.executeSQLRS(sql);

        String spotId, name, desc, member, posts;

        while(rs.next()){
            spotId = rs.getString("spotID");
            name = rs.getString("spotName");
            desc = rs.getString("spotDescription");
            member = rs.getString("spotCountMember");
            posts = rs.getString("spotCountPost");
            topSpots.add(new CardSpot(user, spotId,name, desc, member, posts));
        }

        db_con.endConnection();
        return topSpots;
    }

    public ArrayList<CardPost> getTopPosts(User user, String spotID, int z) throws SQLException, IOException, ParseException {
        Driver db_con = new Driver();
        db_con.startConnection();

        ArrayList<CardPost> posts = new ArrayList<>();
        String sql = null;
        
        if(z == 1){
            sql = "SELECT * FROM `post_tbl` WHERE spotID = '" + spotID + "' ORDER BY `postCountVotes` DESC LIMIT 50";
        }else if(z == 0){
            sql = "SELECT * FROM `post_tbl` WHERE spotID = '" + spotID + "' ORDER BY `postDateCreated` DESC LIMIT 50";
        }
        

        ResultSet rs = db_con.executeSQLRS(sql);

        String postID, authorID, title, votes, comments, date;
        String authorName = null;

        while(rs.next()){
            postID = rs.getString("postID");
            authorID = rs.getString("postAuthorID");
            title = rs.getString("postTitle");
            votes = rs.getString("postCountVotes");
            comments = rs.getString("postCountComment");
            date = rs.getString("postDateCreated");

            String sql1 = "SELECT `username` FROM `user_tbl` WHERE userID = '" + authorID + "'";
            ResultSet rs1 = db_con.executeSQLRS(sql1);

            while(rs1.next()){
                authorName = rs1.getString("username");
            }
            posts.add(new CardPost(user,spotID,postID,authorID,title, authorName, votes, comments, date));
        }

        db_con.endConnection();
        return posts;
    }

    //POSTS
    public String createPosts(@NotNull User user, String spotID, String title, String htmlText) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String sql, postID = null;
        int count = 0;
        ResultSet rs;

        //CREATE THE POST
        sql = "INSERT INTO `post_tbl` (`spotID`, `postAuthorID`, `postTitle`, `postMessage`) " +
                "VALUES ('"+ spotID +"', '"+ user.getUserID() +"', '"+ title +"', '"+ htmlText +"')";
        db_con.executeSQL(sql);

        //GET THE SPOT's POST COUNT
        sql = "SELECT `spotCountPost` FROM `spot_tbl` WHERE spotID = '" + spotID + "' ";
        rs = db_con.executeSQLRS(sql);
        while(rs.next()){
            count = Integer.parseInt(rs.getString("spotCountPost"));
        }

        //INCREMENT THE COUNT OF POSTS IN THE SPOT
        sql = "UPDATE `spot_tbl` " +
                "SET `spotCountPost` = '" + (count + 1) + "' " +
                "WHERE (`spotID` = '" + spotID + "')";
        db_con.executeSQL(sql);

        //GET POST ID
        sql = "SELECT `postID` FROM `post_tbl` WHERE (spotID = '" + spotID + "' && postAuthorID = '" + user.getUserID() + "' && postTitle = '" + title + "'  )";
        rs = db_con.executeSQLRS(sql);
        while(rs.next()){
            postID = rs.getString("postID");
        }

        db_con.endConnection();
        return postID;
    }

    public ContentMainPost getPostInfo(User user, String spotID, String postID) throws SQLException, IOException, ParseException {
        Driver db_con = new Driver();
        db_con.startConnection();

        ContentMainPost mainPost = null;

        String sql = "SELECT * FROM `post_tbl` WHERE postID = '" + postID + "' ";
        ResultSet rs = db_con.executeSQLRS(sql);

        String authorID, authorName = null, title, text, date, votes, comments;

        while(rs.next()){
            authorID = rs.getString("postAuthorID");
            title = rs.getString("postTitle");
            text = rs.getString("postMessage");
            date = rs.getString("postDateCreated");
            votes = rs.getString("postCountVotes");
            comments = rs.getString("postCountComment");

            String sql1 = "SELECT `username` FROM `user_tbl` WHERE userID = '" + authorID + "'";
            ResultSet rs1 = db_con.executeSQLRS(sql1);

            while(rs1.next()){
                authorName = rs1.getString("username");
            }
            mainPost = new ContentMainPost(user,postID, authorID, spotID, authorName,title, votes, comments,text,date);
        }
        

        db_con.endConnection();
        return mainPost;
    }

    public void changeVote(User user, ContentMainPost contentMainPost, String button) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        if(button.equals("up")){
            contentMainPost.switchVotes(user,1, contentMainPost.postID);
        }else if(button.equals("down")){
            contentMainPost.switchVotes(user,-1, contentMainPost.postID);
        }

        int count = contentMainPost.countVotes;

        //UPDATE THE POSTS' VOTE COUNT
        String sql = "UPDATE `post_tbl` " +
                "SET `postCountVotes` = '" + (count) + "' " +
                "WHERE (`postID` = '" + contentMainPost.postID + "')";
        db_con.executeSQL(sql);

        db_con.endConnection();
    }

    public void changeVote(User user, CardPost post, String button) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        if(button.equals("up")){
            post.switchVotes(user,1, post.postID);
        }else if(button.equals("down")){
            post.switchVotes(user,-1, post.postID);
        }

        int count = post.countVotes;

        //UPDATE THE POSTS' VOTE COUNT
        String sql = "UPDATE `post_tbl` " +
                "SET `postCountVotes` = '" + (count) + "' " +
                "WHERE (`postID` = '" + post.postID + "')";
        db_con.executeSQL(sql);

        db_con.endConnection();
    }

    public void changeVote(User user, CardComment comment, String button) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        if(button.equals("up")){
            comment.switchVotes(user,1, comment.commentID);
        }else if(button.equals("down")){
            comment.switchVotes(user,-1, comment.commentID);
        }

        int count = comment.countVotes;

        //UPDATE THE COMMENT'S VOTE COUNT
        String sql = "UPDATE `comment_tbl` " +
                "SET `commentCountVotes` = '" + (count) + "' " +
                "WHERE (`commentID` = '" + comment.commentID + "')";
        db_con.executeSQL(sql);

        db_con.endConnection();
    }

    //COMMENTS
    public void createComments(@NotNull User user, String message, String postID, String spotID) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String sql;
        int count = 0;
        ResultSet rs;

        //CREATE THE COMMENT
        sql = "INSERT INTO `comment_tbl` (`spotID`, `postID`, `commentAuthorID`, `commentMessage`) " +
                "VALUES ('"+ spotID +"', '"+ postID +"', '"+ user.getUserID() +"', \""+ message +"\")";
        db_con.executeSQL(sql);

        //GET THE POST's COMMENT COUNT
        sql = "SELECT `postCountComment` FROM `post_tbl` WHERE postID = '" + postID + "' ";
        rs = db_con.executeSQLRS(sql);
        while(rs.next()){
            count = Integer.parseInt(rs.getString("postCountComment"));
        }

        //INCREMENT THE COUNT OF COMMENT IN THE POST
        sql = "UPDATE `post_tbl` " +
                "SET `postCountComment` = '" + (count + 1) + "' " +
                "WHERE (`postID` = '" + postID + "')";
        db_con.executeSQL(sql);

        db_con.endConnection();
    }

    public ArrayList<CardComment> getComments(User user, String postID) throws SQLException, IOException, ParseException {
        Driver db_con = new Driver();
        db_con.startConnection();

        ArrayList<CardComment> comments = new ArrayList<>();

        String sql = "SELECT * FROM `comment_tbl` WHERE postID = '" + postID + "' ORDER BY `commentCountVotes` DESC LIMIT 20";
        ResultSet rs = db_con.executeSQLRS(sql);

        String commentID, spotID, authorID, vote, reply, message, date;
        String authorName = null;

        while(rs.next()){
            commentID = rs.getString("commentID");
            spotID = rs.getString("spotID");
            authorID = rs.getString("commentAuthorID");
            vote = rs.getString("commentCountVotes");
            reply = rs.getString("commentCountReplies");
            message = rs.getString("commentMessage");
            date = rs.getString("commentDateCreated");

            String sql1 = "SELECT `username` FROM `user_tbl` WHERE userID = '" + authorID + "'";
            ResultSet rs1 = db_con.executeSQLRS(sql1);

            while(rs1.next()){
                authorName = rs1.getString("username");
            }
            comments.add(new CardComment(user, commentID,spotID,postID,authorID,vote, authorName, reply, message, date));
        }

        db_con.endConnection();
        return comments;
    }

    //REPLIES
    public void createReplies(@NotNull User user, String message, CardComment comment) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String sql;
        int count = 0;
        ResultSet rs;

        //CREATE THE REPLY
        sql = "INSERT INTO `reply_tbl` (`spotID`, `postID`,`commentID`, `replyAuthorID`, `replyMessage`) " +
                "VALUES ('"+ comment.spotID +"', '"+ comment.postID +"', '"+ comment.commentID +"', '"+ user.getUserID() +"', \""+ message +"\")";
        db_con.executeSQL(sql);

        //GET THE COMMENT's POST COUNT
        sql = "SELECT `commentCountReplies` FROM `comment_tbl` WHERE commentID = '" + comment.commentID + "' ";
        rs = db_con.executeSQLRS(sql);
        while(rs.next()){
            count = Integer.parseInt(rs.getString("commentCountReplies"));
        }

        //INCREMENT THE COUNT OF REPLIES IN THE COMMENT
        sql = "UPDATE `comment_tbl` " +
                "SET `commentCountReplies` = '" + (count + 1) + "' " +
                "WHERE (`commentID` = '" + comment.commentID + "')";
        db_con.executeSQL(sql);

        db_con.endConnection();
    }

    public ArrayList<CardReply> getReplies(String commentID) throws SQLException, IOException, ParseException {
        Driver db_con = new Driver();
        db_con.startConnection();

        ArrayList<CardReply> replies = new ArrayList<>();

        String sql = "SELECT * FROM `reply_tbl` WHERE commentID = '" + commentID + "' ORDER BY `replyDateCreated` DESC ";
        ResultSet rs = db_con.executeSQLRS(sql);

        String replyID, authorID, message, date;
        String authorName = null;

        while(rs.next()){
            replyID = rs.getString("commentID");
            authorID = rs.getString("replyAuthorID");
            message = rs.getString("replyMessage");
            date = rs.getString("replyDateCreated");

            String sql1 = "SELECT `username` FROM `user_tbl` WHERE userID = '" + authorID + "'";
            ResultSet rs1 = db_con.executeSQLRS(sql1);

            while(rs1.next()){
                authorName = rs1.getString("username");
            }
            replies.add(new CardReply(replyID,commentID,authorID,authorName,message, date));
        }

        db_con.endConnection();
        return replies;
    }

}

