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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

/**
 * A class to count words (tokens)
 *
 * @author Paul Wolfgang
 */
public class WordCounter implements Serializable {
   
    private final Map<String, Double> wordMap;
    private int numDocs;
    
    /**
     * Get the The count of documents represented by this WordCounter. 
     * 
     * @return The count of documents represented by this WordCounter. 
     */
    public int getNumDocs() {
        return numDocs;
    }
    
    public WordCounter(Map<String, Double> wordMap) {
        this.wordMap = wordMap;
        numDocs = 1;
    }
    
    public WordCounter() {
        wordMap = new HashMap<>();
    }
    
    public void updateCounts(String word) {
        wordMap.merge(word, 1.0, Double::sum);
        wordMap.merge("TOTAL_WORDS", 1.0, Double::sum);
    }

    /**
     * Update the counts for each word in another counter.
     * @param wordCounter Source of words to update this counter.
     */
    public void updateCounts(WordCounter wordCounter) {
        wordCounter.wordMap.forEach((w, c) -> {
            wordMap.merge(w, c, Double::sum);
        });
        numDocs++;
    }

    /**
     * Return the number of words seen
     *
     * @return The number of words
     */
    public int getNumWords() {
        return wordMap.get("TOTAL_WORDS").intValue();
    }
    
    public int getNumUniqueWords() {
        return wordMap.keySet().size();
    }
    
    public int getCount(String word) {
        return wordMap.getOrDefault(word, 0.0).intValue();
    }

    /**
     * Return the probability of a given word. The probability is the number of
     * times this word occurs divided by the total number of words
     *
     * @param word The word
     * @return The probability of a given word
     */
    public double getProb(String word) {
        double numWords = wordMap.get("TOTAL_WORDS");
        return wordMap.getOrDefault(word, 0.0)/numWords;
    }
    
    
    public Optional<Double> getLaplaseProb(String word) {
        if (!wordMap.containsKey(word)) {
            return Optional.empty();
        }
        double totalWords = wordMap.get("TOTAL_WORDS");
        double numWords = wordMap.keySet().size();
        double denom = totalWords + numWords - 1.0;
        return Optional.of((wordMap.get(word) + 1)/denom);
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
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() == this.getClass()) {
            WordCounter other = (WordCounter)o;
            return wordMap.equals(other.wordMap);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.wordMap);
        return hash;
    }
}
