package com.ariweiland.hyperoctahedral.partition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class IntegerPartition extends AbstractPartition implements Comparable<IntegerPartition> {

    private final int[] partition;
    private int length;

    public IntegerPartition(int size) {
        this(new int[size], size, 0, size == 0);
    }

    public IntegerPartition(int[] partition) {
        this(partition, sum(partition), partition.length, true);
    }

    private IntegerPartition(int[] partition, int size, int length, boolean isComplete) {
        super(size, isComplete);
        this.partition = partition;
        this.length = length;
    }

    public int[] getPartition() {
        return cleanArray(partition, length);
    }

    @Override
    public int remainder() {
        return getSize() - sum(partition);
    }

    @Override
    public boolean addPart(int i) {
        boolean output = addPart(i, partition, length);
        if (output) length++;
        return output;
    }

    @Override
    public IntegerPartition inverse() {
        if (!isComplete()) {
            throw new UnsupportedOperationException("Cannot invert an incomplete partition!");
        }
        IntegerPartition inverse = new IntegerPartition(getSize());
        int i = length - 1;
        int j = 1;
        while (i >= 0) {
            if (partition[i] >= j) {
                inverse.addPart(i + 1);
                j++;
            } else {
                i--;
            }
        }
        return inverse;
    }

    @Override
    public int compareTo(IntegerPartition o) {
        if (!isComplete() || !o.isComplete()) {
            throw new UnsupportedOperationException("Cannot compare incomplete partitions!");
        }
        // compare length
        int compare = -Integer.compare(length, o.length);
        if (compare != 0) {
            return compare;
        }
        // compare lexicographically
        for (int i=0; i<length; i++) {
            compare = Integer.compare(partition[i], o.partition[i]);
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IntegerPartition that = (IntegerPartition) o;

        return Arrays.equals(getPartition(), that.getPartition());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(partition);
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(getPartition());
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
        Collections.sort(list);
        return list;
    }
}
