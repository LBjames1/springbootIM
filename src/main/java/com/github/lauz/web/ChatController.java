package com.github.lauz.web;

import com.github.lauz.model.Message;
import com.github.lauz.model.User;
import com.github.lauz.service.LoginService;
import com.github.lauz.websocket.MyWebSocketHandler;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class ChatController {
    @Autowired
    private MyWebSocketHandler webSocketHandler;
    @Autowired
    private LoginService loginService;

    @RequestMapping("/onlineusers")
    @ResponseBody
    public Set<String> onlineusers(HttpSession session){
        Map<Integer, WebSocketSession> map=MyWebSocketHandler.userSocketSessionMap;
        Set<Integer> set=map.keySet();
        Iterator<Integer> it = set.iterator();
        Set<String> nameset=new HashSet<String>();
        while(it.hasNext()){
            Integer entry = it.next();
            String name= loginService.getUserNameById(entry);
            String user=(String)session.getAttribute("username");
            if(!user.equals(name)){
                nameset.add(name);
            }
        }
        return nameset;
    }

    // 发布系统广播（群发）
    @ResponseBody
    @RequestMapping(value = "broadcast", method = RequestMethod.POST)
    public void broadcast(@RequestParam("text") String text) throws IOException {
        Message msg = new Message();
        msg.setDate(new Date());
        msg.setFrom(-1);//-1表示系统广播
        msg.setFromName("系统广播");
        msg.setTo(0);
        msg.setText(text);
        webSocketHandler.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
    }

    @RequestMapping("getuid")
    @ResponseBody
    public User getuid(@RequestParam("username")String username){
        Integer uid= loginService.getUserIdByName(username);
        User u=new User();
        u.setUid(uid);
        return u;
    }
}
