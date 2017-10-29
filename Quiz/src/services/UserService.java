package services;

import dataAccess.DatabaseConnection;
import models.Role;
import models.User;

import javax.swing.plaf.nimbus.State;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class UserService {
    private DatabaseConnection db;
    public static User nullObject = null;

    public UserService()
    {
        db = new DatabaseConnection();
    }

    public User isUserValid(String username, String password)
    {
        User user = null;
        PreparedStatement ps = db.getPreparedStatement("select id from users where username = ? and password = ?");
        try {
            ps.setString(1, username);
            ps.setString(2, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                user = getUser(rs.getInt("id"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getUsers() {
        List<domain.User> rawUsers = new ArrayList<>();
        PreparedStatement ps = db.getPreparedStatement("select u.id as userId, u.username,u.password, u.firstName, u.middleName,u.lastName," +
                "r.id as roleId,r.Description as role from quiz.users u join userroles ur on ur.userId" +
                " = u.id join roles r on r.id = ur.roleId");

        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                rawUsers.add(new domain.User(rs.getInt("userId"),rs.getString("firstName"),rs.getString("middleName")
                        ,rs.getString("lastName"), rs.getString("username"), rs.getString("password"),rs.getInt("roleId"), rs.getString("role")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Map<Object,List<Role>> groupedUser = rawUsers.stream().collect(
                groupingBy(x-> Arrays.<Object>asList(x.getId(), x.getUsername(), x.getPassword(), x.getFirstName(), x.getMiddleName(), x.getLastName()),
                        Collectors.mapping((domain.User x) -> new Role(x.getRoleId(),x.getRole()),toList()))
        );

        List<User> users = new ArrayList<>();

        for (Map.Entry<Object,List<Role>> item: groupedUser.entrySet()) {
            List<Object> key = (List<Object>)item.getKey();

            System.out.println(key.get(0));
            users.add(new User((int)key.get(0),(String) key.get(1),(String) key.get(2),(String)key.get(3),(String) key.get(4),(String) key.get(5),item.getValue()));
        }

        return users;
    }

    public User getUser(int id) {
        List<domain.User> rawUsers = new ArrayList<>();
        PreparedStatement ps = db.getPreparedStatement("select u.id as userId, u.username,u.password, u.firstName, u.middleName,u.lastName," +
                "r.id as roleId,r.Description as role from quiz.users u join userroles ur on ur.userId" +
                " = u.id join roles r on r.id = ur.roleId where u.id = ?");

        try {
            ps.setInt(1, id);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                rawUsers.add(new domain.User(rs.getInt("userId"),rs.getString("firstName"),rs.getString("middleName")
                        ,rs.getString("lastName"), rs.getString("username"), rs.getString("password"),rs.getInt("roleId"), rs.getString("role")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Map<Object,List<Role>> groupedUser = rawUsers.stream().collect(
                groupingBy(x-> Arrays.<Object>asList(x.getId(), x.getUsername(), x.getPassword(), x.getFirstName(), x.getMiddleName(), x.getLastName()),
                        Collectors.mapping((domain.User x) -> new Role(x.getRoleId(),x.getRole()),toList()))
        );

        User user = null;

        for (Map.Entry<Object,List<Role>> item: groupedUser.entrySet()) {
            List<Object> key = (List<Object>)item.getKey();

            System.out.println(key.get(0));
            user = new User((int)key.get(0),(String) key.get(1),(String) key.get(2),(String)key.get(3),(String) key.get(4),(String) key.get(5),item.getValue());
        }

        return user;
    }

    public void addUser(User user) {
        String query = user.getId() > 0 ? "update quiz.users set firstName = ?, middleName = ?, lastName = ?, username = ?, password = ? where id = ?":
                "insert into quiz.users (firstName,middleName,lastName,username, password) values (?, ?, ?, ?, ?)";
        PreparedStatement ps = db.getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);
        try {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getMiddleName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getUsername());
            ps.setString(5, user.getPassword());
            if(user.getId() > 0)
            {
                ps.setInt(6, user.getId());
            }
            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0)
            {
                throw new SQLException("Creating User failed");
            }
            int newUserId;
            if(user.getId() > 0)
            {
                newUserId = user.getId();
            }
            else {
                ResultSet generatedkeys = ps.getGeneratedKeys();

                if (generatedkeys.next()) {
                    newUserId = generatedkeys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, not id obtained");
                }
            }

            addUserRoles(user.getRoles(),newUserId);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addUserRoles(List<Role> roles,int userId)
    {
        deleteUserRoles(userId);
        PreparedStatement ps = db.getPreparedStatement("insert into userroles values(?,?)");
        try {
            for (Role role : roles) {
                ps.setInt(1, userId);
                ps.setInt(2,role.getId());
                ps.addBatch();
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUserRoles(int userId)
    {
        PreparedStatement ps = db.getPreparedStatement("delete from userroles where userId = ?");
        try {
            ps.setInt(1, userId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        deleteUserRoles(id);
        PreparedStatement ps = db.getPreparedStatement("delete from quiz.users where id = ?");
        try {
            ps.setInt(1, id);
            ps.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
