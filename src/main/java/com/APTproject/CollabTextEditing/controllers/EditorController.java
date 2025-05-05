package com.APTproject.CollabTextEditing.controllers;

import CRDT.CRDT_Document;
import CRDT.Remote_Operation;
import com.APTproject.CollabTextEditing.model.*;
import com.APTproject.CollabTextEditing.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller

public class EditorController {


    @Autowired
    private SessionService sessionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/session/join")
    @SendToUser("/queue/session-info")
    public SessionInfo handleJoin(JoinSessionRequest joinRequest, StompHeaderAccessor accessor) {
        String socketSessionId = accessor.getSessionId();
        String code = joinRequest.getCode();
        String name = joinRequest.getName();

        CollabSession session = sessionService.getSessionByCode(code); // find by viewer/editor code
        UserRole role = sessionService.getRoleByCode(code);

        User user = new User(name);
        user.setWebSocketSessionID(socketSessionId);
        user.setRole(role);

        String sessionId = session.getSessionCodes().getSessionId();
        sessionService.addUserToSession(sessionId, user);

        messagingTemplate.convertAndSend("/topic/" + sessionId + "/user-joined", user);
        System.out.println("User "+user.getUsername()+ "joined sesssion"+sessionId);

        return new SessionInfo(sessionId, role);
    }



   /* @MessageMapping("/session/edit/{sessionId}")
    public void editMessage(@Payload EditMessage edit,
                            @DestinationVariable String sessionId) {
        System.out.println("✍️ Edit received from user " + edit.getUserId() + ": " + edit.getCharValue());
        sessionService.applyEdit(edit,sessionService.getSession(sessionId));
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, edit);
    }*/

    @MessageMapping("/session/edit/{sessionId}")
    public void editMessage(@Payload EditMessage edit, @DestinationVariable String sessionId) {
        System.out.println("✍️ Edit received from user " + edit.getUserId() + ": " + edit.getCharValue());

        // Get the session and CRDT document
        CollabSession session = sessionService.getSession(sessionId);
        CRDT_Document document = session.getDoc();

        // Determine the type of operation (Insert/Delete)
        Remote_Operation operation;
        if (edit.getOperationType() == OperationTypes.INSERT) {
            operation = document.createInsertOperation(edit.getPosition(), edit.getCharValue());
        } else if (edit.getOperationType() == OperationTypes.DELETE) {
            operation = document.createDeleteOperation(edit.getPositionId());
        } else {
            System.err.println("❌ Unsupported operation type: " + edit.getOperationType());
            return;
        }

        // Apply the operation to the CRDT document
        document.applyRemoteOperation(operation);

        // Broadcast the operation to all clients in the session
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, operation.toJSON());
    }

    /*@MessageMapping("/session/requestDoc/{sessionId}")
    public void handleRequestDoc(@DestinationVariable String sessionId) {
        CollabSession session = sessionService.getSession(sessionId);
        String currentContent = session.getCrdt().toPlainText();
        System.out.println(session.getCrdt().toPlainText());

        // Send the latest document content back to the same session topic
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, new DocumentContentMessage(currentContent));
    }*/
}
