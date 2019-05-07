package util;

public class BytesUtil {

    private static char[] hex = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String toHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i*2] = hex[v/16];
            hexChars[i*2 + 1] = hex[v%16];
        }
        return new String(hexChars);
    }

    public static byte[] toByteArray(String hexString) {
        byte[] bytes = new byte[hexString.length()/2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i/2] = (byte)Integer.parseInt(hexString.substring(i, i+2), 16);
        }
        return bytes;
    }
}
