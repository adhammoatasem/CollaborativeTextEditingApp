package com.APTproject.CollabTextEditing.model;

import CRDT.CRDT_Document;
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

    public CollabSession(String sessionId, String viewerCode, String editorCode, String s, Set<User> users) {
    }

    public Set<User> getUsers() {
        return users;
    }

    public CRDT_Document getDoc() {
        return doc;
    }

    public String getDocumentContent() {
        return documentContent;
    }

    public String getEditorCode() {
        return editorCode;
    }

    public String getViewerCode() {
        return viewerCode;
    }

    private Set<User> users;
private CRDT_Document doc;
    public void addUser(User user){
         users.add(user);
    }

    public String getSessionId() 
    { return sessionId;
    }

    public CRDT_Document getCrdt() 
    { return doc;
    }
}
