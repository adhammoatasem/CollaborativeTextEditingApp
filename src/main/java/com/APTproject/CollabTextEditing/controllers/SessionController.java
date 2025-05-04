package com.APTproject.CollabTextEditing.controllers;

import com.APTproject.CollabTextEditing.model.CollabSession;
import com.APTproject.CollabTextEditing.model.SessionCodes;
import com.APTproject.CollabTextEditing.service.SessionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/create")
    public SessionCodes createSession() {
        // This calls the service to generate the session and return the codes
        CollabSession session=sessionService.createSession();
        System.out.println("session created");
        System.out.println("session id: "+ session.getSessionCodes().getSessionId());
        System.out.println("editor code: "+session.getSessionCodes().getEditorCode());
        System.out.println("viewer code: "+session.getSessionCodes().getViewerCode());
        return session.getSessionCodes();
    }
}
