package com.APTproject.CollabTextEditing.editor;

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



    @MessageMapping("/session/edit/{sessionId}")
    public void editMessage(@Payload EditMessage edit,
                            @DestinationVariable String sessionId) {
        System.out.println("✍️ Edit received from user " + edit.getUserId() + ": " + edit.getCharValue());
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, edit);
    }
}
