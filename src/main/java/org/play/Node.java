package org.play;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

public class Node {
    private final ArrayList<Block> blockchain;
    public static final int DIFFICULTY = 4;

    public Node() {
        this.blockchain = new ArrayList<>();
        this.blockchain.add(createGenesisBlock());
    }

    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }

    public static String bytesToString(byte[] myBlockHash) {
        StringBuilder hexString = new StringBuilder(2 * myBlockHash.length);
        for (byte blockHash : myBlockHash) {
            String hex = Integer.toHexString(0xff & blockHash);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public Block createGenesisBlock() {
        byte[] data = "Hello Genesis".getBytes();
        byte[] previousHash = new byte[0];
        Map.Entry<byte[], Integer> newValidHash = getValidHash(previousHash, data);
        return newBlock(data, new byte[0], newValidHash.getKey(), newValidHash.getValue());
    }

    public static Block newBlock(byte[] data, byte[] previousBlockHash, byte[] newHash, int nonce) {
        return new Block((int) System.currentTimeMillis(), previousBlockHash, newHash, data, nonce);
    }

    public void addBlock(byte[] data) {
        // let's get the previous block
        Block previousBlock = getLastBlock();
        Map.Entry<byte[], Integer> newValidHash = getValidHash(previousBlock.thisBlockHash(), data);
        Block newBlock = newBlock(data, previousBlock.thisBlockHash(), newValidHash.getKey(), newValidHash.getValue());
        blockchain.add(newBlock);
    }

    public Block getLastBlock() {
        return blockchain.get(blockchain.size() - 1);
    }

    public Map.Entry<byte[], Integer> getValidHash(byte[] previousBlockHash, byte[] data) {
        String diffString = new String(new char[DIFFICULTY]).replace("\0", "0");
        byte[] newRandomHash;
        int nonce = 0;
        do {
            int timeStamp = (int) System.currentTimeMillis();
            newRandomHash = getHash(nonce, timeStamp, previousBlockHash, data);
            nonce++;
        } while (!bytesToString(newRandomHash).startsWith(diffString));
        System.out.printf("found hash %s\n", bytesToString(newRandomHash));
        return new AbstractMap.SimpleEntry<>(newRandomHash, nonce);
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
