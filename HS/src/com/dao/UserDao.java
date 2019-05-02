package com.dao;

import com.dto.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserDao {
    public int insert(Connection conn, User user) throws SQLException;

//    public void update(Connection conn) throws SQLException;

    public void delete(Connection conn, User user) throws SQLException;

    public ResultSet select(Connection conn, String username) throws SQLException;
}
