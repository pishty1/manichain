package org.play;

public record Block(Integer timestamp, byte[] previousBlockHash, byte[] thisBlockHash, byte[] allData, int nonce) {}