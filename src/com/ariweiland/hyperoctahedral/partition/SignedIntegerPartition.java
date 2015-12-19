package com.ariweiland.hyperoctahedral.partition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class SignedIntegerPartition extends AbstractPartition implements Comparable<SignedIntegerPartition> {

    private final IntegerPartition positive;
    private final IntegerPartition negative;

    /**
     * Constructs a SignedIntegerPartition of size 0.
     */
    public SignedIntegerPartition() {
        this(new IntegerPartition(), new IntegerPartition());
    }

    /**
     * Constructs a SignedIntegerPartition from the input partitions.
     * @param positive
     * @param negative
     */
    public SignedIntegerPartition(int[] positive, int[] negative) {
        this(new IntegerPartition(positive), new IntegerPartition(negative));
    }

    /**
     * Constructs a SignedIntegerPartition from the input arrays.
     * @param positive
     * @param negative
     */
    public SignedIntegerPartition(IntegerPartition positive, IntegerPartition negative) {
        super(positive.getSize() + negative.getSize());
        this.positive = positive;
        this.negative = negative;
    }

    public IntegerPartition getPositive() {
        return positive;
    }

    public IntegerPartition getNegative() {
        return negative;
    }

    @Override
    public int[] getPartition() {
        int[] pos = positive.getPartition();
        int[] neg = negative.getPartition();
        int[] joined = new int[pos.length + neg.length];
        System.arraycopy(pos, 0, joined, 0, pos.length);
        for (int i=0; i<neg.length; i++) {
            joined[pos.length + i] = -neg[neg.length - 1 - i];
        }
        return joined;
    }

    @Override
    public SignedIntegerPartition inverse() {
        IntegerPartition pos = getPositive().inverse();
        IntegerPartition neg = getNegative().inverse();
        return new SignedIntegerPartition(pos, neg);
    }

    /**
     * Returns a reversed SignedIntegerPartition, with the positive and negative parts swapped.
     * @return
     */
    public SignedIntegerPartition reverse() {
        return new SignedIntegerPartition(getNegative(), getPositive());
    }

    @Override
    public int compareTo(SignedIntegerPartition o) {
        int[] myPos = positive.getPartition();
        int[] myNeg = negative.getPartition();
        int[] otPos = o.positive.getPartition();
        int[] otNeg = o.negative.getPartition();
        // compare total negative
        int compare = Integer.compare(negative.getSize(), o.negative.getSize());
        if (compare != 0) {
            return compare;
        }
        // compare negative length or positive length
        if (positive.getSize() < negative.getSize()) {
            compare = -Integer.compare(myPos.length, otPos.length);
        } else {
            compare = Integer.compare(myNeg.length, otNeg.length);
        }
        if (compare != 0) {
            return compare;
        }
        // compare the other
        if (positive.getSize() < negative.getSize()) {
            compare = Integer.compare(myNeg.length, otNeg.length);
        } else {
            compare = -Integer.compare(myPos.length, otPos.length);
        }
        if (compare != 0) {
            return compare;
        }
        // lexicographic sort
        for (int i=0; i< myPos.length; i++) {
            compare = Integer.compare(myPos[i], otPos[i]);
            if (compare != 0) {
                return compare;
            }
        }
        for (int i=0; i< myNeg.length; i++) {
            compare = -Integer.compare(myNeg[i], otNeg[i]);
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

        return negative.equals(that.negative) && positive.equals(that.positive);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + positive.hashCode();
        result = 31 * result + negative.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" + positive + ", " + negative + "}";
    }

    /**
     * Returns all SignedIntegerPartitions in sorted order.
     * @param n
     * @return
     */
    public static List<SignedIntegerPartition> all(int n) {
        List<SignedIntegerPartition> list = new ArrayList<>();
        for (int i=0; i<=n; i++) {
            List<IntegerPartition> positive = IntegerPartition.all(n - i);
            List<IntegerPartition> negative = IntegerPartition.all(i);
            for (IntegerPartition pos : positive) {
                for (IntegerPartition neg : negative) {
                    list.add(new SignedIntegerPartition(pos, neg));
                }
            }
        }
        Collections.sort(list);
        return list;
    }
}
