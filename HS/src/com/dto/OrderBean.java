package com.dto;

public class OrderBean {
    private int ordernumber;
    private String orderdate;
    private String username;
    private int itemid;
    private String status;
    private String lostdate;

    public OrderBean(int ordernumber, String orderdata, String username, int itemid, String status, String lostdate) {
        this.ordernumber = ordernumber;
        this.orderdate = orderdata;
        this.username = username;
        this.itemid = itemid;
        this.status = status;
        this.lostdate = lostdate;
    }

    public int getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
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

    public String getLostdate() {
        return lostdate;
    }

    public void setLostdate(String lostdate) {
        this.lostdate = lostdate;
    }
}
