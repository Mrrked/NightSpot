package app.myapp.model.user.method;

import app.myapp.database.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuth extends Method{

    //Constructor
    public UserAuth(String username, String password) {
        super(username, password);
    }

    //Check if username already exist and password matches
    public boolean isExist() throws SQLException {
        Driver db_con = new Driver();
        db_con.startConnection();
        String result;

        String sql = "SELECT `password` FROM `user_tbl` WHERE username = '" + getUsername() + "'";
        ResultSet rs = db_con.executeSQLRS(sql);

        while(rs.next()){
            result = rs.getString("password");
            if(result.equals(getEncryptedPassword())){
                db_con.endConnection();
                return true;
            }
        }

        db_con.endConnection();
        return false;
    }

}
