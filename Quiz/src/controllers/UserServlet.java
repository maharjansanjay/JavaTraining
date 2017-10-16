package controllers;

import services.UserService;

import javax.servlet.RequestDispatcher;
import java.io.IOException;

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
        System.out.println(username + password);

        if(userService.IsUserValid(username, password)) {
            RequestDispatcher rd = request.getRequestDispatcher("Users/dashboard.jsp");
            rd.forward(request, response);
        }
        else
        {
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("this is user get method");
    }
}
