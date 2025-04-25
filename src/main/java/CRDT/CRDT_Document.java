package CRDT;
import java.util.HashMap;
import java.util.Map;
public class CRDT_Document
{

       private final String Userid;
        private long DocCounter = 0;


        private final CRDT_Node head; // the first node dummy lowkey
        private final Map<Identifier, CRDT_Node> index = new HashMap<>();

        public CRDT_Document(String user)
        {
            this.Userid = user;
            this.head = new CRDT_Node('\0', new Identifier(user, -1)); // Dummy head
            index.put(head.getId(), head);
        }

        private Identifier nextId()
        {
            return new Identifier(Userid, DocCounter++); //userA rozana r->(userA,1), o->(userA,2)
        }
    /// /////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////continue from heree 20/4/2025/////////////////////////////////////////
    ///  don't forget
    /// /(insert(char value, int position, String userId)
    /// 	•	delete(int position, String userId)
    /// 	•	merge(List<RemoteEdit>)
    /// 	•	String toPlainText())
    /// for mee
        public Identifier localInsert(int position, char c)
        {
            CRDT_Node prev = find_by_visiblePosition(position - 1); //get prev index
            Identifier newId = nextId(); //get new unique id
            CRDT_Node node = new CRDT_Node(c, newId); //create node with the new id and the value
            addNode(prev, node); // add it and update the hashmap
            return newId; // return the new id so that remote users can use it
        }
/// for remote users

        public void remoteInsert(Identifier prevId, Identifier newId, char c)
        { //newid howa ely tale3 mn local insert 3nd ellaptop eltany
            CRDT_Node prev = index.get(prevId);
            if (prev == null)
            {return;} //for safety in case the message arrived out of order
            CRDT_Node node = new CRDT_Node(c, newId); //id received from remote user
            addNode(prev, node);
        } // called by network when receiving char from remote user

        public void delete(Identifier id)
        {
            CRDT_Node node = index.get(id);
            if (node != null) node.delete();
        }

        private void addNode(CRDT_Node prev, CRDT_Node node)
        {
            prev.insertNext(node);
            index.put(node.getId(), node); //for map->hashmap
        }

        public String toPlainText()
        {
            StringBuilder sb = new StringBuilder();
            traverse(head, sb); // traverse the whole tree to append the text to the string builder then turning it to a s tring
            return sb.toString(); //while ignoring deleted nodes
        }
///////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// helper functionss//////////////////////////////////////////
        private void traverse(CRDT_Node node, StringBuilder sb)
        {
            for (CRDT_Node nxt : node.getNext())  //hanmsek kol node w n3ady ala children depth first then elforloop ensures law andy kaza child ha3ady alehom bardo
            {
                if (!nxt.isDeleted()) sb.append(nxt.getValue()); // law mesh deleted hayhotaha gher keda ignore
                traverse(nxt, sb); //call it again for next child
            }
        }

        private CRDT_Node find_by_visiblePosition(int pos)
        {
            final int[] count = {-1}; //for head //array for recursion instead of normal int //for java ig
            return find_by_recursion(head, pos, count);
        }

        private CRDT_Node find_by_recursion(CRDT_Node node, int targetPos, int[] count)
        {
            for (CRDT_Node nxtnode : node.getNext())

            {
                if (!nxtnode.isDeleted())
                { count[0]++;} // ignoring deleted nodes (to get position elsah)
                if (count[0] == targetPos)
                {return nxtnode;}
                CRDT_Node found = find_by_recursion(nxtnode, targetPos, count);
                if (found != null)
                {return found;}
            }
            return node; //return last node w2efna andaha
        }
    }


