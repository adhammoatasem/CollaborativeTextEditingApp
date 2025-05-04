package CRDT;

public class Remote_Operation
{
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

    public Type getType() { return type; }
    public Identifier getPrevId() { return prevId; }
    public Identifier getNewId() { return newId; }
    public char getValue() { return value; }
    public Identifier getTargetId() { return targetId; }
}
