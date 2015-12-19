package com.ariweiland.hyperoctahedral.partition;

import com.ariweiland.hyperoctahedral.Utils;

/**
 * This class is useful for building a SignedIntegerPartition. It used to be that this functionality
 * was built into SignedIntegerPartition, but this method is better so that SignedIntegerPartition
 * is fully immutable. Plus, this is better object-oriented code.
 * @author Ari Weiland
 */
public class SignedIntegerPartitionBuilder extends AbstractPartitionBuilder<SignedIntegerPartition> {

    private final int[] positive;
    private final int[] negative;
    private int posLen;
    private int negLen;

    public SignedIntegerPartitionBuilder(int size) {
        super(size, size == 0);
        this.positive = new int[size];
        this.negative = new int[size];
        this.posLen = 0;
        this.negLen = 0;
    }

    @Override
    public int remainder() {
        return getSize() - Utils.sum(positive) - Utils.sum(negative);
    }

    @Override
    public boolean addPart(int i) {
        boolean output;
        if (i < 0) {
            output = addPart(-i, negative, negLen);
            if (output) negLen++;
        } else {
            output = addPart(i, positive, posLen);
            if (output) posLen++;
        }
        return output;
    }

    @Override
    public SignedIntegerPartition build() {
        while (!isComplete()) {
            addPart(1);
        }
        return new SignedIntegerPartition(Utils.cleanArray(positive, posLen), Utils.cleanArray(negative, negLen));
    }
}
