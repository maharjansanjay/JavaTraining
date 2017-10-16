package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
    private String url = "jdbc:mysql://localhost:3306/quiz";
    private Connection connection = null;
    private String username = "root";
    private String password = "";

    public DatabaseConnection()
    {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//
//    public PreparedStatement getPreparedStatement(String query)
//    {
//
//    }

}
