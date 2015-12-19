package com.ariweiland.hyperoctahedral.partition;

/**
 * A superclass for IntegerPartition and SignedIntegerPartition, detailing a few methods and members
 * that both classes use. Partitions should be instantiated from a full int array, or with an
 * initial size so that they can be assembled by adding parts.
 * @author Ari Weiland
 */
public abstract class AbstractPartition {

    private final int size;

    public AbstractPartition(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public abstract int[] getPartition();

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

}
