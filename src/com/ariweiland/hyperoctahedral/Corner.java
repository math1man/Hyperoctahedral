package com.ariweiland.hyperoctahedral;

import java.util.Collections;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class Corner extends CornerSequence {

    private final int height;
    private final int width;

    public Corner(int index, int partSize, int height, int width) {
        super(index, partSize);
        if (width > partSize) {
            throw new IllegalArgumentException("Width cannot possibly be greater than the part size.");
        }
        if (height < 1) {
            throw new IllegalArgumentException("Height must be greather than 0.");
        }
        if (width < 1) {
            throw new IllegalArgumentException("Width must be greather than 0.");
        }
        this.height = height;
        this.width = width;
    }

    public int getCornerIndex() {
        return getStartIndex() + getHeight() - 1;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public List<Corner> getCorners() {
        return Collections.singletonList(this);
    }

    public int degreesOfFreedom(int n) {
        if (n > getLength()) {
            return 0;
        } else {
            return Math.min(getLength() - n + 1, n);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Corner corner = (Corner) o;

        return height == corner.height && width == corner.width;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + height;
        result = 31 * result + width;
        return result;
    }

    @Override
    public String toString() {
        return "Corner{" +
                "index=" + getStartIndex() +
                ", partSize=" + getStartPartSize() +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
