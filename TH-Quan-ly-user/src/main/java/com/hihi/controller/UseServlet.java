package com.hihi.controller;

import com.hihi.dao.UserDAO;
import com.hihi.model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UseServlet", urlPatterns = "/users")
public class UseServlet extends HttpServlet {
    private static final long serialVersionUID=1L;
    private UserDAO userDAO;

    public void init(){
        userDAO = new UserDAO();
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action=request.getParameter("action");
        if(action==null){
            action = "" ;
        }
        switch (action){

            case "delete":
                deleteUser(request,response);
                break;
            case "create":
                showNewForm(request,response);
                break;
            case "edit":
                updateUser(request,response);
                break;
            default:
                try {
                    listUser(request,response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if(action==null){
            action="";
        }
        switch (action){
            case "create":
                try {
                    createUser(request,response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "edit":
                updateUser(request,response);
                break;
        }
    }

    private void listUser(HttpServletRequest  request, HttpServletResponse  response) throws SQLException, IOException, ServletException {
        List<User> users  = userDAO.selectAllUser();
        request.setAttribute("listUser",users);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request,response);

    }

    private void updateUser(HttpServletRequest request,HttpServletResponse response){
        int id= Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User user = new User(id,name,email,country);
        try {
            userDAO.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        try {
            dispatcher.forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void  deleteUser(HttpServletRequest request, HttpServletResponse response) {

        int id = Integer.parseInt(request.getParameter("id"));
        try {
            userDAO.deleteUser(id);
            List<User> userList = userDAO.selectAllUser();
            request.setAttribute("listUser",userList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
            dispatcher.forward(request,response);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void  createUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User user = new User(name,email,country);
        try {
            userDAO.insertUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("listUser", userDAO.selectAllUser());
        RequestDispatcher dispatcher=request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request,response);
    }


    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }


}
