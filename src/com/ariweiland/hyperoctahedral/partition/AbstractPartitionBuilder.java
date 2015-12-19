package com.ariweiland.hyperoctahedral.partition;

/**
 * This class is a superclass for the other PartitionBuilders.
 * @author Ari Weiland
 */
public abstract class AbstractPartitionBuilder<T extends AbstractPartition> {

    private final int size;
    private boolean isComplete;

    public AbstractPartitionBuilder(int size, boolean isComplete) {
        this.size = size;
        this.isComplete = isComplete;
    }

    /**
     * Returns the size of the partition under construction.
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns whether or not the partition under construction is complete.
     * @return
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Returns the remainder of this partition that has not been partitioned yet.
     * Returns 0 for a complete partition.
     * @return
     */
    public abstract int remainder();

    /**
     * Tries to add a part i to an unfinished partition.
     * Returns whether or not the addition was successful.
     * Addition fails if 1 > i or i > remainder().
     * If the partition is complete (remainder() == 0), it always fails.
     * @param i
     * @return
     */
    public abstract boolean addPart(int i);

    protected boolean addPart(int i, int[] p, int length) {
        int remainder = remainder();
        if (i < 1 || remainder < i) {
            return false;
        } else {
            p[length] = i;
            this.isComplete = (remainder == i);
            return true;
        }
    }

    public abstract T build();
}
