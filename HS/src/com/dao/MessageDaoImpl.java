package com.dao;

import com.dto.ChatMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDaoImpl implements MessageDao{
    @Override
    public int insertMessage(Connection conn, ChatMessage chatMessage) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareCall("INSERT INTO message(text, messagedate, ordernumber, issend) VALUES (?, NOW(), ?, ?)");
        preparedStatement.setString(1, chatMessage.getText());
        preparedStatement.setInt(2, chatMessage.getOrdernumber());
        preparedStatement.setString(3, chatMessage.getIssend());
        return preparedStatement.executeUpdate();
    }

    @Override
    public ResultSet selectMessage(Connection conn, int ordernumber) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT text, messagedate, ordernumber, issend FROM  message WHERE ordernumber = ? order by messageid desc limit 3");
        preparedStatement.setInt(1, ordernumber);
        return preparedStatement.executeQuery();
    }
}
