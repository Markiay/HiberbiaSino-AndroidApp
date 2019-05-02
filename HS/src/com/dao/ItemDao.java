package com.dao;

import com.dto.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ItemDao {
    public int insertItem(Connection conn, Item item) throws SQLException;
    public ResultSet selectALL(Connection conn, String username) throws SQLException;
}
