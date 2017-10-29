package viewModels;

import models.Role;
import models.User;

import java.util.List;

public class UserViewModel extends User {
    public UserViewModel(int id, String username, String password, String firstName, String middleName, String lastName) {
        super(id, username, password, firstName, middleName, lastName);
    }

    public UserViewModel(int id, String username, String password, String firstName, String middleName, String lastName,List<Role> roles) {
        super(id, username, password, firstName, middleName, lastName,roles);
    }
}
