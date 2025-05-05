package com.foodorder.model;

import java.io.Serializable;

public class Admin implements Serializable {
    private static final long serialVersionUID=1L;

    private String adminId;
    private String username;
    private String password;

    public Admin(String adminId,String username,String password){
        this.adminId=adminId;
        this.username=username;
        this.password=password;
    }

    public String getAdminId(){
        return adminId;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    //setters

    public void setAdminId(String adminId){
        this.adminId=adminId;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public boolean authenticate(String inputUsername,String inputPassword){
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    @Override
    public String toString(){
        return "Admin{" + "adminId='" + adminId+ '\'' + ", username='" + username+ '\'' + '}';
    }
}
