package com.service;

import com.dao.OrderDao;
import com.dao.OrderDaoImpl;
import com.dto.Order;
import com.dto.OrderBean;
import com.google.gson.Gson;
import com.util.ConnectionFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    OrderDao orderDao = new OrderDaoImpl();
    Gson gson = new Gson();
    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    public boolean insertOrderInfo(Order order){
        Connection connection = null;
        connection = ConnectionFactory.getInstance().makeConnection();
        try {
            connection.setAutoCommit(false);
            int count = orderDao.insertOrder(connection, order);
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

    public String requireOrderList(String clientName){
        List<OrderBean> OrderBeanList = new ArrayList<OrderBean>();
        Connection conn = null;
        conn = ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet resultSet = orderDao.selectOrder(conn, clientName);
            while(resultSet.next()){
                int orderno = resultSet.getInt("ordernumber");
                Timestamp timestamp = new Timestamp(resultSet.getTimestamp("orderdate").getTime());
                String odString = sdfDateTime.format(timestamp);
                String username = resultSet.getString("username");
                int itemid = resultSet.getInt("itemid");
                String state = resultSet.getString("status");
                Date date = resultSet.getDate("lostdate");
                String dateString = sdfDate.format(date);
                OrderBean orderBean = new OrderBean(orderno, odString, username, itemid, state, dateString);
                OrderBeanList.add(orderBean);
            }
            String jsonString = gson.toJson(OrderBeanList);
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
