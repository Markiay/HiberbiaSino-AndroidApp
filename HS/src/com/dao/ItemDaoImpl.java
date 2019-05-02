package com.dao;

import com.dto.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDaoImpl implements ItemDao{

    @Override
    public int insertItem(Connection conn, Item item) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareCall("INSERT INTO item(picture, price, itemname, username, description) VALUES (?, ?, ?, ? ,?)");
//        preparedStatement.setInt(1, item.getItemid());
        preparedStatement.setBlob(1, item.getPicture());
        preparedStatement.setFloat(2, item.getPrice());
        preparedStatement.setString(3, item.getItemname());
        preparedStatement.setString(4, item.getUsername());
        preparedStatement.setString(5, item.getDescription());
        return preparedStatement.executeUpdate();
    }

    @Override
    public ResultSet selectALL(Connection conn, String username) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM  item WHERE username = ?");
        preparedStatement.setString(1, username);
        return preparedStatement.executeQuery();
    }
}
