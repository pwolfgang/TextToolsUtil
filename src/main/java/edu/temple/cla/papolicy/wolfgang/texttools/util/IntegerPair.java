/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.wolfgang.texttools.util;

/**
 * Class to represent a pair of int values.
 * @author Paul Wolfgang
 */
public class IntegerPair {
    
    /** The x value */
    private final int x;
    /** The y value */
    private final int y;
    
    /** Construct an integer pair
     * @param x The first value
     * @param y The second value
     */
    public IntegerPair(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /** Return x
     * @return  x
     */
    public int getX() {return x;}
    
    /** Return y
     * @return  y
     */
    public int getY() {return y;}
    
    /** Compute Hash Code
     * @return  A hash code
     */
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
    
    /** Compare for equality
     * @param o The object to be compared to
     * @return  true if o is equal to this
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass().equals(IntegerPair.class)) {
            IntegerPair ip = (IntegerPair) o;
            return x == ip.x && y == ip.y;
        }
        return false;
    }
    
    /** Create String representation
     * @return String representation
     */
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
