package app.myapp.model.user.data;

import app.myapp.database.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    //STATES
    private final String userID;
    private String username;
    private String role;
    private String joinedSpots;
    private int countJoinedSpots;

    //CONSTRUCTOR
    public User(String userID) {
        this.userID = userID;
    }

    //GETTERS
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getJoinedSpots() { return joinedSpots; }
    public int getCountJoinedSpots() { return countJoinedSpots; }

    public void updateData() throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String sql = "SELECT `username`, `role`, `countJoinedSpots`,`joinedSpots` FROM `user_tbl` WHERE userID = '" + getUserID() + "'";
        ResultSet rs = db_con.executeSQLRS(sql);

        while(rs.next()){
            this.username = rs.getString("username");
            this.role = rs.getString("role");
            this.countJoinedSpots =  Integer.parseInt(rs.getString("countJoinedSpots"));
            this.joinedSpots = rs.getString("joinedSpots");
        }
        System.out.println("User Data: | "
                + username + " | "
                + role + " | "
                + countJoinedSpots + " | "
                + joinedSpots + " | "
        );

        db_con.endConnection();
    }

}
