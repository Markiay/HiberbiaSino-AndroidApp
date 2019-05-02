package com.dao;

import com.dto.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Employee {
    public ResultSet selectLoginName(Connection conn) throws SQLException;
    public ResultSet selectStatusInfo(Connection conn, String status) throws SQLException;
    public ResultSet selectSingleItem(Connection conn, int itemid) throws SQLException;
    public int updateStatus(Connection conn, String status, int ordernumber) throws SQLException;
}
