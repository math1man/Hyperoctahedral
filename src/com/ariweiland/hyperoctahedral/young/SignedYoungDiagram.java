package com.ariweiland.hyperoctahedral.young;

import com.ariweiland.hyperoctahedral.partition.IntegerPartition;
import com.ariweiland.hyperoctahedral.partition.SignedIntegerPartition;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class SignedYoungDiagram extends AbstractYoungDiagram<SignedYoungDiagram> {

    private final YoungDiagram positive;
    private final YoungDiagram negative;

    public SignedYoungDiagram(IntegerPartition positive, IntegerPartition negative) {
        this(new YoungDiagram(positive), new YoungDiagram(negative));
    }

    public SignedYoungDiagram(SignedIntegerPartition sip) {
        this(new YoungDiagram(sip.getPositive()), new YoungDiagram(sip.getNegative()));
    }

    public SignedYoungDiagram(YoungDiagram positive, YoungDiagram negative) {
        this.positive = positive;
        this.negative = negative;
    }

    public YoungDiagram getPositive() {
        return positive;
    }

    public YoungDiagram getNegative() {
        return negative;
    }

    @Override
    public boolean isEmpty() {
        return positive.isEmpty() && negative.isEmpty();
    }

    @Override
    public SignedYoungDiagram reflect() {
        return new SignedYoungDiagram(positive.reflect(), negative.reflect());
    }

    /**
     * Returns a reversed SignedYoungPartition, with the positive and negative components swapped.
     * @return
     */
    public SignedYoungDiagram reverse() {
        return new SignedYoungDiagram(negative, positive);
    }

    @Override
    public Map<SignedYoungDiagram, Integer> reduce(int n) {
        Map<YoungDiagram, Integer> posMap = positive.reduce(n);
        Map<YoungDiagram, Integer> negMap = negative.reduce(n);
        Map<SignedYoungDiagram, Integer> output = new HashMap<>();
        for (YoungDiagram y : posMap.keySet()) {
            output.put(new SignedYoungDiagram(y, negative), posMap.get(y));
        }
        for (YoungDiagram y : negMap.keySet()) {
            output.put(new SignedYoungDiagram(positive, y), -negMap.get(y));
        }
        return output;
    }

    @Override
    public int compareTo(SignedYoungDiagram o) {
        IntegerPartition myPos = positive.getPartition();
        IntegerPartition myNeg = negative.getPartition();
        IntegerPartition otPos = o.positive.getPartition();
        IntegerPartition otNeg = o.negative.getPartition();
        int myPosLen = myPos.getPartition().length;
        int myNegLen = myNeg.getPartition().length;
        int otPosLen = otPos.getPartition().length;
        int otNegLen = otNeg.getPartition().length;
        // compare total negative
        int compare = Integer.compare(myNeg.getSize(), otNeg.getSize());
        if (compare != 0) {
            return compare;
        }
        // compare positive length or negative length
        if (myPos.getSize() <= myNeg.getSize()) {
            compare = Integer.compare(myPosLen, otPosLen);
        } else {
            compare = -Integer.compare(myNegLen, otNegLen);
        }
        if (compare != 0) {
            return compare;
        }
        // compare the other
        if (myPos.getSize() <= myNeg.getSize()) {
            compare = -Integer.compare(myNegLen, otNegLen);
        } else {
            compare = Integer.compare(myPosLen, otPosLen);
        }
        if (compare != 0) {
            return compare;
        }
        // lexicographic sort
        for (int i=0; i<myPosLen; i++) {
            compare = -Integer.compare(myPos.getPartition()[i], otPos.getPartition()[i]);
            if (compare != 0) {
                return compare;
            }
        }
        for (int i=0; i<myNegLen; i++) {
            compare = Integer.compare(myNeg.getPartition()[i], otNeg.getPartition()[i]);
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

        SignedYoungDiagram that = (SignedYoungDiagram) o;

        return negative.equals(that.negative) && positive.equals(that.positive);

    }

    @Override
    public int hashCode() {
        int result = positive.hashCode();
        result = 31 * result + negative.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" + positive + ", " + negative + "}";
    }

    /**
     * Returns all SignedYoungDiagrams in sorted order.
     * @param n
     * @return
     */
    public static List<SignedYoungDiagram> all(int n) {
        List<SignedYoungDiagram> list = new ArrayList<>();
        for (SignedIntegerPartition sip : SignedIntegerPartition.all(n)) {
            list.add(new SignedYoungDiagram(sip));
        }
        Collections.sort(list);
        return list;
    }
}
