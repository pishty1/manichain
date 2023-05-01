package org.play;

import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@Getter @Setter
public class Node {
    private ArrayList<Block> blockchain;
    public static final int DIFFICULTY = 4;
    public Node() {
        this.blockchain = new ArrayList<>();
        this.blockchain.add(createGenesisBlock());
    }


    public static String bytesToHex(byte[] myBlockHash) {
        StringBuilder hexString = new StringBuilder(2 * myBlockHash.length);
        for (int i = 0; i < myBlockHash.length; i++) {
            String hex = Integer.toHexString(0xff & myBlockHash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public Block createGenesisBlock() {
        byte[] data = "Hello Genesis".getBytes();
        byte[] previousHash = new byte[0];
        byte[] firstBlockHash = findNewValidHash(previousHash, data);
        return newBlock(data, new byte[0], firstBlockHash);
    }
    public static Block newBlock(byte[] data, byte[] previousBlockHash, byte[] newHash) {
        return new Block((int) System.currentTimeMillis(), previousBlockHash, newHash, data);
    }
    public void addBlock(byte[] data) {
        // let's get the previous block
        Block previousBlock = getLastBlock();
        byte[] newHash = findNewValidHash( previousBlock.thisBlockHash(), data);
        Block newBlock = newBlock(data, previousBlock.thisBlockHash(), newHash);
        blockchain.add(newBlock);
    }
    public Block getLastBlock() {
        return blockchain.get(blockchain.size() - 1);
    }
    public byte[] findNewValidHash(byte[] previousBlockHash, byte[] data) {
        String diffString = new String(new char[DIFFICULTY]).replace("\0", "0");
        boolean found;
        byte[] newRandomHash;
        int nonce = 0;
        do {
            int timeStamp = (int) System.currentTimeMillis();
            newRandomHash = getHash(nonce, timeStamp, previousBlockHash, data);
            nonce++;
            found = bytesToHex(newRandomHash).startsWith(diffString);
        } while(!found);
        System.out.printf("found hash %s\n", bytesToHex(newRandomHash));

        return newRandomHash;
    }
    public byte[] getHash(int nonce, Integer timestamp, byte[] previousBlockHash, byte[] allData) {
        byte[] tempTimestamp = timestamp.toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream headers = new ByteArrayOutputStream();
        byte[] digest;
        try {
            headers.write(tempTimestamp);
            headers.write(previousBlockHash);
            headers.write(allData);
            headers.write(nonce);
            byte[] byteArray = headers.toByteArray();
            MessageDigest sha256Digest;
            try {
                sha256Digest = MessageDigest.getInstance("SHA-256");
                // mine until you get a hash with the right difficulty
                digest = sha256Digest.digest(byteArray);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return digest;
    }
}
