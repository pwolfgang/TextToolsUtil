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
