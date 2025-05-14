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
        if (adminId == null || adminId.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin ID cannot be null or empty.");
        }

        this.adminId=adminId;
    }

    public void setUsername(String username){
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        this.username=username;
    }

    public void setPassword(String password){
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        this.password=password;
    }

    public boolean authenticate(String inputUsername,String inputPassword){
        if (inputUsername == null || inputPassword == null) {
            throw new IllegalArgumentException("Input username and password must not be null.");
        }
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }
    public boolean safeAuthenticate(String inputUsername, String inputPassword) {
        try {
            return authenticate(inputUsername, inputPassword);
        } catch (IllegalArgumentException e) {
            // Handle the exception by logging the error and returning false.
            System.err.println("Authentication error: " + e.getMessage());
            return false;
        }
    }


    @Override
    public String toString(){
        return "Admin{" + "adminId='" + adminId+ '\'' + ", username='" + username+ '\'' + '}';
    }
}
