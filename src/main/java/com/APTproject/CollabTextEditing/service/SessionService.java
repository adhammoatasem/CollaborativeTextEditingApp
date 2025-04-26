package com.APTproject.CollabTextEditing.service;

import com.APTproject.CollabTextEditing.model.CollabSession;
import com.APTproject.CollabTextEditing.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<String, CollabSession> sessions = new ConcurrentHashMap<>();

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

    public CollabSession createSession(User creator) {
        String sessionId = UUID.randomUUID().toString().substring(0, 8);
        String viewerCode = generateAccessCode(8);
        String editorCode = generateAccessCode(8);

        Set<User> users = new HashSet<>();
        users.add(creator);

        CollabSession session = new CollabSession(sessionId, viewerCode, editorCode, "", users);
        sessions.putIfAbsent(sessionId, session);

        return session;
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public Collection<CollabSession> getAllSessions() {
        return sessions.values();
    }
}
