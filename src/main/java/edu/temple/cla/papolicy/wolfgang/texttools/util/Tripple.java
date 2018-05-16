/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.wolfgang.texttools.util;

/**
 *
 * @author Paul Wolfgang
 * @param <T1> The type of the first element
 * @param <T2> The type of the second element
 * @param <T3> The type of the third element
 */
public class Tripple<T1, T2, T3> {
    private final T1 t1;
    private final T2 t2;
    private final T3 t3;

    public Tripple(T1 t1, T2 t2, T3 t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }
    public T1 getT1(){return t1;}
    public T2 getT2(){return t2;}
    public T3 getT3(){return t3;}

    @Override
    public int hashCode() {
        return (t1.hashCode() * 511 + t2.hashCode()) * 511 + t3.hashCode();
    }

    @Override
    public String toString() {
        return "(" + t1 + ", " + t2 + ", " + t3 + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass().equals(this.getClass())) {
            Tripple<T1, T2, T3> otherTripple = (Tripple<T1, T2, T3>)o;
            return t1.equals(otherTripple.t1) && t2.equals(otherTripple.t2)
                    && t3.equals(otherTripple.t3);
        } else {
            return false;
        }
    }

}
