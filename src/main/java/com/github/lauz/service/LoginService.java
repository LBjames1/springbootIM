package com.github.lauz.service;


public interface LoginService {
    String getUserPsdByName(String username);
    Integer getUserIdByName(String username);
    String getUserNameById(int uid);
}
