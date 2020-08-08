package com.github.lauz.websocket;

import com.github.lauz.model.Message;
import com.github.lauz.service.LoginService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ Description   :  Socket处理器
 * @ Author        :  lauz
 * @ CreateDate    :  2020/3/16 14:30
 */
@Component
public class MyWebSocketHandler implements WebSocketHandler {

    //用于保存HttpSession与WebSocketSession的映射关系
    public static final Map<Integer, WebSocketSession> userSocketSessionMap;

    @Autowired
    private LoginService loginservice;

    static {
        userSocketSessionMap = new ConcurrentHashMap<Integer, WebSocketSession>();
    }
    /**
     * @Description  ：
     * 	建立连接后,把登录用户的id写入WebSocketSession
     * @author       : lauz
     * @date         : 2020/3/16 14:32
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        Integer uid = (Integer) webSocketSession.getAttributes().get("uid");
        String username=loginservice.getUserNameById(uid);
        if (userSocketSessionMap.get(uid) == null) {
            userSocketSessionMap.put(uid, webSocketSession);
            Message msg = new Message();
            msg.setFrom(0);//0表示上线消息
            msg.setText(username);
            this.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
        }
    }
    /**
     * @Description  ：消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
     * @author       : lauz
     * @date         : 2020/3/16 15:20
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if(webSocketMessage.getPayloadLength()==0){
            return;
        }
        Message msg=new Gson().fromJson(webSocketMessage.getPayload().toString(),Message.class);
        msg.setDate(new Date());
        sendMessageToUser(msg.getTo(), new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        Iterator<Map.Entry<Integer, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
        // 移除当前抛出异常用户的Socket会话
        while (it.hasNext()) {
            Map.Entry<Integer, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(webSocketSession.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
                String username=loginservice.getUserNameById(entry.getKey());
                Message msg = new Message();
                msg.setFrom(-2);
                msg.setText(username);
                this.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
                break;
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("Websocket:" + webSocketSession.getId() + "已经关闭");
        Iterator<Map.Entry<Integer, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
        // 移除当前用户的Socket会话
        while (it.hasNext()) {
            Map.Entry<Integer, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(webSocketSession.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
                String username=loginservice.getUserNameById(entry.getKey());
                Message msg = new Message();
                msg.setFrom(-2);//下线消息，用-2表示
                msg.setText(username);
                this.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
                break;
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给所有在线用户发送消息
     * @param message
     * @throws IOException
     */
    public void broadcast(final TextMessage message) throws IOException {

        Iterator<Map.Entry<Integer, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();

        //多线程群发
        while (it.hasNext()) {

            final Map.Entry<Integer, WebSocketSession> entry = it.next();

            if (entry.getValue().isOpen()) {
                // entry.getValue().sendMessage(message);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (entry.getValue().isOpen()) {
                                entry.getValue().sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param uid
     * @param message
     * @throws IOException
     */
    public void sendMessageToUser(Integer uid, TextMessage message) throws IOException {
        WebSocketSession session = userSocketSessionMap.get(uid);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }
}
