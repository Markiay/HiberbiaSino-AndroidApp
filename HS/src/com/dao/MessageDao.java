package com.dao;

import com.dto.ChatMessage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface MessageDao {
    public int insertMessage(Connection conn, ChatMessage chatMessage) throws SQLException;
    public ResultSet selectMessage(Connection conn, int ordernumber) throws SQLException;
}
