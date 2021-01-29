package com.realtrac;

import lombok.Getter;

import javax.websocket.Session;

@Getter
public final class MySessionSingleton {

    public Session session;
    private static MySessionSingleton instance;

    private MySessionSingleton(Session session) {
        this.session = session;
    }

    public static MySessionSingleton getInstance(Session session) {
        if (instance == null) {
            instance = new MySessionSingleton(session);
        }
        return instance;
    }

}
