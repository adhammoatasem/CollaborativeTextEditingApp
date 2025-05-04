package com.APTproject.CollabTextEditing.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditMessage {
    private String action;
    private String charValue;
    private String prevId;
    private String positionId;
    private String userId;
    private String sessionId;
    //private long timestamp;

}
