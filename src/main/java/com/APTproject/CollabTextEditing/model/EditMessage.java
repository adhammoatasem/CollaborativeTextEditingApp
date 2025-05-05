package com.APTproject.CollabTextEditing.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditMessage {
    private String type; // e.g., "INSERT" or "DELETE"
    private char charValue; // The character being inserted or deleted
    private String positionId; // The position identifier in CRDT
    private String sessionId; // The session ID
    private String userId; // The user performing the edit

    public OperationTypes getOperationType() {
        return OperationTypes.valueOf(type.toUpperCase());
    }

    public int getPosition() {
        try {
            return Integer.parseInt(positionId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid position ID: " + positionId);
        }
    }

}
