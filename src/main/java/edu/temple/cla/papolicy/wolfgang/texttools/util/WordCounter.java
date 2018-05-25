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

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * A class to count words (tokens)
 *
 * @author Paul Wolfgang
 */
public class WordCounter implements Serializable {

    /**
     * A class to encapsulate a counter
     */
    private static class Counter implements Serializable {

        private int count = 0;

        /**
         * Increment the count
         */
        public void increment() {
            ++count;
        }

        /**
         * Return the count
         *
         * @return the count
         */
        public int getCount() {
            return count;
        }
    }
    private final Map<String, Counter> wordMap = new HashMap<>();
    private int numWords = 0;

    /**
     * Update the count for a word
     *
     * @param word The list of tokens
     */
    public void updateCounts(String word) {
            Counter count = wordMap.get(word);
            if (count == null) {
                count = new Counter();
                wordMap.put(word, count);
            }
            count.increment();
            ++numWords;
    }

    /**
     * Return the number of words seen
     *
     * @return The number of words
     */
    public int getNumWords() {
        return numWords;
    }

    /**
     * Return the probability of a given word. The probability is the number of
     * times this word occurs divided by the total number of words
     *
     * @param word The word
     * @return The probability of a given word
     */
    public double getProb(String word) {
        Counter count = wordMap.get(word);
        if (count != null) {
            return (double) count.getCount() / (double) numWords;
        } else {
            return 0.0;
        }
    }

    /**
     * Return the list of words as a Set
     *
     * @return The list of words as a Set
     */
    public Set<String> getWords() {
        return wordMap.keySet();
    }
}
