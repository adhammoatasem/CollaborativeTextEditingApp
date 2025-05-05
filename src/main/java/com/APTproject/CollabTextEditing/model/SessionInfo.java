package com.APTproject.CollabTextEditing.model;

import lombok.*;

@Getter
@Setter
public class SessionInfo {
    private String sessionId;
    private UserRole role = null;
    private String message=null;

    public SessionInfo(String sessionId, String message) {
        this.sessionId = sessionId;
        this.message = message;
    }
    public SessionInfo(String sessionId, UserRole role) {
        this.sessionId = sessionId;
        this.role = role;
    }
}
