package com.service;

import com.dao.ItemDao;
import com.dao.ItemDaoImpl;
import com.dao.OrderDao;
import com.dao.OrderDaoImpl;
import com.dto.Item;
import com.dto.ItemBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.util.ConnectionFactory;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemService {
    ItemDao itemDao = new ItemDaoImpl();
    Gson gson = new GsonBuilder().disableHtmlEscaping().create(); ;
    OrderDao orderDao = new OrderDaoImpl();

    public boolean insertItemInfo(Item item){
        Connection conn = null;
        conn= ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            int count = itemDao.insertItem(conn, item);
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

    public String acquireForm(String userName) {
        List<ItemBean> itemBeanList = new ArrayList<ItemBean>();
        Connection conn = null;
        conn = ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet resultSet = itemDao.selectALL(conn, userName);
            while (resultSet.next()) {
                int itemid = resultSet.getInt("itemid");
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
                ItemBean itemBean = new ItemBean(itemid, picture, price, itemname, username, description);
                itemBeanList.add(itemBean);
            }
            String jsonString = gson.toJson(itemBeanList);
            return jsonString;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
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
                result =Base64.encode(output.toByteArray()) ;
                result = result.replace("\n", "").replace("\r", "");
                output.close();
            return result;
        }else {
            return null;
        }
    }

}
