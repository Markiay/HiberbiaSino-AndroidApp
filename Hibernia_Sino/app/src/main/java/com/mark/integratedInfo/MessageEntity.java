package com.mark.integratedInfo;

public class MessageEntity{

    private String message;//消息的文字内容
    private boolean isMine;//是否为自己发出
    private int msgType;//消息类型
    private String imagePath;//图片消息中图片的路径

    public MessageEntity(String message,String imagePath,int msgType, boolean isMine) {
        this.message = message;
        this.imagePath=imagePath;
        this.msgType=msgType;
        this.isMine = isMine;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "message='" + message + '\'' +
                ", isMine=" + isMine +
                ", msgType=" + msgType +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
