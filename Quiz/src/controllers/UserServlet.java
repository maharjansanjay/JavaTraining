package controllers;

import domain.User;
import services.UserService;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;

public class UserServlet extends javax.servlet.http.HttpServlet {

    UserService userService;

    public UserServlet()
    {
        userService = new UserService();
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("this is user post method");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String role = request.getParameter("role");
        String page = request.getParameter("page");

        switch (page)
        {
            case "Login":
                User user = userService.IsUserValid(username, password);
                if(user != null) {
                    RequestDispatcher rd = request.getRequestDispatcher("Users/dashboard.jsp");
                    rd.forward(request, response);
                }
                else
                {
                    RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                    rd.forward(request, response);
                }
                break;
            case "UserList":
                {
                    List<User> users = userService.GetUsers();
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
                            User userEdit = userService.GetUser(id);
                            request.setAttribute("id", userEdit.getId());
                            request.setAttribute("username", userEdit.getUsername());
                            request.setAttribute("role", userEdit.getRole());
                        }

                    } catch (NumberFormatException e) {
                        id = 0;
                    }
                }
            case "AddUser":
                {
                    RequestDispatcher rd = request.getRequestDispatcher("Users/AddUser.jsp");
                    rd.forward(request, response);
                }
                break;
            case "AddUserPost":
                {
                    String passwordConfirm = request.getParameter("password-confirm");

                    int id = -1;
                    try {
                        if (request.getParameter("id") != null){
                            id = Integer.parseInt(request.getParameter("id"));
                        }
                    } catch (NumberFormatException e) {
                        id = 0;
                    }

                    if (password.equals(passwordConfirm)) {
                        userService.AddUser(id, username, password, role);
                        response.sendRedirect("/users?page=UserList");
                    }
                    else {
                        String error = "Passwords do not match.";

                        RequestDispatcher rd = request.getRequestDispatcher("Users/AddUser.jsp");
                        request.setAttribute("error", error);
                        request.setAttribute("username", username);
                        request.setAttribute("role", role);
                        rd.forward(request, response);
                    }
                }
                break;
            case "Delete":
                {
                    int id;
                    try {
                        if (request.getParameter("id") != null){
                            id = Integer.parseInt(request.getParameter("id"));
                            userService.DeleteUser(id);
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
