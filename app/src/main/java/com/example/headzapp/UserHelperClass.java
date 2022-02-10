package com.example.headzapp;

public class UserHelperClass {
    String fullName, email, phone,password, cPassword;

    public UserHelperClass() {

    }

    public UserHelperClass(String fullName, String email, String phone, String password,String cPassword) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.password = cPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCPassword() {
        return cPassword;
    }

    public void setCPassword(String Cpassword) {
        this.cPassword = cPassword;
    }
}
