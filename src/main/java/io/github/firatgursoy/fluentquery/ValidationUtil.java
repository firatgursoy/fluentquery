package io.github.firatgursoy.fluentquery;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNullOrZero(Comparable<Number> number) {
        if (number == null) {
            return false;
        }
        return number.compareTo(0) == 0;
    }

    protected static List<String> extractParameterKeys(String sqlPart) {
        List<String> keys = new ArrayList<>();
        Pattern compile = Pattern.compile("(:[\\s\\S])\\w+");
        Matcher matcher = compile.matcher(sqlPart);
        while (matcher.find()) {
            String group = matcher.group();
            keys.add(group.replace(":", ""));
        }
        return keys;
    }


    public static <T> T instantiateClass(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
