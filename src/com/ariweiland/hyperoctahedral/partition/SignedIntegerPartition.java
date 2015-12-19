package com.ariweiland.hyperoctahedral.partition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class SignedIntegerPartition extends AbstractPartition implements Comparable<SignedIntegerPartition> {

    private final int[] positive;
    private final int[] negative;
    private int posLen;
    private int negLen;


    public SignedIntegerPartition(int size) {
        this(size, new int[size], new int[size], 0, 0, false);
    }

    public SignedIntegerPartition(int[] positive, int[] negative) {
        this(sum(positive) + sum(negative), positive, negative, positive.length, negative.length, true);
    }

    private SignedIntegerPartition(int size, int[] positive, int[] negative, int posLen, int negLen, boolean isComplete) {
        super(size, isComplete);
        this.positive = positive;
        this.negative = negative;
        this.posLen = posLen;
        this.negLen = negLen;
    }

    public int[] getPositive() {
        return cleanArray(positive, posLen);
    }

    public int[] getNegative() {
        return cleanArray(negative, negLen);
    }

    @Override
    public int remainder() {
        return getSize() - sum(positive) - sum(negative);
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
    public SignedIntegerPartition inverse() {
        if (!isComplete()) {
            throw new UnsupportedOperationException("Cannot invert an incomplete partition!");
        }
        IntegerPartition pos = new IntegerPartition(getPositive()).inverse();
        IntegerPartition neg = new IntegerPartition(getNegative()).inverse();
        return new SignedIntegerPartition(pos.getPartition(), neg.getPartition());
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
        if (sum(positive) < sum(negative)) {
            compare = -Integer.compare(posLen, o.posLen);
        } else {
            compare = Integer.compare(negLen, o.negLen);
        }
        if (compare != 0) {
            return compare;
        }
        // compare the other
        if (sum(positive) < sum(negative)) {
            compare = Integer.compare(negLen, o.negLen);
        } else {
            compare = -Integer.compare(posLen, o.posLen);
        }
        if (compare != 0) {
            return compare;
        }
        // lexicographic sort
        for (int i=0; i<posLen; i++) {
            compare = Integer.compare(positive[i], o.positive[i]);
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
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SignedIntegerPartition that = (SignedIntegerPartition) o;

        return Arrays.equals(getNegative(), that.getNegative()) && Arrays.equals(getPositive(), that.getPositive());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(positive);
        result = 31 * result + Arrays.hashCode(negative);
        return result;
    }

    @Override
    public String toString() {
        return "{" + Arrays.toString(getPositive()) + ", " + Arrays.toString(getNegative()) + "}";
    }

    public static List<SignedIntegerPartition> signedIntegerPartitions(int n) {
        List<SignedIntegerPartition> list = new ArrayList<>();
        for (int i=0; i<=n; i++) {
            List<IntegerPartition> positive = IntegerPartition.integerPartitions(n - i);
            List<IntegerPartition> negative = IntegerPartition.integerPartitions(i);
            for (IntegerPartition pos : positive) {
                for (IntegerPartition neg : negative) {
                    list.add(new SignedIntegerPartition(pos.getPartition(), neg.getPartition()));
                }
            }
        }
        Collections.sort(list);
        return list;
    }
}
