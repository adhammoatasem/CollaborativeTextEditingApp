package CRDT;

import com.APTproject.CollabTextEditing.model.EditMessage;
import com.google.gson.Gson;

public class Remote_Operation {
    public enum Type { INSERT, DELETE }

    private final Type type;
    private final Identifier prevId, newId; // for insert
    private final char value;               // for insert
    private final Identifier targetId;      // for delete

    // Insert constructor
    public Remote_Operation(Identifier prevId, Identifier newId, char value) {
        this.type = Type.INSERT;
        this.prevId = prevId;
        this.newId = newId;
        this.value = value;
        this.targetId = null;
    }

    // Delete constructor
    public Remote_Operation(Identifier targetId) {
        this.type = Type.DELETE;
        this.prevId = null;
        this.newId = null;
        this.value = '\0';
        this.targetId = targetId;
    }

    // Getters
    public Type getType() { return type; }
    public Identifier getPrevId() { return prevId; }
    public Identifier getNewId() { return newId; }
    public char getValue() { return value; }
    public Identifier getTargetId() { return targetId; }

    // JSON Serialization
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // JSON Deserialization
    public static Remote_Operation fromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Remote_Operation.class);
    }

    /**
     * Utility method to create a Remote_Operation from EditMessage.
     * @param editMessage The EditMessage object.
     * @param crdtDocument The CRDT document to use for creating the operation.
     * @return A Remote_Operation object.
     */
    public static Remote_Operation fromEditMessage(EditMessage editMessage, CRDT_Document crdtDocument) {
        if (editMessage.getOperationType() == OperationTypes.INSERT) {
            // Create INSERT operation
            Identifier prevId = crdtDocument.find_by_visiblePosition(editMessage.getPosition() - 1).getId();
            Identifier newId = new Identifier(crdtDocument.getUserid(),crdtDocument.getDocCounter()+1); ;
            return new Remote_Operation(prevId, newId, editMessage.getCharValue());
        } else if (editMessage.getOperationType() == OperationTypes.DELETE) {
            // Create DELETE operation
            Identifier targetId = Identifier.fromString(editMessage.getPositionId());
            return new Remote_Operation(targetId);
        } else {
            throw new IllegalArgumentException("Unsupported operation type: " + editMessage.getOperationType());
        }
    }

}