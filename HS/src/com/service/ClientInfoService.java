package com.service;

import com.dao.ClientInfoDao;
import com.dao.ClientInfoDaoImpl;
import com.dto.customerALL;
import com.dto.customerALLBean;
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

public class ClientInfoService {
    ClientInfoDao clientInfoDao = new ClientInfoDaoImpl();
    Gson gson = new GsonBuilder().disableHtmlEscaping().create(); ;
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    public boolean updateCI(customerALL customerALL){
        Connection conn = null;
        conn= ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            int count = clientInfoDao.updateClientInfo(conn, customerALL);
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

    public String getCI(String name){
        List<customerALLBean> customerALLBeanList = new ArrayList<customerALLBean>();
        Connection conn = null;
        conn = ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet resultSet = clientInfoDao.getClientInfo(conn, name);
            while(resultSet.next()){
                String username = resultSet.getString("username");
                String gender = resultSet.getString("gender");
                if (gender == null){
                    gender = "";
                }else{
                    if (gender.equals("M")) gender = "Male";
                    else if (gender.equals("F")) gender = "Female";
                }

                String phonenumber = resultSet.getString("phonenumber");
                if (phonenumber == null){
                    phonenumber = "";
                }

                String emailaddress = resultSet.getString("emailaddress");
                if (emailaddress == null){
                    emailaddress = "";
                }

                Date bd = resultSet.getDate("birthdate");
                String birthdate = "";
                if (bd == null){
                    birthdate = "";
                }else {
                    birthdate = sdfDate.format(bd);
                }

                Blob iconb = resultSet.getBlob("icon");
                String icon = null;
                if (iconb == null){
                    icon = "";
                }else{
                    icon = blobToBase64(iconb);
                }
                customerALLBean customerALLBean = new customerALLBean(username, gender, phonenumber, emailaddress, birthdate, icon);
                customerALLBeanList.add(customerALLBean);
            }
            String jsonStr = gson.toJson(customerALLBeanList);
            return jsonStr;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String blobToBase64(Blob blob) throws SQLException, IOException {
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
