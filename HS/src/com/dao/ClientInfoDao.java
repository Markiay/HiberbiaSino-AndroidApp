package com.dao;

import com.dto.customerALL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ClientInfoDao {
    public int updateClientInfo(Connection conn, customerALL customerALL) throws SQLException;
    public ResultSet getClientInfo(Connection conn, String username) throws SQLException;
}
