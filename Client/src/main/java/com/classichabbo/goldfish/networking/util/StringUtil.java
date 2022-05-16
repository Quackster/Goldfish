package com.classichabbo.goldfish.networking.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringUtil {

    /**
     * Get encoding for strings
     *
     * @return the encoding
     */
    public static Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
