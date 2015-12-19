package com.ariweiland.hyperoctahedral.partition;

import com.ariweiland.hyperoctahedral.Utils;

/**
 * This class is useful for building an IntegerPartition. It used to be that this functionality
 * was built into IntegerPartition, but this method is better so that IntegerPartition is fully
 * immutable. Plus, this is better object-oriented code.
 * @author Ari Weiland
 */
public class IntegerPartitionBuilder extends AbstractPartitionBuilder<IntegerPartition> {

    private final int[] partition;
    private int length;

    public IntegerPartitionBuilder(int size) {
        super(size, size == 0);
        this.partition = new int[size];
        this.length = 0;
    }

    @Override
    public int remainder() {
        return getSize() - Utils.sum(partition);
    }

    @Override
    public boolean addPart(int i) {
        boolean output = addPart(i, partition, length);
        if (output) length++;
        return output;
    }

    @Override
    public IntegerPartition build() {
        while (!isComplete()) {
            addPart(1);
        }
        return new IntegerPartition(Utils.cleanArray(partition, length));
    }
}
