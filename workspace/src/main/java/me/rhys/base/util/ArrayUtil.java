package me.rhys.base.util;

public class ArrayUtil {

    private ArrayUtil() {
    }

    public static String[] offset(String[] array, int offset) {
        if (offset >= array.length) {
            return null;
        }

        String[] copy = new String[array.length - offset];
        if (array.length - offset >= 0)
            System.arraycopy(array, offset, copy, 0, array.length - offset);
        return copy;
    }
}
