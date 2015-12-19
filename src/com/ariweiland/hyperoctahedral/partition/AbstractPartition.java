package com.ariweiland.hyperoctahedral.partition;

/**
 * @author Ari Weiland
 */
public abstract class AbstractPartition {

    private final int size;
    private boolean isComplete;

    public AbstractPartition(int size, boolean isComplete) {
        this.size = size;
        this.isComplete = isComplete;
    }

    public int getSize() {
        return size;
    }

    public boolean isComplete() {
        return isComplete;
    }

    protected void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
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

    /**
     * Returns a new partition that is the inverse of this one.
     * @return
     */
    public abstract AbstractPartition inverse();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPartition that = (AbstractPartition) o;

        return size == that.size;
    }

    @Override
    public int hashCode() {
        return size;
    }

    protected static int[] cleanArray(int[] array, int length) {
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
}
