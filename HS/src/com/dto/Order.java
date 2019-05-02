package com.dto;

import java.sql.Date;

public class Order {
    private int ordernumber;
    private String orderdate;
    private String username;
    private int itemid;
    private String status;
    private Date lostdate;

    public Order(int ordernumber, String orderdata, String username, int itemid, String status, Date lostdate) {
        this.ordernumber = ordernumber;
        this.orderdate = orderdata;
        this.username = username;
        this.itemid = itemid;
        this.status = status;
        this.lostdate = lostdate;
    }

    public String getOrderdata() {
        return orderdate;
    }

    public void setOrderdata(String orderdata) {
        this.orderdate = orderdata;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLostdate() {
        return lostdate;
    }

    public void setLostdate(Date lostdate) {
        this.lostdate = lostdate;
    }
}
