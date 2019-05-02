package com.service;

import com.dao.MessageDao;
import com.dao.MessageDaoImpl;
import com.dto.ChatMessage;
import com.google.gson.Gson;
import com.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    MessageDao messageDao = new MessageDaoImpl();
    Gson gson = new Gson();
    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public boolean sendChat(ChatMessage chatMessage){
        Connection connection = null;
        connection = ConnectionFactory.getInstance().makeConnection();
        try {
            connection.setAutoCommit(false);
            int count = messageDao.insertMessage(connection, chatMessage);
            connection.commit();

            if (count == 0){
                return false;
            }else{
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public String messageCollection(int orderno){
        List<ChatMessage> messageList = new ArrayList<ChatMessage>();
        Connection conn = null;
        conn = ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet resultSet = messageDao.selectMessage(conn, orderno);
            while (resultSet.next()){
                String text = resultSet.getString("text");
                Timestamp timestamp = new Timestamp(resultSet.getTimestamp("messagedate").getTime());
                String messagedate = sdfDateTime.format(timestamp);
                int ordernumber = resultSet.getInt("ordernumber");
                String issend = resultSet.getString("issend");
                ChatMessage chatMessage = new ChatMessage(text, messagedate, ordernumber, issend);
                messageList.add(chatMessage);
            }

            String jsonString = gson.toJson(messageList);
            return jsonString;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
