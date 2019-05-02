package com.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
//    声明四个成员变量
    private static String driver;
    private static String dburl;
    private static String user;
    private static String password;
//    构造一个本类变量
    private static final ConnectionFactory factory = new ConnectionFactory();
//    connection变量用于保存数据库连接
    private Connection conn;
//  静态代码块
    static {
        Properties properties = new Properties();
        InputStream is = ConnectionFactory.class.getClassLoader().getResourceAsStream("dbconfig.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            System.out.println("======配置文件读取错误======");
            e.printStackTrace();
        }

        driver = properties.getProperty("driver");
        dburl = properties.getProperty("dburl");
        user = properties.getProperty("user");
        password = properties.getProperty("password");

    }

    private ConnectionFactory(){

    }

    public static ConnectionFactory getInstance(){
        return factory;
    }
//  数据库连接的方法
    public Connection makeConnection(){
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(dburl, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
