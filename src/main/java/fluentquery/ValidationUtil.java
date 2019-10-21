package fluentquery;

import java.util.Collection;

class ValidationUtil {
    static boolean isBlank(final CharSequence cs) {
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

    static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }
}
