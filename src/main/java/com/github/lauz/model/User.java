package com.github.lauz.model;
/**
 * @ Description   :  用户类
 * @ Author        :  lauz
 * @ CreateDate    :  2020/3/13 17:45
 */

public class User {
    private Integer uid;
    private String username;
    private String password;
    private String email;
    private String pet_name;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }
}
