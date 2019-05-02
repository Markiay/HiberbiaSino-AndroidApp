package com.mark.integratedInfo;

public class ChatMessage {
    private String text;
    private String messagedate;
    private int ordernumber;
    private String issend;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessagedate() {
        return messagedate;
    }

    public void setMessagedate(String messagedate) {
        this.messagedate = messagedate;
    }

    public int getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getIssend() {
        return issend;
    }

    public void setIssend(String issend) {
        this.issend = issend;
    }

    public ChatMessage(String text, String messagedate, int ordernumber, String issend) {
        this.text = text;
        this.messagedate = messagedate;
        this.ordernumber = ordernumber;
        this.issend = issend;
    }
}
