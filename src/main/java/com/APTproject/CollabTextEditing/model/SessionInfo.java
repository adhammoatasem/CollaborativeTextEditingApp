package com.APTproject.CollabTextEditing.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class SessionInfo {
    private String sessionId;
    private UserRole role;
}
