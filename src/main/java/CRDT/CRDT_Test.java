package CRDT;

public class CRDT_Test {
    public static void main(String[] args) {
        CRDT_Document doc = new CRDT_Document();
        //Identifier user = new Identifier();
        System.out.println("=== Insertion Test ===");
        Identifier id1 =doc.localInsert(0, 'j');
        Identifier id2 = doc.localInsert(1, 'a');
        Identifier id3 = doc.localInsert(2, 'l');
        doc.localInsert(3, 'a');
        doc.localInsert(4, 'p');
        doc.localInsert(5, 'o');
        System.out.println("Expected: jalapo");
        System.out.println("Actual  : " + doc.toString());

        System.out.println("\n=== Deletion Test ===");
        System.out.println("Expected: jalapo");
        System.out.println("Actual  : " + doc.toString());
       doc.delete(id1);
        System.out.println("Expected: alapo");
        System.out.println("Actual  : " + doc.toString());
//        System.out.println("\n=== Deletion Test ===");
//        doc.delete(new Identifier(2); // delete the first 'l'
//        System.out.println("Expected: Helo");
//        System.out.println("Actual  : " + doc.toString());

        System.out.println("\n=== Undo Test ===");
        doc.undo(); // should undo delete
        System.out.println("Expected: jalapo");
        System.out.println("Actual  : " + doc.toString());

        System.out.println("\n=== Redo Test ===");
        doc.redo(); // should redo delete
        System.out.println("Expected: alapo");
        System.out.println("Actual  : " + doc.toString());

        System.out.println("\n=== Last Test ===");
        doc.localInsert(2, 'm');
        System.out.println("Expected: almapo");
        System.out.println("Actual  : " + doc.toString());
//        doc.localInsert(1, 'm');
//        System.out.println("Expected: mmalapo");
//        System.out.println("Actual  : " + doc.toString());

        System.out.println("\n=== File Export Test ===");
        String filePath = "test_export.txt";
        doc.exportToFile(filePath);
        System.out.println("Exported to: " + filePath);

        System.out.println("\n=== File Import Test ===");
        CRDT_Document importedDoc = new CRDT_Document();
        importedDoc.importFromFile(filePath);
        System.out.println("Expected: jalapo");
        System.out.println("Actual  : " + importedDoc.toString());
    }
}
