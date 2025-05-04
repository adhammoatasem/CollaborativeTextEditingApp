package com.APTproject.CollabTextEditing.model;

import lombok.*;

import java.net.http.WebSocket;
import java.util.UUID;

@Getter
@Setter
public class User {
    private String username;
    private UserRole role;
    String WebSocketSessionID;

    public User(String username) {
        this.username = username;
    }
}
