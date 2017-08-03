/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitsenior.g40.weewhorescuer.models;

import android.support.annotation.NonNull;

/**
 *
 * @author PNattawut
 */
public class Profile {
    private static Profile profile;
    private long userId;
    private String firstName;
    private String lastName;
    private long personalId;
    private String phoneNumber;
    private String address1;
    private String address2;
    private int age;
    private char gender;

    public static Profile getInsatance(){
        if(profile == null){
            profile = new Profile();
        }
        return profile;
    }


    public static void set(@NonNull Profile profile){
        Profile.profile = profile;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(long personalId) {
        this.personalId = personalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Profile{" + "userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", personalId=" + personalId + ", phoneNumber=" + phoneNumber + ", address1=" + address1 + ", address2=" + address2 + ", age=" + age + ", gender=" + gender + '}';
    }

    
    
}
