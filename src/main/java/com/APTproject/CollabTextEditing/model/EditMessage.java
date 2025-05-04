package com.APTproject.CollabTextEditing.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditMessage
{
    private String type; // e.g., "INSERT" or "DELETE"
    private String action; // e.g., "INSERT_CHAR" or "DELETE_CHAR"
    private char charValue; // The character being inserted or deleted

    public String getPositionId() {
        return positionId;
    }

    public String getType() {
        return type;
    }

    public String getAction() {
        return action;
    }

    public String getSessionId() {
        return sessionId;
    }

    private String positionId; // The position identifier in CRDT
    private String sessionId; // The session ID

    public String getUserId() {
        return userId;
    }

    private String userId; // The user performing the edit

    /**
     * Returns the operation type (INSERT or DELETE).
     * @return OperationType enum value
     */
    public OperationTypes.OperationType getOperationType() {
        return OperationTypes.OperationType.valueOf(type.toUpperCase());
    }

    /**
     * Returns the position as an integer.
     * @return The position where the operation should be applied
     */
    public int getPosition() {
        try {
            return Integer.parseInt(positionId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid position ID: " + positionId);
        }
    }

    /**
     * Returns the target CRDT identifier for delete operations.
     * @return The target identifier
     */
    public String getTargetId() {
        return positionId; // Assuming positionId is reused for delete operations
    }

    public char getCharValue()
    { return charValue;
    }
}