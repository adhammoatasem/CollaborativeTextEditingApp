package com.APTproject.CollabTextEditing.editor;

import com.APTproject.CollabTextEditing.model.EditMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class EditorController {

    @MessageMapping("/edit")
    @SendTo("/topic/EditMessage")
    public EditMessage editMessage(@Payload EditMessage edit){
        return edit;
    }



}
