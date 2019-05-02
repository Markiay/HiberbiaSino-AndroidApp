package com.dto;

import java.sql.Blob;
import java.sql.Date;

public class customerALL {
    private String username;
    private String gender;
    private String phonenumber;
    private String emailaddress;
    private Date birthdate;
    private Blob icon;

    public customerALL(String username, String gender, String phonenumber, String emailaddress, Date birthdate, Blob icon) {
        this.username = username;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.emailaddress = emailaddress;
        this.birthdate = birthdate;
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Blob getIcon() {
        return icon;
    }

    public void setIcon(Blob icon) {
        this.icon = icon;
    }
}

