package com.ariweiland.hyperoctahedral;

import com.ariweiland.hyperoctahedral.partition.IntegerPartition;
import com.ariweiland.hyperoctahedral.partition.SignedIntegerPartition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class MathUtils {

    public static int min(int... ints) {
        int min = ints[0];
        for (int i=1; i<ints.length; i++) {
            min = Math.min(min, ints[i]);
        }
        return min;
    }

    public static long factorial(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot take the factorial of a negative number: " + n);
        }
        long acc = 1;
        for (long i=2; i<=n; i++) {
            acc *= i;
        }
        return acc;
    }

    public static long binomial(long n, long k) {
        return factorial(n) / factorial(k) / factorial(n - k);
    }

    public static List<IntegerPartition> integerPartitions(int n) {
        return integerPartitionRecurse(n, n);
    }

    private static List<IntegerPartition> integerPartitionRecurse(int n, int max) {
        List<IntegerPartition> list = new ArrayList<>();
        if (n <= 0) {
            return Collections.singletonList(new IntegerPartition(0));
        } else {
            for (int i = 1; i <= Math.min(n, max); i++) {
                List<IntegerPartition> recurse = integerPartitionRecurse(n - i, i);
                for (IntegerPartition p : recurse) {
                    IntegerPartition p2 = new IntegerPartition(p.getSize() + i);
                    p2.addPart(i);
                    for (int j : p.getPartition()) {
                        p2.addPart(j);
                    }
                    list.add(p2);
                }
            }
        }
        return list;
    }

    public static List<SignedIntegerPartition> signedIntegerPartitions(int n) {
        List<SignedIntegerPartition> list = new ArrayList<>();
        for (int i=0; i<=n; i++) {
            List<IntegerPartition> positive = integerPartitions(n - i);
            List<IntegerPartition> negative = integerPartitions(i);
            for (IntegerPartition pos : positive) {
                for (IntegerPartition neg : negative) {
                    list.add(new SignedIntegerPartition(pos.getPartition(), neg.getPartition()));
                }
            }
        }
        return list;
    }
}
