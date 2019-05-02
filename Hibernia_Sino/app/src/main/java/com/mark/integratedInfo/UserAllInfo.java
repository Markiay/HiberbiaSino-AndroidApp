package com.mark.integratedInfo;

public class UserAllInfo {
    private String username;
    private String gender;
    private String phonenumber;
    private String emailaddress;
    private String birthdate;
    private String icon;

    public UserAllInfo(String username, String gender, String phonenumber, String emailaddress, String birthdate, String icon) {
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
