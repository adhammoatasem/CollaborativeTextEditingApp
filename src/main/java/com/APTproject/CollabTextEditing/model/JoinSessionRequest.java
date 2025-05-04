package com.APTproject.CollabTextEditing.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class JoinSessionRequest
{
    private String username;
    private UserRole userRole = UserRole.EDITOR;
    private String sessionId;

    public String getUsername()
    { return username;
    }
}
