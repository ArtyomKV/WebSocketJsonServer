package com.realtrac.endpoints;

import com.realtrac.MySessionSingleton;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

//@Component
@Slf4j
@Getter
@Setter
@ServerEndpoint("/")
public class Endpoint {
    Session userSession = null;

    @OnOpen
    public void onOpen(Session userSession) {
        log.info("opening websocket");
        MySessionSingleton.getInstance(userSession);
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
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
