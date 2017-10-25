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
                List<User> users = userService.GetUsers();
                RequestDispatcher rd = request.getRequestDispatcher("Users/UserList.jsp");
                request.setAttribute("users",users);
                rd.forward(request, response);
        }


    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doPost(request,response);
    }
}
