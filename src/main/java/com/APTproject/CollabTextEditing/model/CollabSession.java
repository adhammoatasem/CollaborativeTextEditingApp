package com.APTproject.CollabTextEditing.model;

import CRDT.CRDT_Document;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CollabSession {
    private SessionCodes sessionCodes;
    private Set<User> users;
    private CRDT_Document doc;

    public void addUser(User user){
         users.add(user);
    }
}
