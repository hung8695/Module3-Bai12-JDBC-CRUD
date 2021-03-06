package com.hihi.dao;

import com.hihi.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {

    public void insertUser(User user) throws SQLException;

    public User selectUser(int id) throws SQLException;

    public boolean deleteUser(int id) throws SQLException;

    public List<User> selectAllUser() throws SQLException;

    public boolean updateUser(User user) throws SQLException;
}
