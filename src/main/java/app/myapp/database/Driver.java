package app.myapp.database;

import java.sql.*;

public class Driver {
    private Connection connection;
    private Statement stmt;
    private ResultSet rs;

    //Execute method with return data
    public ResultSet executeSQLRS(String sql) throws SQLException {
        stmt = connection.createStatement();
        rs = stmt.executeQuery(sql);
        return rs;
    }

    //Execute method without return data
    public void executeSQL(String sql) throws SQLException {
        stmt = connection.createStatement();
        stmt.execute(sql);
    }

    //Toggle: Database Connection
    public void startConnection() {
        try {
            String jdbcURL = "jdbc:mysql://localhost:3306/forum_db";
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
        System.out.println("Database Connection: OFF");
    }
}
