package com.ariweiland.hyperoctahedral.partition;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class IntegerPartition {

    protected final int[] partition;
    private final int size;
    protected int length;
    protected boolean isComplete;

    public static IntegerPartition singletonPartition(int n) {
        return new IntegerPartition(new int[]{n});
    }

    public IntegerPartition(int size) {
        this(new int[size], size, 0, size == 0);
    }

    public IntegerPartition(int[] partition) {
        this(partition, sum(partition), partition.length, true);
    }

    public IntegerPartition(IntegerPartition p) {
        this(p.partition, p.size, p.length, p.isComplete);
    }

    protected IntegerPartition(int[] partition, int size, int length, boolean isComplete) {
        this.partition = partition;
        this.size = size;
        this.length = length;
        this.isComplete = isComplete;
    }

    public int[] getPartition() {
        int[] output = new int[length];
        System.arraycopy(partition, 0, output, 0, length);
        return output;
    }

    public int getSize() {
        return size;
    }

    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Returns the remainder of this partition that has not been partitioned yet.
     * Returns 0 for a complete partition.
     * @return
     */
    public int remainder() {
        return size - sum(partition);
    }

    /**
     * Tries to add a part i to an unfinished partition.
     * Returns whether or not the addition was successful.
     * Addition fails if 1 > i or i > remainder().
     * If the partition is complete (remainder() == 0), it always fails.
     * @param i
     * @return
     */
    public boolean addPart(int i) {
        int remainder = remainder();
        if (i < 1 || remainder < i) {
            return false;
        } else {
            partition[length] = i;
            length++;
            isComplete = (remainder == i);
            return true;
        }
    }

    /**
     * Completes an unfinished partition by filling it in with 1s.
     * Returns the number of 1s added.  If the partition is complete, returns 0.
     * @return
     */
    public int complete() {
        int remainder = remainder();
        for (int i=0; i<remainder; i++) {
            partition[length] = 1;
            length++;
        }
        isComplete = true;
        return remainder;
    }

    /**
     * Returns a new partition that is the inverse of this one.
     * @return
     */
    public IntegerPartition inverse() {
        if (!isComplete()) {
            throw new UnsupportedOperationException("Cannot invert an incomplete partition!");
        }
        IntegerPartition inverse = new IntegerPartition(size);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerPartition that = (IntegerPartition) o;

        return isComplete == that.isComplete && size == that.size
                && Arrays.equals(getPartition(), that.getPartition());

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getPartition());
        result = 31 * result + size;
        result = 31 * result + (isComplete ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(getPartition());
    }

    protected static int sum(int[] p) {
        int sum = 0;
        for (int i : p) {
            sum += i;
        }
        return sum;
    }
}
