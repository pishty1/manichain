package org.play;

// Press ⇧ twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) {

        Node node = new Node();

        int numberOfTestBlocks = 5;
        for (int i = 1; i <= numberOfTestBlocks; i++) {
            node.addBlock(("Test data " + i).getBytes());
        }

        node.getBlockchain().forEach(x -> {
            String bHash = Node.bytesToHex(x.myBlockHash());
            String bPHash = Node.bytesToHex(x.previousBlockHash());
            System.out.printf("\nblockhash: %s, previous Blockhash: %s %n", bHash,
                    bPHash);
        });

    }
}