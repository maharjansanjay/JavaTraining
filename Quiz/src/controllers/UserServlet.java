package controllers;

import models.Role;
import models.User;
import services.LookUpItemService;
import services.UserService;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserServlet extends javax.servlet.http.HttpServlet {

    UserService userService;
    private LookUpItemService _list;

    public UserServlet()
    {
        userService = new UserService();
        _list = new LookUpItemService();
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String page = request.getParameter("page");

        switch (page)
        {
            case "Login": {
                String username = request.getParameter("username");
                String password = request.getParameter("password");


                User user = userService.isUserValid(username, password);
                if (user != null) {
                    RequestDispatcher rd = request.getRequestDispatcher("Users/dashboard.jsp");
                    rd.forward(request, response);
                } else {
                    RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                    rd.forward(request, response);
                }
                break;
            }
            case "UserList":
                {
                    List<User> users = userService.getUsers();
                    RequestDispatcher rd = request.getRequestDispatcher("Users/UserList.jsp");
                    request.setAttribute("users", users);
                    rd.forward(request, response);
                }
                break;
            case "EditUser":
                {
                    int id;
                    try {
                        if (request.getParameter("id") != null){
                            id = Integer.parseInt(request.getParameter("id"));
                            User userEdit = userService.getUser(id);
                            request.setAttribute("id", userEdit.getId());
                            request.setAttribute("username", userEdit.getUsername());
                            request.setAttribute("role", userEdit.getRoles());
                        }

                    } catch (NumberFormatException e) {
                        id = 0;
                    }
                }
            case "AddUser":
                {
                    List<Role> roles = _list.getRoles();
                    RequestDispatcher rd = request.getRequestDispatcher("Users/AddUser.jsp");
                    request.setAttribute("roleLookUp",roles);
                    rd.forward(request, response);
                }
                break;
            case "AddUserPost":
                {
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    String firstName = request.getParameter("firstName");
                    String middleName = request.getParameter("middleName");
                    String lastName = request.getParameter("lastName");

                    String[] roles = request.getParameterValues("role");

                    String passwordConfirm = request.getParameter("password-confirm");

                    int id;
                    try {
                        if (request.getParameter("id") != null){
                            id = Integer.parseInt(request.getParameter("id"));
                        }
                        else {
                            id = 0;
                        }
                    } catch (NumberFormatException e) {
                        id = 0;
                    }

                    if (password.equals(passwordConfirm)) {
                        List<Role> userRoles = new ArrayList<Role>();
                        for (String role: roles) {
                            userRoles.add(new Role(Integer.parseInt(role)));
                        }
                        userService.addUser(new User(id, username, password, firstName,middleName,lastName, userRoles));
                        response.sendRedirect("/users?page=UserList");
                    }
                    else {
                        String error = "Passwords do not match.";

                        RequestDispatcher rd = request.getRequestDispatcher("Users/AddUser.jsp");
                        request.setAttribute("error", error);
                        request.setAttribute("username", username);
                        request.setAttribute("role", roles);
                        rd.forward(request, response);
                    }
                }
                break;
            case "DeleteUser":
                {
                    int id;
                    try {
                        if (request.getParameter("id") != null){
                            id = Integer.parseInt(request.getParameter("id"));
                            userService.deleteUser(id);
                        }

                    } catch (NumberFormatException e) {
                        id = 0;
                    }
                    response.sendRedirect("/users?page=UserList");
                }
                break;
        }


    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doPost(request,response);
    }
}
