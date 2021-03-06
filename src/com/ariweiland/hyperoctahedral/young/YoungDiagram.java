package com.ariweiland.hyperoctahedral.young;

import com.ariweiland.hyperoctahedral.partition.IntegerPartition;
import com.ariweiland.hyperoctahedral.partition.IntegerPartitionBuilder;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class YoungDiagram extends AbstractYoungDiagram<YoungDiagram> {

    private final IntegerPartition partition;

    public YoungDiagram(int... partition) {
        this(new IntegerPartition(partition));
    }

    public YoungDiagram(IntegerPartition partition) {
        this.partition = partition;
    }

    public IntegerPartition getPartition() {
        return partition;
    }

    @Override
    public boolean isEmpty() {
        return partition.getSize() == 0;
    }

    @Override
    public YoungDiagram reflect() {
        return new YoungDiagram(getPartition().inverse());
    }

    /**
     * Returns all corners of the diagram
     * @return
     */
    public List<Corner> getCorners() {
        List<Corner> corners = new ArrayList<>();
        int[] p = partition.getPartition();
        int index = 0;
        for (int i = 0; i<p.length-1; i++) {
            if (p[i] != p[i+1]) {
                corners.add(new Corner(index, p[index], i - index + 1, p[i] - p[i+1]));
                index = i+1;
            }
        }
        if (p.length > 0) {
            corners.add(new Corner(index, p[index], p.length - index, p[index]));
        }
        return corners;
    }

    /**
     * Returns all sequences of adjacent corners of the diagram, including single corners.
     * @return
     */
    public List<CornerSequence> getCornerSequences() {
        List<CornerSequence> sequences = new ArrayList<>();
        List<Corner> corners = getCorners();
        for (int i=0; i<corners.size(); i++) {
            for (int j=i; j<corners.size(); j++) {
                sequences.add(new CornerSequence(corners.subList(i, j+1)));
            }
        }
        return sequences;
    }

    @Override
    public Map<YoungDiagram, Integer> reduce(int n) {
        Map<YoungDiagram, Integer> reduced = new HashMap<>();
        int reducedSize = partition.getSize() - n;
        int[] p = partition.getPartition();
        // first, handle single-corner reductions
        for (CornerSequence c : getCornerSequences()) {
            for (int i=0; i<c.degreesOfFreedom(n); i++) {
                IntegerPartitionBuilder builder = new IntegerPartitionBuilder(reducedSize);
                int k = 0;
                int cut = n;
                for (int j=0; j<p.length; j++) {
                    if (j - i >= c.get(0).getCornerIndex() + c.getMinLength() - n
                            && j - i >= c.getStartIndex()
                            && j < c.getStartIndex() + c.getHeight()) {
                        // handle reduction
                        if (j == c.get(k).getCornerIndex()) { // remove more than one
                            if (k == c.size() - 1) { // last corner: remove the rest
                                builder.addPart(p[j] - cut);
                            } else { // middle corner: remove width and inner corner
                                builder.addPart(p[j] - (c.get(k).getWidth() + 1));
                                cut -= c.get(k).getWidth() + 1;
                                k++;
                            }
                        } else { // remove just one
                            builder.addPart(p[j] - 1);
                            cut--;
                        }
                    } else {
                        builder.addPart(p[j]);
                    }
                }
                reduced.put(new YoungDiagram(builder.build()),
                        c.getHeight() - Math.max(c.get(0).getHeight() + c.getMinLength() - 1 - n, 0) - i);
            }
        }
        // multicorner reductions
        return reduced;
    }

    @Override
    public int compareTo(YoungDiagram o) {
        // compare by length
        int compare = Integer.compare(partition.getPartition().length, o.partition.getPartition().length);
        if (compare != 0) {
            return compare;
        }
        // compare lexicographically
        for (int i=0; i<partition.getPartition().length; i++) {
            compare = -Integer.compare(partition.getPartition()[i], o.partition.getPartition()[i]);
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

        YoungDiagram that = (YoungDiagram) o;

        return partition.equals(that.partition);

    }

    @Override
    public int hashCode() {
        return partition.hashCode();
    }

    @Override
    public String toString() {
        return partition.toString();
    }

    /**
     * Returns all YoungDiagrams in sorted order.
     * @param n
     * @return
     */
    public static List<YoungDiagram> all(int n) {
        List<YoungDiagram> list = new ArrayList<>();
        for (IntegerPartition ip : IntegerPartition.all(n)) {
            list.add(new YoungDiagram(ip));
        }
        Collections.sort(list);
        return list;
    }
}
