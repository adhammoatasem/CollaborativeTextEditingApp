package CRDT;
import java.util.HashMap;
import java.util.Map;
public class CRDT_Document
{

       private final String Userid;
        private long DocCounter = 0;


        private final CRDT_Node head; // the first node dummy lowkey
        private final Map<Identifier, CRDT_Node> index = new HashMap<>();

        public CRDT_Document(String user) {
            this.Userid = user;
            this.head = new CRDT_Node('\0', new Identifier(user, -1)); // Dummy head
            index.put(head.getId(), head);
        }

        private Identifier nextId() {
            return new Identifier(Userid, DocCounter++); //userA rozana r->(userA,1), o->(userA,2)
        }
    /// /////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////continue from heree 20/4/2025/////////////////////////////////////////
    ///  don't forget
    /// /(insert(char value, int position, String userId)
    /// 	•	delete(int position, String userId)
    /// 	•	merge(List<RemoteEdit>)
    /// 	•	String toPlainText())
        public Identifier localInsert(int position, char c) {
            CRDT_Node prev = findByVisiblePosition(position - 1);
            Identifier newId = nextId();
            CRDT_Node node = new CRDT_Node(c, newId);
            addNode(prev, node);
            return newId;
        }

        public void remoteInsert(Identifier prevId, Identifier newId, char c) {
            CRDT_Node prev = index.get(prevId);
            if (prev == null) return;
            CRDT_Node node = new CRDT_Node(c, newId);
            addNode(prev, node);
        }

        public void delete(Identifier id) {
            CRDT_Node node = index.get(id);
            if (node != null) node.delete();
        }

        private void addNode(CRDT_Node prev, CRDT_Node node) {
            prev.insertNext(node);
            index.put(node.getId(), node);
        }

        public String toPlainText() {
            StringBuilder sb = new StringBuilder();
            traverse(head, sb);
            return sb.toString();
        }

        private void traverse(CRDT_Node node, StringBuilder sb) {
            for (CRDT_Node nxt : node.getNext()) {
                if (!nxt.isDeleted()) sb.append(nxt.getValue());
                traverse(nxt, sb);
            }
        }

        private CRDT_Node findByVisiblePosition(int pos) {
            final int[] count = {-1};
            return findRecursive(head, pos, count);
        }

        private CRDT_Node findRecursive(CRDT_Node node, int targetPos, int[] count) {
            for (CRDT_Node nxt : node.getNext()) {
                if (!nxt.isDeleted()) count[0]++;
                if (count[0] == targetPos) return nxt;
                CRDT_Node found = findRecursive(nxt, targetPos, count);
                if (found != null) return found;
            }
            return node;
        }
    }


