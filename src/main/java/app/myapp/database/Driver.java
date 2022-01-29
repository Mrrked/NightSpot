package app.myapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver {
    private Connection connection;

    public void startConnection() {
        try {
            String jdbcURL = "jdbc:mysql://localhost:3306/forum_engine_db";
            String username = "root";
            String password = "qswdefrgthyjukil";
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Database Connection: ON");
        } catch (SQLException e) {
            System.out.println("Database Connection: ERROR");
            e.printStackTrace();
        }
    }
    public void endConnection() throws SQLException {
        connection.close();
    }
}
