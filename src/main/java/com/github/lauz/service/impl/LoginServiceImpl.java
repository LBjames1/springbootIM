package com.github.lauz.service.impl;

import com.github.lauz.mapper.LoginMapper;
import com.github.lauz.model.User;
import com.github.lauz.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private LoginMapper loginMapper;


    @Override
    public String getUserPsdByName(String username) {
        User user = loginMapper.getUserByName(username);
        if(user!=null){
            return user.getPassword();
        }
        return null;
    }

    @Override
    public Integer getUserIdByName(String username) {
        User user = loginMapper.getUserByName(username);
        if(user!=null){
            return user.getUid();
        }
        return null;
    }

    @Override
    public String getUserNameById(int uid) {
        User user = loginMapper.getUserById(uid);
        if(user!=null){
            return user.getUsername();
        }
        return null;
    }
}
