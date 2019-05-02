package com.service;

import com.dao.Employee;
import com.dao.EmployeeImpl;
import com.dto.ItemBean;
import com.dto.OrderBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.util.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    Employee employeeJDBC = new EmployeeImpl();
    Gson gson = new GsonBuilder().disableHtmlEscaping().create(); ;
    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    public String selectAllName(){
        List<String> nameList = new ArrayList<String>();
        Connection conn = null;
        conn = ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet resultSet = employeeJDBC.selectLoginName(conn);
            while (resultSet.next()){
                String name = resultSet.getString("username");
                nameList.add(name);
            }
            String jsonString = gson.toJson(nameList);
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

    public String selectAllOrderInfoByStatus(String status){
        List<OrderBean> OrderBeanList2 = new ArrayList<OrderBean>();
        Connection conn = null;
        conn = ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet resultSet = employeeJDBC.selectStatusInfo(conn,status);
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
                OrderBeanList2.add(orderBean);
            }
            String jsonStr = gson.toJson(OrderBeanList2);
            return jsonStr;
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

    public String selectSingleItemInfo(int itemid){
        List<ItemBean> itemBeanList2 = new ArrayList<ItemBean>();
        Connection conn = null;
        conn = ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet resultSet = employeeJDBC.selectSingleItem(conn,itemid);
            while (resultSet.next()) {
                int itemid2 = resultSet.getInt("itemid");
                Blob blob = resultSet.getBlob("picture");
                float price = resultSet.getFloat("price");
                String itemname = resultSet.getString("itemname");
                String username = resultSet.getString("username");
                String description = resultSet.getString("description");
                String picture = null;
                try {
                    picture = blobToBase64(blob);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ItemBean itemBean = new ItemBean(itemid2, picture, price, itemname, username, description);
                itemBeanList2.add(itemBean);
            }
            String jsonString = gson.toJson(itemBeanList2);
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

    public boolean upadateOrderStatus(String status, int ordernumber){
        Connection conn = null;
        conn= ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            int count = employeeJDBC.updateStatus(conn, status, ordernumber);
            conn.commit();

            if (count==0){
                return false;
            }else{
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

    public static String blobToBase64(Blob blob) throws SQLException, IOException{
        String result = "";
        if(null != blob) {
            InputStream msgContent = blob.getBinaryStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[100];
            int n = 0;
            while (-1 != (n = msgContent.read(buffer))) {
                output.write(buffer, 0, n);
            }
            result = Base64.encode(output.toByteArray()) ;
            result = result.replace("\n", "").replace("\r", "");
            output.close();
            return result;
        }else {
            return null;
        }
    }
}
