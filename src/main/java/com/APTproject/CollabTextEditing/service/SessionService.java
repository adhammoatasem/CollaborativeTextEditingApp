package com.APTproject.CollabTextEditing.service;

import CRDT.CRDT_Document;
import CRDT.Identifier;
import com.APTproject.CollabTextEditing.model.*;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<String, CollabSession> sessions = new ConcurrentHashMap<>();
    Map<String, String> editorCodeToSessionId = new HashMap<>();
    Map<String, String> viewerCodeToSessionId = new HashMap<>();

    private String generateAccessCode(int length) {
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


    public CollabSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public CollabSession createSession() {
        String sessionId = UUID.randomUUID().toString().substring(0, 8);
        String viewerCode = generateAccessCode(8);
        String editorCode = generateAccessCode(8);

        Set<User> users = new HashSet<>();
        SessionCodes sessionCodes = new SessionCodes(sessionId,viewerCode,editorCode);

        CollabSession session = new CollabSession(sessionCodes, null, users);
        sessions.putIfAbsent(sessionId, session);
        editorCodeToSessionId.put(editorCode,sessionId);
        viewerCodeToSessionId.put(viewerCode,sessionId);

        return session;
    }

    public void addUserToSession(String sessionID,User user) {
        CollabSession session = getSession(sessionID);
        session.addUser(user);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public Collection<CollabSession> getAllSessions() {
        return sessions.values();
    }


    private Identifier parseIdentifier(String idString) {
        String[] parts = idString.split("-");
        return new Identifier(parts[0], Long.parseLong(parts[1]));
    }



    public void applyEdit(EditMessage msg,CollabSession collabSession) {
        if ("INSERT".equals(msg.getAction())) {
            Identifier prev = parseIdentifier(msg.getPrevId());
            Identifier newId = parseIdentifier(msg.getPositionId());
            char value = msg.getCharValue().charAt(0);
            collabSession.getCrdt().remoteInsert(prev, newId, value);
        } else if ("DELETE".equals(msg.getAction())) {
            Identifier target = parseIdentifier(msg.getPositionId());
            collabSession.getCrdt().delete(target);
        }
    }

    public CollabSession getSessionByCode(String code) {
        String sessionId = editorCodeToSessionId.get(code);
        if (sessionId == null) {
            sessionId = viewerCodeToSessionId.get(code);
        }

        if (sessionId == null) {
            throw new IllegalArgumentException("Invalid session code: " + code);
        }

        return sessions.get(sessionId);
    }

    public UserRole getRoleByCode(String code) {
        if (editorCodeToSessionId.containsKey(code)) return UserRole.EDITOR;
        if (viewerCodeToSessionId.containsKey(code)) return UserRole.VIEWER;
        throw new IllegalArgumentException("Invalid session code");
    }
}
