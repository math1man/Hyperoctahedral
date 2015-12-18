package com.ariweiland.hyperoctahedral;

import java.util.List;

/**
 * @author Ari Weiland
 */
public class CornerSequence {

    private final int startIndex;
    private final int startPartSize;
    private final List<Corner> corners;

    protected CornerSequence(int startIndex, int startPartSize) {
        this.startIndex = startIndex;
        this.startPartSize = startPartSize;
        this.corners = null;
    }

    public CornerSequence(List<Corner> corners) {
        if (corners == null || corners.isEmpty()) {
            throw new IllegalArgumentException("No corners specified!");
        } else if (!checkAdjacency(corners)) {
            throw new IllegalArgumentException("Corners must be adjacent!");
        }
        this.startIndex = corners.get(0).getStartIndex();
        this.startPartSize = corners.get(0).getStartPartSize();
        this.corners = corners;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getStartPartSize() {
        return startPartSize;
    }
    
    public int getHeight() {
        int sum = 0;
        for (Corner c : corners) {
            sum += c.getHeight();
        }
        return sum;
    }

    public int getFirstCornerIndex() {
        return getCorners().get(0).getCornerIndex();
    }
    
    public int getWidth() {
        int sum = 0;
        for (Corner c : corners) {
            sum += c.getWidth();
        }
        return sum;
    }

    public int getLength() {
        int sum = getCorners().size() - 1;
        for (Corner c : getCorners()) {
            sum += c.getHeight() + c.getWidth() - 1;
        }
        return sum;
    }

    public int getMinLength() {
        int sum = 1;
        for (int i=0; i<getCorners().size(); i++) {
            if (i > 0) {
                sum += corners.get(i).getHeight();
            }
            if (i < getCorners().size() - 1) {
                sum += corners.get(i).getWidth();
            }
        }
        return sum;
    }

    public List<Corner> getCorners() {
        return corners;
    }

    public int size() {
        return getCorners().size();
    }

    public Corner get(int i) {
        return getCorners().get(i);
    }

    public boolean isCorner() {
        return size() == 1;
    }

    public int degreesOfFreedom(int n) {
        if (n > getLength() || n < getMinLength()) {
            return 0;
        } else {
            int firstHeight = get(0).getHeight();
            return MathUtils.min(getLength() - n + 1, n - getMinLength() + 1, firstHeight, getLength() - firstHeight - getMinLength() + 2);
        }
    }

    public boolean isAdjacent(CornerSequence other) {
        if (other.getStartIndex() == getStartIndex()) {
            return false;
        } else if (other.getStartIndex() > getStartIndex()) {
            return getStartPartSize() - other.getStartPartSize() == getWidth()
                    && other.getStartIndex() - getStartIndex() == getHeight();
        } else {
            return other.getStartPartSize() - getStartPartSize() == other.getWidth()
                    && getStartIndex() - other.getStartIndex() == other.getHeight();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CornerSequence that = (CornerSequence) o;

        return startIndex == that.startIndex
                && startPartSize == that.startPartSize
                && !(corners != null ? !corners.equals(that.corners) : that.corners != null);

    }

    @Override
    public int hashCode() {
        int result = startIndex;
        result = 31 * result + startPartSize;
        result = 31 * result + (corners != null ? corners.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CornerSequence{" +
                "startIndex=" + startIndex +
                ", startPartSize=" + startPartSize +
                ", corners=" + corners +
                '}';
    }

    public static boolean checkAdjacency(List<? extends CornerSequence> corners) {
        for (int i=0; i<corners.size() - 1; i++) {
            // check order and adjacency
            if (corners.get(i).getStartIndex() >= corners.get(i+1).getStartIndex() 
                    || !corners.get(i).isAdjacent(corners.get(i+1))) {
                return false;
            }
        }
        return true;

    }
}
