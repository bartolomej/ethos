package util;

public class StringUtil {

    public static String repeat(String pattern, int times) {
        String repeatedPattern = "";
        for (int i = 0; i < times; i++)
            repeatedPattern += pattern;
        return repeatedPattern;
    }
}
