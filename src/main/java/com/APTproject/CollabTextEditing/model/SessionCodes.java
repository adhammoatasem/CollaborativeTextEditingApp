package com.APTproject.CollabTextEditing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SessionCodes {
    private String sessionId;
    private String viewerCode;
    private String editorCode;
}
