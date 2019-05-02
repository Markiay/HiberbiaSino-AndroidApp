package com.dao;

import com.dto.customerALL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientInfoDaoImpl implements ClientInfoDao{
    @Override
    public int updateClientInfo(Connection conn, customerALL customerALL) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareCall("update userinfo set gender = ?, phonenumber = ?, emailaddress = ?, birthdate = ?, icon = ? where username = ?");
        preparedStatement.setString(1, customerALL.getGender());
        preparedStatement.setString(2, customerALL.getPhonenumber());
        preparedStatement.setString(3, customerALL.getEmailaddress());
        preparedStatement.setDate(4, customerALL.getBirthdate());
        preparedStatement.setBlob(5, customerALL.getIcon());
        preparedStatement.setString(6, customerALL.getUsername());
        return preparedStatement.executeUpdate();
    }

    @Override
    public ResultSet getClientInfo(Connection conn, String username) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM  userinfo WHERE username = ?");
        preparedStatement.setString(1, username);
        return preparedStatement.executeQuery();
    }
}
