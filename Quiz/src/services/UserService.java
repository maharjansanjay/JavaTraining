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
    public static User nullObject = null;

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

    public User GetUser(int id) {
        User user = null;
        PreparedStatement ps = db.getPreparedStatement("select * from quiz.users where id = ?");

        try {
            ps.setInt(1, id);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public void AddUser(int id, String username, String password, String role) {
        PreparedStatement ps;
        String query = null;
        if (id > 0) {
            query = "update quiz.users set username = ?,"
                            + (password.isEmpty() ? "" : "password = ?, ")
                            + "role = ? where id = ?";
            ps = db.getPreparedStatement(query);
            try {
                ps.setString(1, username);
                if (!password.isEmpty()) {
                    ps.setString(2, password);
                    ps.setString(3, role);
                    ps.setInt(4, id);
                }
                else {
                    ps.setString(2, role);
                    ps.setInt(3, id);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            query = "insert into quiz.users (username, password, role) values (?, ?, ?)";
            ps = db.getPreparedStatement(query);

            try {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DeleteUser(int id) {
        PreparedStatement ps = db.getPreparedStatement("delete from quiz.users where id = ? limit 1");
        try {
            ps.setInt(1, id);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
