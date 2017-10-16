package services;

import dataAccess.DatabaseConnection;

public class UserService {
    private DatabaseConnection db;

    public UserService()
    {
        db = new DatabaseConnection();
    }

    public boolean IsUserValid(String username, String password)
    {
        return true;
    }
}
