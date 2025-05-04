package com.APTproject.CollabTextEditing.model;

import CRDT.CRDT_Document;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CollabSession {
    private SessionCodes sessionCodes;
    private final CRDT_Document crdt;
    private Set<User> users;

    public void addUser(User user){
         users.add(user);
    }
}
