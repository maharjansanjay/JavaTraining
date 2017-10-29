package services;

import dataAccess.DatabaseConnection;
import models.QuestionCategory;
import models.Role;
import models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LookUpItemService {

    private DatabaseConnection db;

    public LookUpItemService()
    {
        db = new DatabaseConnection();
    }

    public List<Role> getRoles()
    {
        List<Role> roles = new ArrayList<>();
        PreparedStatement ps = db.getPreparedStatement("select * from roles");

        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                roles.add(new Role(rs.getInt("id"),rs.getString("description")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public List<QuestionCategory> getQuestionCategories() {
        List<QuestionCategory> questionCategories = new ArrayList<>();
        PreparedStatement ps = db.getPreparedStatement("select * from questionCategories");

        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                questionCategories.add(new QuestionCategory(rs.getInt("id"), rs.getString("Description")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return questionCategories;
    }
}
