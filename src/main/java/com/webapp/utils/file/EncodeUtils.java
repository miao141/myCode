package com.webapp.utils.file;

import com.webapp.utils.string.Utils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class EncodeUtils {
    public static String decode(ByteBuffer buffer, String charsetName) {
        return Charset.forName(charsetName).decode((ByteBuffer)buffer.rewind()).toString();
    }

    public static String decode(ByteBuffer buffer) {
        return EncodeUtils.decode(buffer, Utils.Charsets.uft8);
    }

    public static String decode(String str) {
        return EncodeUtils.decode(str, Utils.Charsets.uft8);
    }

    public static String decode(String str, String charsetName) {
        return EncodeUtils.decode(ByteBuffer.wrap(str.getBytes()), charsetName);
    }

    public static String decode(CharBuffer buffer) {
        return EncodeUtils.decode(buffer, Utils.Charsets.uft8);
    }

    public static String decode(CharBuffer buffer, String charsetName) {
        return EncodeUtils.decode(buffer.toString());
    }

    public static String decode(byte[] byteArray) {
        return EncodeUtils.decode(ByteBuffer.wrap(byteArray), Utils.Charsets.uft8);
    }

    public static String decode(byte[] byteArray, String charsetName) {
        return EncodeUtils.decode(ByteBuffer.wrap(byteArray), charsetName);
    }

    public static ByteBuffer encode(String str) {
        return EncodeUtils.encode(str, Utils.Charsets.uft8);
    }

    public static ByteBuffer encode(String str, String charsetName) {
        return Charset.forName(charsetName).encode(str);
    }

    public static ByteBuffer encode(CharBuffer buffer) {
        return EncodeUtils.encode(buffer, Utils.Charsets.uft8);
    }

    public static ByteBuffer encode(CharBuffer buffer, String charsetName) {
        return Charset.forName(charsetName).encode((CharBuffer)buffer.rewind());
    }

    public static ByteBuffer encode(byte[] byteArray) {
        return ByteBuffer.wrap(byteArray);
    }

    public static byte[] encodeByte(ByteBuffer buffer) {
        return EncodeUtils.decode(buffer).getBytes();
    }

    public static Charset getUTFCharset() {
        return StandardCharsets.UTF_8;
    }

    public static Charset getDefaultCharset() {
        return Charset.defaultCharset();
    }

    private EncodeUtils() {
    }
}
