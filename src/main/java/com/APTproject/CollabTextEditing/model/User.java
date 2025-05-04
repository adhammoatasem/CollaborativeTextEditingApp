package com.APTproject.CollabTextEditing.model;

import lombok.*;

import java.net.http.WebSocket;
import java.util.UUID;

@Getter
@Setter
public class User {
    private String userId = UUID.randomUUID().toString();;
    private String username;
    String WebSocketSessionID;

    public User(String username, String webSocketSessionID) {
        this.username = username;
        this.WebSocketSessionID = webSocketSessionID;
    }

    public String getUsername()
    { return username;
    }
}
