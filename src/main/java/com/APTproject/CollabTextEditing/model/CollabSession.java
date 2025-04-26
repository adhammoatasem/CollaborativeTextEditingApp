package com.APTproject.CollabTextEditing.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CollabSession {
    private String sessionId;
    private String viewerCode;
    private String editorCode;
    private String documentContent;
    private Set<User> users;

    public void addUser(User user){
         users.add(user);
    }
}
