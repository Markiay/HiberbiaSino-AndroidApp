package com.mark.integratedInfo;

import java.io.Serializable;

public class UserInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private String account;

    public UserInfo(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

//    @Override
//    public String toString() {
//        return "UserInfo{" +
//                "nickname='" + nickname + '\'' +
//                ", account='" + account + '\'' +
//                '}';
//    }

}
