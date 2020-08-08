package com.github.lauz.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VerifyImgController {
    @RequestMapping(value="/authImg")
    public String getVerifyImg(){
        return "authImg";
    }
}
