package util;

import java.util.Base64;

public class ByteUtil {

    private static char[] hex = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    // TODO: replace with base64 encoding
    public static String encodeToBase64(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static byte[] decodeFromBase64(String input) {
        return Base64.getDecoder().decode(input);
    }

    public static String toHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i*2] = hex[v/16];
            hexChars[i*2 + 1] = hex[v%16];
        }
        return new String(hexChars);
    }

    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] concatenated = new byte[array1.length + array2.length];
        int counter = 0;
        for (int i = 0; i < concatenated.length; i++) {
            if (i < array1.length) {
                concatenated[i] = array1[counter];
            }
            if (i == array1.length) {
                counter = 0;
            }
            if (i >= array1.length) {
                concatenated[i] = array2[counter];
            }
            counter++;
        }
        return concatenated;
    }

    public static byte[] toByteArray(String hexString) {
        byte[] bytes = new byte[hexString.length()/2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i/2] = (byte)Integer.parseInt(hexString.substring(i, i+2), 16);
        }
        return bytes;
    }

    public static boolean arraysEqual(byte[] array1, byte[] array2) {
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array1[i]) return false;
        }
        return true;
    }
}
