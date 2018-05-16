/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.wolfgang.texttools.util;

/**
 *
 * @author Paul Wolfgang
 * @param <T1> The first type
 * @param <T2> The second type
 */
public class Pair<T1, T2> {
    private final T1 t1;
    private final T2 t2;
    public Pair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }
    public T1 getT1(){return t1;}
    public T2 getT2(){return t2;}
    
    @Override
    public int hashCode() {
        return t1.hashCode() * 511 + t2.hashCode();
    }
    
    @Override
    public String toString() {
        return "(" + t1 + ", " + t2 + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass().equals(this.getClass())) {
            Pair<T1, T2> otherPair = (Pair<T1, T2>)o;
            return t1.equals(otherPair.t1) && t2.equals(otherPair.t2);
        } else {
            return false;
        }
    }

}
