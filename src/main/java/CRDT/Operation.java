package CRDT;

public class Operation
{
    public enum Type // type of op
    {
        INSERT, //0
        DELETE  //1
    }

    private final Type type; //type of op
    private final CRDT_Node affectedNode;
    private final int position;

    public Operation(Type type, CRDT_Node affectedNode, int position) //set operation lama tehsal
    {
        this.type = type;
        this.affectedNode = affectedNode;
        this.position = position;
    }

    public Type getType()
    {
        return type;
    }

    public CRDT_Node getAffectedNode()
    {
        return affectedNode;
    }

    public int getPosition()
    {
        return position;
    }


}