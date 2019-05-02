package com.dao;

import com.dto.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface OrderDao {
    public int insertOrder(Connection conn, Order order) throws SQLException;
    public ResultSet selectOrder(Connection conn, String username) throws SQLException;
    public ResultSet selectStatus(Connection conn, int itemid) throws SQLException;
}
