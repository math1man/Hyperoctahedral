package com.ariweiland.hyperoctahedral.partition;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class SignedIntegerPartition extends IntegerPartition implements Comparable<SignedIntegerPartition> {

    private final int[] negative;
    private int negLen;

    public SignedIntegerPartition(int size) {
        super(size);
        this.negative = new int[size];
        this.negLen = 0;
    }

    public SignedIntegerPartition(int[] positive, int[] negative) {
        this(positive, negative, sum(positive) + sum(negative), positive.length, negative.length, true);
    }

    public SignedIntegerPartition(SignedIntegerPartition sip) {
        this(sip.partition, sip.negative, sip.getSize(), sip.length, sip.negLen, sip.isComplete());
    }

    protected SignedIntegerPartition(int[] positive, int[] negative, int size, int posLen, int negLen, boolean isComplete) {
        super(positive, size, posLen, isComplete);
        this.negative = negative;
        this.negLen = negLen;
    }

    public int[] getPositive() {
        return getPartition();
    }

    public int[] getNegative() {
        return negativePartition().getPartition();
    }

    protected IntegerPartition negativePartition() {
        return new IntegerPartition(negative, getSize() - sum(partition), negLen, isComplete());
    }

    @Override
    public int remainder() {
        return super.remainder() - sum(negative);
    }

    @Override
    public boolean addPart(int i) {
        if (i < 0) {
            boolean output = negativePartition().addPart(-i);
            negLen += (output ? 1 : 0);
            isComplete = (remainder() == 0);
            return output;
        } else {
            return super.addPart(i);
        }
    }

    public int complete(boolean positive) {
        if (positive) {
            return super.complete();
        } else {
            int output = negativePartition().complete();
            negLen += output;
            isComplete = true;
            return output;
        }
    }

    public SignedIntegerPartition inverse() {
        if (!isComplete()) {
            throw new UnsupportedOperationException("Cannot invert an incomplete partition!");
        }
        SignedIntegerPartition inverse = new SignedIntegerPartition(getSize());
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
        i = negLen - 1;
        j = 1;
        while (i >= 0) {
            if (negative[i] >= j) {
                inverse.addPart(-(i + 1));
                j++;
            } else {
                i--;
            }
        }
        return inverse;
    }

    public SignedIntegerPartition reverse() {
        if (!isComplete()) {
            throw new UnsupportedOperationException("Cannot invert an incomplete partition!");
        }
        return new SignedIntegerPartition(getNegative(), getPositive());
    }

    @Override
    public int compareTo(SignedIntegerPartition o) {
        if (!isComplete() || !o.isComplete()) {
            throw new UnsupportedOperationException("Cannot compare incomplete partitions!");
        }
        // compare total negative
        int compare = Integer.compare(sum(negative), sum(o.negative));
        if (compare != 0) {
            return compare;
        }
        // compare negative length or positive length
        if (sum(partition) < sum(negative)) {
            compare = -Integer.compare(length, o.length);
        } else {
            compare = Integer.compare(negLen, o.negLen);
        }
        if (compare != 0) {
            return compare;
        }
        // compare the other
        if (sum(partition) < sum(negative)) {
            compare = Integer.compare(negLen, o.negLen);
        } else {
            compare = -Integer.compare(length, o.length);
        }
        if (compare != 0) {
            return compare;
        }
        // compare largest element
        for (int i=0; i<length; i++) {
            compare = Integer.compare(partition[i], o.partition[i]);
            if (compare != 0) {
                return compare;
            }
        }
        for (int i=0; i<negLen; i++) {
            compare = -Integer.compare(negative[i], o.negative[i]);
            if (compare != 0) {
                return compare;
            }
        }

        if (negLen == 0 || length > 0 && partition[0] > negative[0]) {
            compare = Integer.compare(partition[0], o.partition[0]);
        } else {
            compare = -Integer.compare(negative[0], o.negative[0]);
        }
        if (compare != 0) {
            return compare;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SignedIntegerPartition that = (SignedIntegerPartition) o;

        return Arrays.equals(getNegative(), that.getNegative());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(getNegative());
        return result;
    }

    @Override
    public String toString() {
        return "{" + super.toString() + ", " + Arrays.toString(getNegative()) + "}";
    }
}
