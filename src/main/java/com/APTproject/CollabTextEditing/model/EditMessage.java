package com.APTproject.CollabTextEditing.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditMessage {
    private String type;
    private String action;
    private String charValue;
    private String positionId;
    private String sessionId;
    private String userId;

}
