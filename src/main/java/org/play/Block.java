package org.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

public record Block(Integer timestamp, byte[] previousBlockHash, byte[] myBlockHash, byte[] allData) {}