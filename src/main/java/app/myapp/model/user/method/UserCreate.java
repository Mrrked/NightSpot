package app.myapp.model.user.method;

import app.myapp.database.Driver;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCreate extends Method{

    //Constructor
    public UserCreate(String username, String password) {
        super(username, password);
    }

    //Check if username already exist
    public boolean isExist() throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();
        String result;

        String sql = "SELECT `username` FROM `user_tbl` WHERE username = '" + getUsername() + "'";
        ResultSet rs = db_con.executeSQLRS(sql);

        while(rs.next()){
            result = rs.getString("username");
            if(result.equals(getUsername())){
                db_con.endConnection();
                return true;
            }
        }

        db_con.endConnection();
        return false;
    }

    //Insert user to database
    public void insertUser() throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();

        String sql = "INSERT INTO `user_tbl` (`username`, `password`, `joinedSpots`, `upPost`, `upComment`, `downPost`, `downComment`) " +
                "VALUES ('" + getUsername() + "', '" + getEncryptedPassword() + "', '', '', '', '', '')";
        db_con.executeSQL(sql);

        System.out.println("CREATE USER: SUCCESS");
        db_con.endConnection();
    }

}
