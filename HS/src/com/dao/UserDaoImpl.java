package com.dao;

import com.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    @Override
    public int insert(Connection conn, User user) throws SQLException {
        PreparedStatement preparedStatement2 = conn.prepareCall("INSERT INTO userinfo(username) VALUES (?)");
        preparedStatement2.setString(1, user.getUsername());
        preparedStatement2.executeUpdate();

        PreparedStatement preparedStatement = conn.prepareCall("INSERT INTO login(username, password, uid) VALUES (?, ? ,?)");
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, "customer");
        return preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Connection conn, User user) throws SQLException {

    }

    @Override
    public ResultSet select(Connection conn, String username) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT password, uid FROM  login WHERE username = ?");
        preparedStatement.setString(1, username);
        return preparedStatement.executeQuery();
    }
}
