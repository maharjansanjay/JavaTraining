package services;

import dataAccess.DatabaseConnection;
import domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private DatabaseConnection db;

    public UserService()
    {
        db = new DatabaseConnection();
    }

    public User IsUserValid(String username, String password)
    {
        User user = null;

        PreparedStatement ps = db.getPreparedStatement("select * from quiz.users where username = ? and password = ?");
        try {
            ps.setString(1, username);
            ps.setString(2, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                int id = rs.getInt("id");
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<User> GetUsers() {
        List<User> lu = new ArrayList<User>();

        PreparedStatement ps = db.getPreparedStatement("select * from quiz.users");
        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int id = rs.getInt("id");
                lu.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lu;
    }
}
