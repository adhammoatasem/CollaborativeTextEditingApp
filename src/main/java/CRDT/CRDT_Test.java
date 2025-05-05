//package CRDT;

package CRDT;

public class CRDT_Test {
    public static void main(String[] args) {
       CRDT_Document doc = new CRDT_Document();
//
        System.out.println("=== Local Insertion Test ===");
       // Identifier id0 = doc.localInsert(-1, 'r');
        Identifier id1 = doc.localInsert(0, 'j');
        Identifier id2 = doc.localInsert(1, 'a');
        Identifier id3 = doc.localInsert(2, 'l');
        System.out.println("Expected: jal");
        System.out.println("Actual  : " + doc.toString());
//
        System.out.println("\n=== Remote Insert Test ===");
        // Simulate a remote insert
        Identifier remotePrevId = id1; // Insert after 'a'
        Identifier remoteNewId = new Identifier("remote_user",1 ); // Simulated remote identifier
        char remoteChar = 'x'; // Character to insert
    System.out.print("inserting now");
        // Perform the remote insert
        doc.remoteInsert(remotePrevId, remoteNewId, remoteChar);

        System.out.println("Expected: jxal");
        System.out.println("Actual  : " + doc.toString());

        // Print identifiers for debugging
        System.out.println("\n=== Identifiers After Remote Insert ===");
        doc.printIdentifiers();
        /// //////////////////////////////////////////////////////
//        System.out.println("\n=== Remote Insert Test ===");
//// Simulate a remote insert at the very beginning
//        Identifier remotePrevId = doc.getHeadId(); // <- use head's ID
//        Identifier remoteNewId = new Identifier("remote_user", 0); // Simulated remote identifier
//        char remoteChar = 'r'; // Character to insert

// Perform the remote insert
//        doc.remoteInsert(remotePrevId, remoteNewId, remoteChar);
//
//        System.out.println("Expected: rjal");
//        System.out.println("Actual  : " + doc.toString());

    }
}
//
//public class CRDT_Test {
////    public static void main(String[] args) {
////        CRDT_Document doc = new CRDT_Document();
////        //Identifier user = new Identifier();
////        System.out.println("=== Insertion Test ===");
////        Identifier id1 =doc.localInsert(0, 'j');
////        Identifier id2 = doc.localInsert(1, 'a');
////        Identifier id3 = doc.localInsert(2, 'l');
//////
////        System.out.println("Expected: jal");
////        System.out.println("Actual  : " + doc.toString());
////
////
////        System.out.println("\n=== Last Test ===");
////        doc.localInsert(0, 'm');
////        System.out.println("Expected: mjal");
////        System.out.println("Actual  : " + doc.toString());
////        System.out.println("\n=== Last Test ===");
////        doc.localInsert(6, 'y');
////        System.out.println("Expected: mjal  y");
////        System.out.println("Actual  : " + doc.toString());
////        doc.printIdentifiers();
////
////
////    }
//
//
//
//        public static void main(String[] args) {
//            CRDT_Document doc = new CRDT_Document();
//
//            System.out.println("=== Local Insertion Test ===");
//            Identifier id1 = doc.localInsert(0, 'j');
//            Identifier id2 = doc.localInsert(1, 'a');
//            Identifier id3 = doc.localInsert(2, 'l');
//            System.out.println("Expected: jal");
//            System.out.println("Actual  : " + doc.toString());
//
//            System.out.println("\n=== Remote Insert Test ===");
//            // Simulate a remote insert
//            Identifier remotePrevId = id2; // Insert after 'a'
//            Identifier remoteNewId = new Identifier("remote_user", 1); // Simulated remote identifier
//            char remoteChar = 'x'; // Character to insert
//
//            // Perform the remote insert
//            doc.remoteInsert(remotePrevId, remoteNewId, remoteChar);
//
//            System.out.println("Expected: jaxl");
//            System.out.println("Actual  : " + doc.toString());
//
//            // Print identifiers for debugging
//            System.out.println("\n=== Identifiers After Remote Insert ===");
//            doc.printIdentifiers();
//        }
//
//}
