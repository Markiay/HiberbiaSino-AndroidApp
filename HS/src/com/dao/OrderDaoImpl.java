package com.dao;

import com.dto.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDaoImpl implements OrderDao{

    @Override
    public int insertOrder(Connection conn, Order order) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareCall("INSERT INTO ordertable(username, itemid, status, lostdate) VALUES (?, ?, ? ,?)");
        preparedStatement.setString(1, order.getUsername());
        preparedStatement.setInt(2, order.getItemid());
        preparedStatement.setString(3, order.getStatus());
        preparedStatement.setDate(4, order.getLostdate());
        return preparedStatement.executeUpdate();
    }

    @Override
    public ResultSet selectOrder(Connection conn, String username) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM ordertable WHERE username = ?");
        preparedStatement.setString(1, username);
        return preparedStatement.executeQuery();
    }

    @Override
    public ResultSet selectStatus(Connection conn, int itemid) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT status FROM ordertable WHERE itemid = ?");
        preparedStatement.setInt(1, itemid);
        return preparedStatement.executeQuery();
    }
}
