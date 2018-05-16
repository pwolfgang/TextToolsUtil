/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.wolfgang.texttools.util;

import java.util.Comparator;

/**
 * Comparator class that returns -1 for greater,
 * 0 for equal and +1 for less. Can be used to 
 * created decending sorted sets, etc.
 * @author Paul Wolfgang
 * @param <T> The type
 */
public class Greater<T extends Comparable<T>> implements Comparator<T>{
    @Override
    public int compare(T o1, T o2) {
        return -o1.compareTo(o2);
    }
}
