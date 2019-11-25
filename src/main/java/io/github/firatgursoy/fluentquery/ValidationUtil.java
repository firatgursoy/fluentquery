package io.github.firatgursoy.fluentquery;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
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

    static boolean isTrue(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    static boolean isNullOrZero(Comparable<Number> number) {
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

    /**
     * Convert a name in camelCase to an underscored name in lower case.
     * Any upper case letters are converted to lower case with a preceding underscore.
     *
     * @param name the original name
     * @return the converted name
     * @see #lowerCaseName
     */
    public static String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(lowerCaseName(name.substring(0, 1)));
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = lowerCaseName(s);
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

    /**
     * Convert the given name to lower case.
     * By default, conversions will happen within the US locale.
     *
     * @param name the original name
     * @return the converted name
     */
    public static String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }

    public static Class<?> findSuperClassParameterType(Object instance, Class<?> classOfInterest, int parameterIndex) {
        Class<?> subClass = instance.getClass();
        while (subClass != subClass.getSuperclass()) {
            // instance.getClass() is no subclass of classOfInterest or instance is a direct instance of classOfInterest
            subClass = subClass.getSuperclass();
            if (subClass == null) throw new IllegalArgumentException();
        }
        ParameterizedType parameterizedType = (ParameterizedType) subClass.getGenericSuperclass();
        return (Class<?>) parameterizedType.getActualTypeArguments()[parameterIndex];
    }

    public static Type getFirstGenericTypeArgument(Class clazz, String field) {
        Type actualTypeArgument = null;
        try {
            actualTypeArgument = ((ParameterizedTypeImpl) clazz.getDeclaredField(field).getGenericType()).getActualTypeArguments()[0];
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return actualTypeArgument;
    }

    /**
     * Retrieve a JDBC object value for the specified column.
     * <p>The default implementation calls
     * {@link JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)}.
     * Subclasses may override this to check specific value types upfront,
     * or to post-process values return from {@code getResultSetValue}.
     *
     * @param rs    is the ResultSet holding the data
     * @param index is the column index
     * @param pd    the bean property that each result object is expected to match
     * @return the Object value
     * @throws SQLException in case of extraction failure
     * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)
     */

    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

}
