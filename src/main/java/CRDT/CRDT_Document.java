package CRDT;
import java.io.*;
import java.util.*;

public class CRDT_Document
{
    private final String Userid;
    private long DocCounter = -1;
    private final CRDT_Node head; // the first node dummy lowkey
    private final Map<Identifier, CRDT_Node> index = new HashMap<>();
    // private List<CRDT_Node> visibleNodes = new ArrayList<>(); ///see later
    /// /////////////////////////////////for redo and undo/////////////////////////////////////////////////////
    //using deque ashan a_edit front aw end

    // Stack to store last 3 operations for undo
    private final Deque<Operation> undoStack = new ArrayDeque<>();
    // Stack to store undone operations for redo
    private final Deque<Operation> redoStack = new ArrayDeque<>();

    /////////////////////////////////// getters w setters//////////////////////////////////////////
    public String getUserid() {
        return Userid;
    }

    public long getDocCounter() {
        return DocCounter;
    }


    public CRDT_Document() {
        this.Userid = "default_user"; // Default user ID
        this.head = new CRDT_Node('\0', new Identifier(Userid, -1)); // Dummy head
        index.put(head.getId(), head);
    }

    private Identifier nextId()
    {
        return new Identifier(Userid, DocCounter++); //userA rozana r->(userA,0), o->(userA,1)
    }
    /// /////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////continue from heree 20/4/2025/////////////////////////////////////////

//        public Identifier localInsert(int position, char c) // main bae
//        {
//            CRDT_Node prev = find_by_visiblePosition(position-1); //get prev index //shelna -1
//            long count;
//            count =prev.getId().getCounter()+1;
//            Identifier newId = new Identifier(getUserid(),count); //get new unique id
    ////            System.out.println("the prev ID  : " + prev.getId().toString());
    ////           System.out.println("the new ID  : " + newId.toString());
//
//            CRDT_Node node = new CRDT_Node(c, newId); //create node with the new id and the value
//            System.out.println("the prev node  : "+ prev.getValue()+ "  ID " + prev.getId().toString());
//            System.out.println("the new node   : " + prev.getValue()+ "  ID " + node.getId().toString());
//
//            addNode(prev, node); // add it and update the hashmap
//            /// ///////////////redo and undo////////////////////
//            // Push this operation to undo stack and clear redo stack
//            pushToUndoStack(new Operation(Operation.Type.INSERT, node, position)); // push el op flstack
//            redoStack.clear(); // once ma new op btegy elredo byrestart
//            /////////////////////////////////////////////////////
//            return newId; // return the new id so that remote users can use it
//        }

    public void reassignIdentifiers() {
        // Reset the counter for the document
        DocCounter = 0;

        // Traverse the document and reassign identifiers
        traverseAndReassign(head);

        // Optionally, print updated identifiers for debugging
        System.out.println("=== Identifiers After Reassignment ===");
        printIdentifiers();
    }

    private void traverseAndReassign(CRDT_Node node) {
        for (CRDT_Node child : node.getNext()) {
            if (!child.isDeleted()) {
                // Assign a new identifier to the node
                Identifier newId = new Identifier(Userid, DocCounter++);
                index.remove(child.getId()); // Remove old identifier from the index
                child.setId(newId); // Update the identifier
                index.put(newId, child); // Add the new identifier to the index
            }
            traverseAndReassign(child); // Recursively process child nodes
        }
    }


    private void addNodeBefore(CRDT_Node parent, CRDT_Node target, CRDT_Node newNode) {
        // Get the parent's children list
        List<CRDT_Node> targetList = (parent == null) ? head.getNext() : parent.getNext();

        // Set the parent of the new node
        newNode.setParent(parent == null ? head : parent);

        // Find the index of the target node in the children list
        int index = targetList.indexOf(target);

        // Add the new node at the calculated index (before the target node)
        if (index == -1) {
            targetList.add(newNode); // If the target is not found, add to the end of the list
        } else {
            targetList.add(index, newNode); // Insert before the target node
        }

        // No sorting is necessary here because we're inserting at the correct index
        this.index.put(newNode.getId(), newNode); // Add to the index for fast lookup
    }
    /// see later



    /// for remote users /////////////////to test



//    public void remoteInsert(Identifier prevId, Identifier newId, char c) {
//        // Retrieve the previous node using the index map
//        CRDT_Node prev = index.get(prevId);
//
//        // Handle the case where the previous node is not found
//        if (prev == null) {
//            System.err.println("Remote Insert Failed: prevId not found in index. Message might be out of order.");
//            return; // Safely exit if the previous node is not found
//        }
//
//        // Create the new node using the received ID and character
//        CRDT_Node node = new CRDT_Node(c, newId);
//
//        // Determine the parent and target node
//        CRDT_Node parent = prev.getParent();
//        CRDT_Node target = prev.getNext().isEmpty() ? null : prev.getNext().get(0);
//
//        // Add the new node before the target node
//        addNodeBefore(parent, target, node);
//
//        // Optionally log the successful insertion
//        System.out.println("Remote Insert Successful: Node '" + c + "' inserted after prevId: " + prevId);
//    }// called by network when receiving char from remote user
    /// ///////////////locallll///////////////////////
    public void delete(Identifier id)
    {
        CRDT_Node node = index.get(id);
        if (node != null)
        {node.delete();}
        pushToUndoStack(new Operation(Operation.Type.DELETE, node, -1));
        redoStack.clear();
    }
/// //////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////fih add node tanya fo2iha ///////////////////////////////////////////////////////

    /// ////////////////////////////////////////////////////////// copilot add node

    private void addNode(CRDT_Node prev, CRDT_Node node) // ely feha sorting lnext
    {
        List<CRDT_Node> targetList = (prev == null) ? head.getNext() : prev.getNext();
        targetList.add(node); // Add the new node to the list
        node.setParent(prev == null ? head : prev); // Set the parent of the new node
        targetList.sort((a, b) -> a.getId().compareTo(b.getId())); // Sort nodes by ID to maintain CRDT order
        index.put(node.getId(), node); // Add to the index for fast lookup
    }

////        private void addNode(CRDT_Node prev, CRDT_Node node) {
////            List<CRDT_Node> targetList;
////
//            if (prev == null) {
//                // If prev is null, insert at the beginning (not the end of the document)
//                targetList = visibleNodes; // This should be your top-level list of nodes
//                // Make sure to insert at the start if it's the first node
//                targetList.add(0, node);  // Insert at the beginning
//            } else {
//                // Otherwise, insert after prev in its next list
//                targetList = prev.getNext();
//                targetList.add(node);  // Insert after the previous node
//            }
//
//            // Sort the list by the node's ID (keeping order of insertion and versioning)
//            targetList.sort(Comparator.comparing(CRDT_Node::getId));
//
//            // Add the new node to the index for lookup by its identifier
//            index.put(node.getId(), node);
//        }

    /////////////////////////////////dont forget missing functionalities/////////////////////////////////////////////////////

    public String toPlainText()
    {
        StringBuilder sb = new StringBuilder();
        traverse(head, sb); // traverse the whole tree to append the text to the string builder then turning it to a string
        return sb.toString(); //while ignoring deleted nodes
    }
    @Override
    public String toString()
    {
        // Use the existing toPlainText method to get the document content
        return toPlainText();
    }
///////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// helper functionss//////////////////////////////////////////
//        private void traverse(CRDT_Node node, StringBuilder sb)
//        {
//            for (CRDT_Node nxt : node.getNext())  //hanmsek kol node w n3ady ala children depth first then elforloop ensures law andy kaza child ha3ady alehom bardo
//            {
//                if (!nxt.isDeleted()) sb.append(nxt.getValue()); // law mesh deleted hayhotaha gher keda ignore
//                traverse(nxt, sb); //call it again for next child
//            }
//        }

//        private CRDT_Node find_by_visiblePosition(int pos)
//        {
//            final int[] count = {-1}; //for head //array for recursion instead of normal int //for java ig
//            return find_by_recursion(head, pos, count);
//        }

//        private CRDT_Node find_by_recursion(CRDT_Node node, int targetPos, int[] count) {
//            for (CRDT_Node nxtnode : node.getNext()) {
//                if (!nxtnode.isDeleted()) {
//                    count[0]++;
//                } // ignoring deleted nodes (to get position elsah)
//                if (count[0] == targetPos) {
//                    return nxtnode;
//                }
//                CRDT_Node found = find_by_recursion(nxtnode, targetPos, count);
//                if (found != null)
//                {
//                    return found;
//                }
//            }
//            return node; //return last node w2efna andaha
//        }

    /// ///////////////////////////////////////////UNDO AND REDO FUNCTIONS//////////////////////////////////////////////////////


    public boolean canUndo() // make sure fih haga a3melaha undo w en heya mesh remote op
    {
        if (undoStack.isEmpty()||!isLocalOperation(undoStack.peekLast()))
        {return false;}
        return true;
    }




    public boolean canRedo() // make sure fih haga a3melaha redo w mesh remotes
    {
        if (redoStack.isEmpty() || !isLocalOperation(redoStack.peekLast()))
        {return false;}
        return true;

    }


    private boolean isLocalOperation(Operation op)
    {
        if (op == null)
        {
            return false;
        }
        if (op.getAffectedNode() == null)
        {
            return false;
        }
        if (!op.getAffectedNode().getId().getUserID().equals(this.Userid))  //makesure op is done by the localuser mesh remote
        {
            return false;
        }
        return true;
    }

    private void pushToUndoStack(Operation Op)
    {
        if (undoStack.size() == 3) // we care abt last 3 operations
        {
            undoStack.removeFirst(); // remove mn elawel ehna ayzen akher 3
        }
        undoStack.addLast(Op);
    }
    /// undo
    public void undo()
    {
        if (canUndo())
        {
            Operation op = undoStack.removeLast(); // remove the last op ->op
            redoStack.addLast(op); // ahot op -> redostack

            if (op.getType() == Operation.Type.INSERT)
            {
                op.getAffectedNode().delete(); // law kanet insert -> haamel delete
            } else if (op.getType() == Operation.Type.DELETE)
            {
                op.getAffectedNode().undelete(); // law kanet delete -> haamelaha undelete mesh insert (ha set deleted b 0)
            }
        }
    }
    /// redo
    public void redo()
    {  // im doing the actual op type ashan dah reDO mesh undo
        if (canRedo())
        {
            Operation op = redoStack.removeLast(); // akher op haamelha tany
            undoStack.addLast(op); // lama a3mel redo lhaga hatrga3 lelundo if i need to undo

            if (op.getType() == Operation.Type.INSERT)
            {
                op.getAffectedNode().undelete(); // haraga3ha
            } else if (op.getType() == Operation.Type.DELETE)
            {
                op.getAffectedNode().delete(); // hamsah
            }
        }
    }

/// //////////////////////////////ask chat to explain/ for server we need to add more data members to operation ////////////////////////////////////////////////
/// not now for server to do //// 3 missing neeeddd
//    public void applyRemoteOperation(Operation op)
//    {
//        if (op.getType() == Operation.Type.INSERT) {
//            // remoteInsert takes (prevId, newId, value)
//            remoteInsert(op.getPrevId(), op.getNewId(), op.getValue());
//        } else { // DELETE
//            delete(op.getTargetId());
//        }
//    }
    //////////////////////////////////////// import and export////////////////////////////////////////////////////

    /// to clear doc
    private void Clear_Document()
    {
        head.getNext().clear();
        index.clear();
        index.put(head.getId(), head);
        DocCounter = 0;
        undoStack.clear();
        redoStack.clear();
    }

    /// import
    public void importFromText(String text)
    {
        Clear_Document(); // reset kol haga here / clear

        CRDT_Node current = head;
        for (char c : text.toCharArray())  // haymshy ala kol char w law mesh new line aw \r hay3melaha id w yhotaha f node w y-add elnode dih
        {
            if (c == '\r') continue; // skip
            Identifier id = nextId();
            CRDT_Node node = new CRDT_Node(c, id);
            addNode(current, node);
            current = node;
        }
    }
    ///   ///////////////// we need to get the path mn heta ask gpt where to get or define it ///////////////
    public void importFromFile(String path)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            reader.close();

            importFromText(builder.toString()); // reuse existing method
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    ///export
    public String exportToText() /// traverse all the nodes and then after appending everything to sb it converts it to a string
    {
        StringBuilder sb = new StringBuilder();
        traverseForExport(head, sb);
        return sb.toString();
    }

    private void traverseForExport(CRDT_Node node, StringBuilder sb) //traverses by recursion over the nodes and appends each exported value(handeled) to the string builder
    {     // lahad ma tkhalas then bterga3 lel export to text kamla to be converted
        for (CRDT_Node nxt : node.getNext()) {
            if (!nxt.isDeleted()) {
                sb.append(nxt.getExportValue());
            }
            traverseForExport(nxt, sb);
        }
    }
    /// //////////////////////we need to give a path define it or ne3raf bngebo mnen//////////////////////
    public void exportToFile(String path)
    {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(toPlainText());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
///  for adham to use in curser tracking
/// /////////////////////to do still 25/4 /////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////////////////////////for curser tracking //////////////////////////////////////////////
    /**
     * @param visualPos Editor position (0=before first char)
     * @return CRDT node at that position
     */
    public CRDT_Node getNodeAtPosition(int visualPos) {
        return find_by_visiblePosition(visualPos);
    }

    /**
     * @param nodeId CRDT node identifier
     * @return Editor position (0-based)
     */
    public int getVisualPosition(Identifier nodeId) {
        int[] count = { -1 };
        return countVisibleNodes(head, nodeId, count);
    }

    private int countVisibleNodes(CRDT_Node node, Identifier targetId, int[] count) {
        for (CRDT_Node child : node.getNext()) {
            if (!child.isDeleted()) {
                count[0]++;
                if (child.getId().equals(targetId)) {
                    return count[0];
                }
            }
            int result = countVisibleNodes(child, targetId, count);
            if (result != -1) return result;
        }
        return -1; // Not found
    }



/// //////////////////////////////FIXES//////////////////////////////////////////////////////////
//public Identifier localInsert(int position, char c) {
//    CRDT_Node prev = (position == 0) ? null : find_by_visiblePosition(position - 1); // Handle position 0
//    Identifier newId = nextId(); // Get new unique ID
//    CRDT_Node node = new CRDT_Node(c, newId); // Create new node with the character and ID
//    addNode(prev, node); // Add node to the document
//
//    ///////////////// Redo and Undo /////////////////
//    // Push the insert operation to the undo stack
//    pushToUndoStack(new Operation(Operation.Type.INSERT, node, position)); // Push operation to undo stack
//    redoStack.clear(); // Clear redo stack after new operation
//    ///////////////////////////////////////////////////
//
//    return newId; // Return the new identifier for remote users
//}

//private CRDT_Node find_by_visiblePosition(int pos) {
//    final int[] count = {-1}; // Use array for recursion (counter)
//    return find_by_recursion(head, pos, count); // Start recursion from the head node
//}
//
//private CRDT_Node find_by_recursion(CRDT_Node node, int targetPos, int[] count)
//{
//    for (CRDT_Node nxtnode : node.getNext())
//    {
//        if (!nxtnode.isDeleted())
//        {
//            count[0]++; // Skip deleted nodes
//        }
//
//        if (count[0] == targetPos)
//        {   System.out.println(" we found the target ");
//            System.out.println(" the value of node "+ nxtnode.getValue());
//            return nxtnode; // Return the node at the desired position
//        }
//
//        CRDT_Node found = find_by_recursion(nxtnode, targetPos, count); // Recursively search the next nodes
//        if (found != null)
//        {
//            return found;
//        }
//    }
//
//    return node; // Return the last node if no other node is found
//}
/// ////////////////////////////////////////////////////////////////////////////////////////////////
//
//private CRDT_Node find_by_visiblePosition(int pos) {
//    final int[] count = { -1 }; // Start from dummy head position
//    return find_by_recursion(head, pos, count);
//}
//
//    private CRDT_Node find_by_recursion(CRDT_Node node, int targetPos, int[] count) {
//        for (CRDT_Node nxtnode : node.getNext()) {
//            if (!nxtnode.isDeleted()) {
//                count[0]++;
//            }
//            if (count[0] == targetPos) {
//                return nxtnode;
//            }
//            CRDT_Node found = find_by_recursion(nxtnode, targetPos, count);
//            if (found != null) {
//                return found;
//            }
//        }
//        return node; // fallback: return last valid node
//    }
    /// ///////////////////////////////////////////////////////new copilot
//    private CRDT_Node find_by_visiblePosition(int pos) {
//        final int[] count = { -1 }; // Start at -1 to account for the dummy head
//        return find_by_recursion(head, pos, count);
//    }
//
//    private CRDT_Node find_by_recursion(CRDT_Node node, int targetPos, int[] count) {
//        for (CRDT_Node nxtnode : node.getNext()) {
//            if (!nxtnode.isDeleted()) {
//                count[0]++;
//            }
//            if (count[0] == targetPos) {
//                return nxtnode; // Found the node at the target position
//            }
//            CRDT_Node found = find_by_recursion(nxtnode, targetPos, count);
//            if (found != null) {
//                return found;
//            }
//        }
//        return node; // Fallback to the last valid node
//    }
    /// //////////////////////////////////////////////////////////////////////////
    public CRDT_Node find_by_visiblePosition(int pos) {
        final int[] count = { -1 }; // Start at -1 to account for the dummy head
        return find_by_recursion(head, pos, count);
    }

    public CRDT_Node find_by_recursion(CRDT_Node node, int targetPos, int[] count) {
        for (CRDT_Node nxtnode : node.getNext()) {
            if (!nxtnode.isDeleted()) {
                count[0]++;
            }
            if (count[0] == targetPos) {
                return nxtnode; // Return the node at the target position
            }
            CRDT_Node found = find_by_recursion(nxtnode, targetPos, count);
            if (found != null) {
                return found;
            }
        }
        return null; // Return null if no node is found at the target position
    }
/// ///////////////////////////////////////////////////////////////////////////////////////first breadth first
//private void traverse(CRDT_Node root, StringBuilder sb) {
//    // Use a queue to process nodes level by level
//    Queue<CRDT_Node> queue = new LinkedList<>();
//
//    // Start with the root node
//    queue.add(root);
//
//    while (!queue.isEmpty()) {
//        // Get the next node from the queue
//        CRDT_Node current = queue.poll();
//
//        // Process the current node
//        if (!current.isDeleted()) {
//            sb.append(current.getValue());
//        }
//
//        // Add all children of the current node to the queue
//        for (CRDT_Node child : current.getNext()) {
//            queue.add(child);
//        }
//    }
//}
    /// /////////////////////////////////////////////// copilot version
    private void traverse(CRDT_Node root, StringBuilder sb) {
        Queue<CRDT_Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            CRDT_Node current = queue.poll();

            // Skip the dummy head node and deleted nodes
            if (current != head && !current.isDeleted()) {
                sb.append(current.getValue());
            }

            // Add all children of the current node to the queue
            queue.addAll(current.getNext());
        }
    }
    /// ////////////////////print identifiers
    public void printIdentifiers() {
        traverseAndPrint(head); // Start traversal from the head node
    }

    private void traverseAndPrint(CRDT_Node node) {
        for (CRDT_Node child : node.getNext()) {
            if (!child.isDeleted()) { // Skip deleted nodes
                System.out.println("Character: " + child.getValue() + ", Identifier: " + child.getId());
            }
            traverseAndPrint(child); // Recursively go through the tree
        }
    }
    /////////////////////////////////////////////////////////////////////////
//    public void remoteInsert(Identifier prevId, Identifier newId, char c) {
//        // Retrieve the previous node using the index map
//        CRDT_Node prev = index.get(prevId);
//
//        // Handle the case where the previous node is not found
//        if (prev == null) {
//            System.err.println("Remote Insert Failed: prevId not found in index. Message might be out of order.");
//            return; // Safely exit if the previous node is not found
//        }
//
//        // Determine the target position for insertion based on prevId
//        int position = getVisualPosition(prev.getId()) + 1;
//
//        // Use the localInsert logic, but override the newId and bypass undo/redo stack management
//        CRDT_Node target = find_by_visiblePosition(position); // Find the node at the target position
//        CRDT_Node parent = (target == null) ? head : target.getParent(); // Find the parent node
//        CRDT_Node node = new CRDT_Node(c, newId); // Create the new node with the provided remote ID
//
//        addNodeBefore(parent, target, node); // Add the new node before the target position
//
//        // Reassign identifiers for consistency
//        reassignIdentifiers();
//
//        // Log the successful insertion
//        System.out.println("Remote Insert Successful: Node '" + c + "' inserted after prevId: " + prevId);
//    }
//    public void remoteInsert(Identifier prevId, Identifier newId, char c) {
//        // Retrieve the previous node using the index map
//        CRDT_Node prev = index.get(prevId);
//
//        // Handle the case where the previous node is not found
//        if (prev == null) {
//            System.err.println("Remote Insert Failed: prevId not found in index. Using head node as fallback.");
//            prev = head; // Fallback to the head node if prevId is not found
//        }
//
//        // Determine the target position for insertion based on prevId
//        int position = getVisualPosition(prev.getId()) + 1;
//
//        // Find the target node and parent node
//        CRDT_Node target = find_by_visiblePosition(position); // Find the node at the target position
//        CRDT_Node parent = (target == null) ? head : target.getParent(); // Find the parent node
//
//        // Create the new node using the received ID and character
//        CRDT_Node node = new CRDT_Node(c, newId);
//
//        // Add the new node before the target node
//        addNodeBefore(parent, target, node);
//
//        // Reassign identifiers for consistency
//        reassignIdentifiers();
//
//        // Optionally log the successful insertion
//        System.out.println("Remote Insert Successful: Node '" + c + "' inserted after prevId: " + prevId);
//    }

    public void remoteInsert(Identifier prevId, Identifier newId, char c) {
        // Retrieve the previous node using the index map
        CRDT_Node prev = index.get(prevId);

        // Handle the case where the previous node is not found
        if (prev == null) {
            System.err.println("Remote Insert Failed: prevId not found in index. Using head node as fallback.");
            prev = head; // Fallback to the head node if prevId is not found
        }

        // Special case: Ensure the first ID starts from 0 if it's -1
        if (prev.getId().getCounter() == -1) {
            prev.getId().setCounter(0); // Adjust the counter to start from 0
            index.put(prev.getId(), prev); // Update the index with the adjusted ID
        }

        // Determine the target position for insertion based on prevId
        int position = getVisualPosition(prev.getId()) + 1;

        // Find the target node and parent node
        CRDT_Node target = find_by_visiblePosition(position); // Find the node at the target position
        CRDT_Node parent = (target == null) ? head : target.getParent(); // Find the parent node

        // Create the new node using the provided ID
        CRDT_Node node = new CRDT_Node(c, newId);

        // Add the new node before the target node
        addNodeBefore(parent, target, node);

        // Reassign identifiers for consistency
        reassignIdentifiers();

        // Optionally log the successful insertion
        System.out.println("Remote Insert Successful: Node '" + c + "' inserted after prevId: " + prevId);
    }

    public Identifier localInsert(int position, char c) {
        CRDT_Node target = find_by_visiblePosition(position); // Find the node at the exact position
        CRDT_Node prev = (target == null) ? head : target.getParent(); // Find the parent of the target node
        Identifier newId = nextId(); // Generate a unique ID
        CRDT_Node node = new CRDT_Node(c, newId); // Create the new node

        addNodeBefore(prev, target, node); // Add the new node before the target node

        // Push the operation to the undo stack
        pushToUndoStack(new Operation(Operation.Type.INSERT, node, position));
        redoStack.clear(); // Clear redo stack after a new operation

        reassignIdentifiers(); ///for sorting identifiers

        return newId; // Return the new ID
    }
    /// ////////////////////////////////////Remote operation class //////////////////////////////////////////
    public void applyRemoteOperation(Remote_Operation op)
    {
        if (op.getType() == Remote_Operation.Type.INSERT) {
            remoteInsert(op.getPrevId(), op.getNewId(), op.getValue());
        } else {
            delete(op.getTargetId());
        }
    }
    /// ////////////////////////////////////////////////////////
//    public Remote_Operation createInsertOperation(int position, char c) {
//        CRDT_Node prev = find_by_visiblePosition(position - 1);
//        Identifier newId = nextId();
//        CRDT_Node node = new CRDT_Node(c, newId);
//        addNode(prev, node);
//        pushToUndoStack(new Operation(Operation.Type.INSERT, node, position));
//        redoStack.clear();
//        return new Remote_Operation(prev.getId(), newId, c);
//    }
//    public Remote_Operation createDeleteOperation(Identifier id) {
//        CRDT_Node node = index.get(id);
//        if (node != null) {
//            node.delete();
//            pushToUndoStack(new Operation(Operation.Type.DELETE, node, -1));
//            redoStack.clear();
//            return new Remote_Operation(id);
//        }
//        return null;
//    }
    /// //after edit message
    public Remote_Operation createInsertOperation(int position, char c) {
        CRDT_Node prev = find_by_visiblePosition(position - 1); // Find previous node
        Identifier newId = nextId(); // Generate a unique identifier
        CRDT_Node node = new CRDT_Node(c, newId); // Create a new node
        addNode(prev, node); // Add the node to the CRDT document
        return new Remote_Operation(prev.getId(), newId, c); // Return the operation
    }


    public Remote_Operation createDeleteOperation(String targetId) {
        Identifier id = Identifier.fromString(targetId); // Parse the identifier
        CRDT_Node node = index.get(id); // Find the node by its identifier
        if (node != null) {
            node.delete(); // Mark the node as deleted
            return new Remote_Operation(id); // Return the delete operation
        }
        throw new IllegalArgumentException("Target node not found: " + targetId);
    }

    ////////////////////////fpr comments////////////////////////////////////
    /*public boolean addCommentToNode(Identifier id, Comment comment) {
        CRDT_Node node = index.get(id);
        if (node != null && !node.isDeleted()) {
            node.addComment(comment);
            return true;
        }
        return false;
    }*/
}
