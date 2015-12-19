package com.ariweiland.hyperoctahedral;

/**
 * @author Ari Weiland
 */
public class Utils {

    public static int[] cleanArray(int[] array, int length) {
        int[] output = new int[length];
        System.arraycopy(array, 0, output, 0, length);
        return output;
    }

    public static int sum(int[] p) {
        int sum = 0;
        for (int i : p) {
            sum += i;
        }
        return sum;
    }

    public static int min(int... ints) {
        int min = ints[0];
        for (int i=1; i<ints.length; i++) {
            min = Math.min(min, ints[i]);
        }
        return min;
    }
}
