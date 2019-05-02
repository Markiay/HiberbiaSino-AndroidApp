package com.service;

import com.dao.UserDao;
import com.dao.UserDaoImpl;
import com.dto.User;
import com.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class InsertInfoService {

    UserDao userDao = new UserDaoImpl();

    public boolean insertUserInfo(User user){
        Connection conn = null;
        conn= ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);

            int count = userDao.insert(conn, user);
            conn.commit();

            if (count==0){
                return false;
            }else {
                return true;
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
        return false;
    }


}
