package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeImpl implements Employee{
    @Override
    public ResultSet selectLoginName(Connection conn) throws SQLException {
//        PreparedStatement preparedStatement = conn.prepareStatement("SELECT username FROM login WHERE username = ?");
//        preparedStatement.setString(1, username);
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT username FROM login where uid = ?");
        preparedStatement.setString(1, "customer");
        return preparedStatement.executeQuery();
    }

    @Override
    public ResultSet selectStatusInfo(Connection conn, String status) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM  ordertable WHERE status = ?");
        preparedStatement.setString(1, status);
        return preparedStatement.executeQuery();
    }

    @Override
    public ResultSet selectSingleItem(Connection conn, int itemid) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM  item WHERE itemid = ?");
        preparedStatement.setInt(1, itemid);
        return preparedStatement.executeQuery();
    }

    @Override
    public int updateStatus(Connection conn, String status, int ordernumber) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareCall("update ordertable set status = ? where ordernumber = ?");
        preparedStatement.setString(1, status);
        preparedStatement.setInt(2, ordernumber);
        return preparedStatement.executeUpdate();
    }
}
