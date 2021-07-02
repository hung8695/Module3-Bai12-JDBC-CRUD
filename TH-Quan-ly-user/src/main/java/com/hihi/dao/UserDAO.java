package com.hihi.dao;

import com.hihi.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO  implements IUserDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/hihi?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "123456";

    private static final String INSERT_USERS_SQL= "INSERT INTO users"+"(name,email,country)VALUES"+"(?,?,?)";
    private static final String SELECT_USER_BY_ID="SELECT id,name,email,country from users  where  id=?;";
    private static final String SELECT_ALL_USER = "SELECT * FROM users";
    private static final String UPDATE_USER_SQL = "UPDATE users SET name=?,email=?,country=? WHERE id=?;";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id=?;";

    public UserDAO() {}

    protected Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }


    @Override
    public void insertUser(User user) throws SQLException {
        Connection connection=getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(INSERT_USERS_SQL);
        preparedStatement.setString(1,user.getName());
        preparedStatement.setString(2,user.getEmail());
        preparedStatement.setString(3,user.getCountry());
        preparedStatement.executeUpdate();
    }

    @Override
    public User selectUser(int id) throws SQLException{
        User user=null;
        Connection connection=getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(SELECT_USER_BY_ID);
        preparedStatement.setInt(1,id);
        ResultSet resultSet=preparedStatement.executeQuery();
        while (resultSet.next()){
            String name=resultSet.getString("name");
            String email=resultSet.getString("email");
            String country=resultSet.getString("country");
            user=new User(id,name,email,country);
        }

        return user;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {

        boolean rowDelete;

        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL);
        preparedStatement.setInt(1,id);
        rowDelete= preparedStatement.executeUpdate()>0;

        return rowDelete;
    }

    @Override
    public List<User> selectAllUser() throws SQLException{
        List<User> users= new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(SELECT_ALL_USER);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String country = resultSet.getString("country");
            users.add(new User(id,name,email,country));
        }
        return users;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdate;

        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL);
        preparedStatement.setString(1,user.getName());
        preparedStatement.setString(2,user.getEmail());
        preparedStatement.setString(3,user.getCountry());
        preparedStatement.setInt(4,user.getId());
        rowUpdate=preparedStatement.executeUpdate()>0;

        return rowUpdate;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }



}



