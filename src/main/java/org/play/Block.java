package org.play;

public record Block(Integer timestamp, byte[] previousBlockHash, byte[] myBlockHash, byte[] allData) {}