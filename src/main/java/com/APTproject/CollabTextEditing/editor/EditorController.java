package com.APTproject.CollabTextEditing.editor;

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
import org.springframework.stereotype.Controller;

@Controller

public class EditorController {


    @Autowired
    private SessionService sessionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/session/createSession")
    @SendTo("/topic/public")
    public SessionInfo handleCreateSession(User creator){
        CollabSession session = sessionService.createSession(creator);
        return new SessionInfo(session.getSessionId(),"new session created");
    }


    @MessageMapping("/session/join")
    @SendTo("/topic/session/{sessionId}")
    public SessionInfo handleJoin(@Payload JoinSessionRequest request,
                                  SimpMessageHeaderAccessor headerAccessor,
                                  @DestinationVariable String sessionId) {

        String websocketSessionId = headerAccessor.getSessionId();
        System.out.println("User joined from WebSocket session: " + websocketSessionId);

        User user = new User(request.getUsername(), websocketSessionId);

        CollabSession session = sessionService.getSession(sessionId);
        session.addUser(user);

        return new SessionInfo(sessionId, user.getUsername() + " joined the session.");
    }



//    @MessageMapping("/session/edit/{sessionId}")
//    public void editMessage(@Payload EditMessage edit,
//                            @DestinationVariable String sessionId) {
//        System.out.println("✍️ Edit received from user " + edit.getUserId() + ": " + edit.getCharValue());
//        messagingTemplate.convertAndSend("/topic/session/" + sessionId, edit);
//    }


    /// /////////////////rozz///////////////////////////
    @MessageMapping("/session/edit/{sessionId}")
    public void editMessage(@Payload EditMessage edit, @DestinationVariable String sessionId) {
        System.out.println("✍️ Edit received from user " + edit.getUserId() + ": " + edit.getCharValue());

        // Get the session and CRDT document
        CollabSession session = sessionService.getSession(sessionId);
        CRDT_Document document = session.getCrdt();

        // Determine the type of operation (Insert/Delete)
        Remote_Operation operation;
        if (edit.getOperationType() == OperationTypes.OperationType.INSERT) {
            operation = document.createInsertOperation(edit.getPosition(), edit.getCharValue());
        } else if (edit.getOperationType() == OperationTypes.OperationType.DELETE) {
            operation = document.createDeleteOperation(edit.getTargetId());
        } else {
            System.err.println("❌ Unsupported operation type: " + edit.getOperationType());
            return;
        }

        // Apply the operation to the CRDT document
        document.applyRemoteOperation(operation);

        // Broadcast the operation to all clients in the session
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, operation.toJSON());
    }
}
