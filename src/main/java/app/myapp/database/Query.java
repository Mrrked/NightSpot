package app.myapp.database;

import app.myapp.controller.component.element.CardPost;
import app.myapp.controller.component.element.CardSpot;
import app.myapp.controller.component.element.Spot_Info;
import app.myapp.model.user.data.User;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Query {

    public void createSpots(@NotNull User user, String name, String desc, String rules) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String newSpotList = null, sql;
        ResultSet rs;

        //CREATE THE SPOT
        sql = "INSERT INTO `spot_tbl` (`spotOwnerID`, `spotRules`, `spotName`, `spotDescription` ) " +
                "VALUES ('"+ user.getUserID() +"', '"+ rules +"', '"+ name +"', '"+ desc +"')";
        db_con.executeSQL(sql);
        
        //GET THE SPOT's ID and CONCAT IT TO THE OWNERS JOINED SPOT LISTS
        sql = "SELECT `spotID` FROM `spot_tbl` WHERE spotOwnerID = '" + user.getUserID() + "' && spotName = '"+ name +"' ";
        rs = db_con.executeSQLRS(sql);
        while(rs.next()) {
            newSpotList = user.getJoinedSpots() + rs.getString("spotID") + ",";
        }

        //UPDATE THE OWNERS JOINED SPOT LIST AND INCREMENT THE COUNT
        sql = "UPDATE `user_tbl` " +
                "SET `countJoinedSpots` = '" + (user.getCountJoinedSpots() + 1) + "', " +
                "`joinedSpots` = '" + newSpotList + "' " +
                "WHERE (`userID` = '" + user.getUserID() + "')";
        db_con.executeSQL(sql);

        db_con.endConnection();
    }

    public Map<String,String> getJoinedSpots(User user) throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        Map<String,String> spots = new HashMap<>();
        String[] spotsID = user.getJoinedSpots().split(",");

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

    public ArrayList<CardSpot> getTopSpots() throws SQLException, IOException {
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
            topSpots.add(new CardSpot(spotId,name, desc, member, posts));
        }

        db_con.endConnection();
        return topSpots;
    }

    public Spot_Info getSpotInfo(String spotID) throws SQLException, IOException{
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
            spot = new Spot_Info(spotID, ownerID, name, member, posts, desc, rules);
        }

        db_con.endConnection();
        return spot;
    }

    public ArrayList<CardPost> getTopPosts(String spotID) throws SQLException, IOException {
        Driver db_con = new Driver();
        db_con.startConnection();

        ArrayList<CardPost> posts = new ArrayList<>();

        String sql = "SELECT * FROM `post_tbl` WHERE spotID = '" + spotID + "' ORDER BY `postCountVotes` DESC LIMIT 50";
        ResultSet rs = db_con.executeSQLRS(sql);

        String postID, authorID, title, authorName = null, votes, comments, date;

        while(rs.next()){
            postID = rs.getString("postID");
            authorID = rs.getString("postAuthorID");
            title = rs.getString("postTitle");
            votes = rs.getString("postCountVotes");
            comments = rs.getString("postCountComment");
            date = rs.getString("postDateCreated");

            String sql1 = "SELECT `username` FROM `user_tbl` WHERE userID = '" + authorID + "'";
            ResultSet rs1 = db_con.executeSQLRS(sql1);
            while(rs.next()){
                authorName = rs1.getString("username");
            }

            posts.add(new CardPost(spotID,postID,authorID,title, authorName, votes, comments, date));
        }

        db_con.endConnection();
        return posts;
    }


}
/*
    public void template() throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String sql = "SELECT `username`, `role` FROM `user_tbl` WHERE userID = '" + user.getUserID() + "'";
        ResultSet rs = db_con.executeSQLRS(sql);

        while(rs.next()){
            this.username = rs.getString("username");
            this.role = rs.getString("role");
        }

        db_con.endConnection();
    }

 */