package com.ariweiland.hyperoctahedral.partition;

import com.ariweiland.hyperoctahedral.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class IntegerPartition extends AbstractPartition implements Comparable<IntegerPartition> {

    private final int[] partition;

    /**
     * Constructs an IntegerPartition of size 0.
     */
    public IntegerPartition() {
        this(new int[0]);
    }

    /**
     * Constructs an IntegerPartition from the input array.
     * @param partition
     */
    public IntegerPartition(int[] partition) {
        super(Utils.sum(partition));
        this.partition = partition;
    }

    @Override
    public int[] getPartition() {
        return partition;
    }

    @Override
    public IntegerPartition inverse() {
        IntegerPartitionBuilder builder = new IntegerPartitionBuilder(getSize());
        int i = partition.length - 1;
        int j = 1;
        while (i >= 0) {
            if (partition[i] >= j) {
                builder.addPart(i + 1);
                j++;
            } else {
                i--;
            }
        }
        return builder.build();
    }

    @Override
    public int compareTo(IntegerPartition o) {
        // compare length
        int compare = -Integer.compare(partition.length, o.partition.length);
        if (compare != 0) {
            return compare;
        }
        // compare lexicographically
        for (int i=0; i<partition.length; i++) {
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

    /**
     * Returns all IntegerPartitions in sorted order.
     * @param n
     * @return
     */
    public static List<IntegerPartition> all(int n) {
        List<IntegerPartition> list = generator(n, n);
        Collections.sort(list);
        return list;
    }

    private static List<IntegerPartition> generator(int n, int max) {
        List<IntegerPartition> list = new ArrayList<>();
        if (n <= 0) {
            return Collections.singletonList(new IntegerPartition());
        } else {
            for (int i = 1; i <= Utils.min(n, max); i++) {
                List<IntegerPartition> recurse = generator(n - i, i);
                for (IntegerPartition p : recurse) {
                    IntegerPartitionBuilder builder = new IntegerPartitionBuilder(p.getSize() + i);
                    builder.addPart(i);
                    for (int j : p.getPartition()) {
                        builder.addPart(j);
                    }
                    list.add(builder.build());
                }
            }
        }
        return list;
    }
}
