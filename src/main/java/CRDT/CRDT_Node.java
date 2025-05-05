package CRDT;
import java.util.ArrayList;
import java.util.List;
public class CRDT_Node {
    /// //////////////////////for comments////////////////////////
    private final List<Comment> comments = new ArrayList<>();

    public List<Comment> getComments() { return comments; }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
    /// ////////////////////////////////////////
    private final char value;

    public void setId(Identifier id) {
        this.id = id;
    }

    private  Identifier id;
    private boolean deleted = false;  //0->not deleted //1->deleted
    private List<CRDT_Node> next = new ArrayList<>(); //list of nodes after this one
    private CRDT_Node parent; // Add a parent reference
    /// change next to final if we reallly have to but we will delete insert and the setter which make us have to handle pointers manually //
    public CRDT_Node(char value, Identifier id) {
        this.value = value;
        this.id = id;
    }
    public CRDT_Node getParent() {
        return parent;
    }
    public void setParent(CRDT_Node parent) {
        this.parent = parent;
    }

    public char getValue() {
        return value;
    }

    public Identifier getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }


    public void delete() {
        deleted = true;
        comments.clear(); // Clear comments when text is deleted
    }
    /// flagging it as a deleted element

    public void undelete() {
        deleted = false;
    }

    public List<CRDT_Node> getNext() {
        return next;
    } //return list of next elements

    @Override
    public String toString() {

        if (deleted) {
            return "";
        } else {
            return Character.toString(value);
        }

    }

//    public void setNext(CRDT_Node next) {
//        this.next = next.getNext();
//    }

    public void setNext(List<CRDT_Node> next) {
        this.next = next;
    }

    ///  Inserts the next node (CRDTChar) after the current node.

//    public void insertNext(CRDT_Node node) {
//        // Make the current node point to the new node
//        node.setNext((CRDT_Node) this.next); // New node points to the current next node (if any)
//        this.setNext(node); // Current node now points to the new node
//    }
    public void insertNext(CRDT_Node node) {
        this.next.add(node); // Add the new node to the `next` list
        node.setParent(this); // Set the parent of the new node
        this.next.sort((a, b) -> a.getId().compareTo(b.getId())); // Sort the list by ID to maintain CRDT order
    }
    ///////////////////////////////////////////for adham (server) to use/////////////////////////////////////////////////////
    /// export fns bagama3hom f crdt_document
    public String getExportValue() // hamsek value value crdt ahawelha Lshaklaha f text 3ady w amalna 2 functions ashan n-handle deleted w /n ,/r
    {
        if (deleted)
        {
            return "";
        }
        if (value == '\n')
        {
            return System.lineSeparator(); // Convert to OS-specific line ending
        }
        if (value == '\r') ////to handle line breaks f files in old mac
        {
            return ""; // Discard them
        }
        return String.valueOf(value); // Reg characters
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////
}