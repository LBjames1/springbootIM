package com.github.lauz.web;

import com.github.lauz.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping("/login")
    public String index(){
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.removeAttribute("username");
        httpSession.removeAttribute("uid");
        return "login";
    }

    @RequestMapping("/loginvalidate")
    public String loginvalidate(@RequestParam("username") String username, @RequestParam("pic") String pic, @RequestParam("password") String pwd, HttpSession httpSession){
        //获取验证码
        String picode=(String) httpSession.getAttribute("rand");
        if(!picode.equalsIgnoreCase(pic)){
            return "failcode";
        }
        if(username==null){
            return "login";
        }
        String realpwd= loginService.getUserPsdByName(username);
        if(realpwd!=null&&pwd.equals(realpwd))
        {
            int uid= loginService.getUserIdByName(username);
            httpSession.setAttribute("username", username);
            httpSession.setAttribute("uid", uid);
            return "chatroom";
        }else{
            return "fail";
        }
    }

}
