package com.classichabbo.goldfish.networking.util;

import org.apache.commons.codec.binary.Hex;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class StringUtil {
    public static Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    public static String createHash(String toHash) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        messageDigest.reset();
        messageDigest.update(toHash.getBytes(StandardCharsets.UTF_8));
        final byte[] resultByte = messageDigest.digest();
        final String result = new String(Hex.encodeHex(resultByte));
        return result;
    }

}
