/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitsenior.g40.weewhorescuer.models;

/**
 *
 * @author PNattawut
 */
public class User {

    private static User user;
    private long userId;
    private String username;
    private String password;
    //User Type are specify by 
    //'T' is a Rescuer/Monitoring and 
    //'M' is a General User.
    private char userType;

    public User(String username, String password, char userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public User() {
    }

    public static User getInsatance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getUserType() {
        return userType;
    }

    public void setUserType(char userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", username=" + username + ", password=" + password + ", userType=" + userType + '}';
    }

}
