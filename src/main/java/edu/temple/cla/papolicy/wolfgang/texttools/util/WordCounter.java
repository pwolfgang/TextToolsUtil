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
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

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
        
        public void add(int delta) {
            count += delta;
        }

        /**
         * Return the count
         *
         * @return the count
         */
        public int getCount() {
            return count;
        }
        
        /**
         * Return the count as a string.
         * @return The count converted to a string.
         */
        @Override
        public String toString() {
            return Integer.toString(count);
        }
    }
    
    private Map<String, Double> laplaseProb = null;
    private final Map<String, Counter> wordMap = new HashMap<>();
    private int numWords = 0;
    private int sum = 0;

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
     * Update the counts for each word in another counter.
     * @param wordCounter 
     */
    public void updateCounts(WordCounter wordCounter) {
        wordCounter.wordMap.forEach((w, c) -> {
            Counter count = wordMap.get(w);
            if (count == null) {
                count = new Counter();
                wordMap.put(w, count);
            }
            count.add(c.getCount());
            numWords += c.getCount();
        });
        
    }

    /**
     * Return the number of words seen
     *
     * @return The number of words
     */
    public int getNumWords() {
        return numWords;
    }
    
    public int getCount(String word) {
        Counter c = wordMap.get(word);
        if (c == null) {
            return 0;
        } else {
            return c.getCount();
        }
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
    
    
    public Optional<Double> getLaplaseProb(String word) {
        if (laplaseProb == null) {
            laplaseProb = new HashMap<>();
            sum = 0;
            wordMap.forEach((w, c) -> {
                int cP1 = c.getCount() + 1;
                laplaseProb.put(w, (double)cP1);
                sum += cP1;
            });
            laplaseProb.keySet().forEach((w) -> {
                double countForWord = laplaseProb.get(w);
                laplaseProb.put(w, countForWord/sum);
            });
        }
        return Optional.ofNullable(laplaseProb.get(word));
    }

    /**
     * Return the list of words as a Set
     *
     * @return The list of words as a Set
     */
    public Set<String> getWords() {
        return wordMap.keySet();
    }
    
    /**
     * Create a string representation of this WordCounter object.
     * @return A list of each word followed by the number of times it appears.
     */
    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "{", "}\n");
        wordMap.forEach((k, v) -> sj.add(String.format("%s -> %s", k, v)));
        return sj.toString();
    }
}
