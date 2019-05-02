package com.service;

import com.dao.UserDao;
import com.dao.UserDaoImpl;
import com.dto.User;
import com.util.BCrypt;
import com.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckUserService {

    UserDao userDao = new UserDaoImpl();

    public String checkLogin(User user){
        Connection conn = null;
        conn= ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet rs = userDao.select(conn, user.getUsername());
            while(rs.next()){
                String pass = rs.getString("password");
                String identity = rs.getString("uid");
                if (BCrypt.checkpw(user.getPassword(),pass)){
                    return "true "+identity;
                }else{
                    return "false "+identity;
                }
            }
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
