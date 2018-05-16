/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
