package com.github.lauz.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @ Description   :  WebScoket配置处理器
 * @ Author        :  lauz
 * @ CreateDate    :  2020/3/16 15:05
 */
@Component
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private MyWebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webSocketHandler, "/ws").addInterceptors(new HandShake());

        webSocketHandlerRegistry.addHandler(webSocketHandler, "/ws/sockjs").addInterceptors(new HandShake()).withSockJS();
    }
}
