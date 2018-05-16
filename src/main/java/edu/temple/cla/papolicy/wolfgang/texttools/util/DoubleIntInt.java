/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.wolfgang.texttools.util;

import java.util.Comparator;

/**
 * The DoubleIntInt class represents a tripple consisting of a double and two
 * integer values. It is ordered by the value of the double followed by the
 * value of the ints.
 * @author Paul Wolfgang
 */
public class DoubleIntInt implements Comparable<DoubleIntInt> {

    private final double d;
    private final int x;
    private final int y;

    public DoubleIntInt(double d, int x, int y) {
        this.d = d;
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(DoubleIntInt o) {
        if (getD() < o.getD()) return -1;
        if (getD() > o.getD()) return +1;
        if (getX() < o.getX()) return -1;
        if (getX() > o.getX()) return +1;
        if (getY() < o.getY()) return -1;
        if (getY() > o.getY()) return +1;
        return 0;
    }

    public static Comparator<DoubleIntInt> reverseComparator() {
        return (DoubleIntInt d1, DoubleIntInt d2) -> d2.compareTo(d1);
    }

    /**
     * @return the d
     */
    public double getD() {
        return d;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
}
