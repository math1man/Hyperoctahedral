package com.ariweiland.hyperoctahedral.young;

import com.ariweiland.hyperoctahedral.partition.IntegerPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class YoungDiagram implements Comparable<YoungDiagram> {

    private final IntegerPartition partition;

    public YoungDiagram(int... partition) {
        this(new IntegerPartition(partition));
    }

    public YoungDiagram(IntegerPartition partition) {
        if (!partition.isComplete()) {
            throw new IllegalArgumentException("Must specify a complete partition!");
        }
        this.partition = partition;
    }

    public IntegerPartition getPartition() {
        return partition;
    }

    public boolean isEmpty() {
        return partition.getSize() == 0;
    }

    /**
     * Returns a new YoungDiagram that is the reflection of this one about its diagonal axis
     * @return
     */
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

    /**
     * Returns a
     * @param n
     * @return
     */
    public Map<YoungDiagram, Integer> reduce(int n) {
        Map<YoungDiagram, Integer> reduced = new HashMap<>();
        int reducedSize = partition.getSize() - n;
        int[] p = partition.getPartition();
        // first, handle single-corner reductions
        for (CornerSequence c : getCornerSequences()) {
            for (int i=0; i<c.degreesOfFreedom(n); i++) {
                IntegerPartition ip = new IntegerPartition(reducedSize);
                int k = 0;
                int cut = n;
                for (int j=0; j<p.length; j++) {
                    if (j - i >= c.get(0).getCornerIndex() + c.getMinLength() - n
                            && j - i >= c.getStartIndex()
                            && j < c.getStartIndex() + c.getHeight()) {
                        // handle reduction
                        if (j == c.get(k).getCornerIndex()) { // remove more than one
                            if (k == c.size() - 1) { // last corner: remove the rest
                                ip.addPart(p[j] - cut);
                            } else { // middle corner: remove width and inner corner
                                ip.addPart(p[j] - (c.get(k).getWidth() + 1));
                                cut -= c.get(k).getWidth() + 1;
                                k++;
                            }
                        } else { // remove just one
                            ip.addPart(p[j] - 1);
                            cut--;
                        }
                    } else {
                        ip.addPart(p[j]);
                    }
                }
                reduced.put(new YoungDiagram(ip),
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
}
