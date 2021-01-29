package com.realtrac.endpoints;

import com.realtrac.service.WebSockServer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Slf4j
@Setter
@ServerEndpoint("/")
public class Endpoint {
    Session userSession = null;

    @OnOpen
    public void onOpen(Session userSession) {
        log.info("opening websocket");
        WebSockServer.session = userSession;
        this.userSession = userSession;
    }

    @OnClose
    public void onClose() {
        log.info("closing websocket");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (userSession != null) {
            log.info("Отправляю на фронт....");
            try {
                userSession.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.info("Пока нет сессии...");
        }
    }

}
