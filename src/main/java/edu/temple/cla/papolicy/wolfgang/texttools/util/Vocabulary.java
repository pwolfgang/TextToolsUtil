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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.StringJoiner;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 * Class to encapsulate the vocabulary
 * @author Paul Wolfgang
 */
public class Vocabulary implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(Vocabulary.class);

    /** List of word probabilities indexed by word id */
    private final ArrayList<Double> allWords;
    /** Map of words to word id */
    private final  Map<String, Integer> wordIds;
    /** List of word indexed by word id */
    private final List<String> idWords;

    /** WordCounter to accumulate the count of the words */
    private final WordCounter counter;
    
    /** Flag to indicate that new words are not to be entered into the vocabulary */
    private boolean frozen = false;
    
    /** Construct a Vocabulary from a WordCounter
     * @param counter The WordCounter to accumulate the count of words.
     */
    public Vocabulary(WordCounter counter) {
        allWords = new ArrayList<>();
        allWords.add(0.0); // there is no word zero
        wordIds = new HashMap<>();
        idWords = new ArrayList<>();
        idWords.add(""); // there is no word zero
        wordIds.put("", 0);
        this.counter = counter;
        counter.getWords().forEach(word -> {
            if (!"TOTAL_WORDS".equals(word)) {
                int thisWordId = idWords.size();
                wordIds.put(word, thisWordId);
                idWords.add(word);
                allWords.add(counter.getProb(word));
            }
        });
    }

    /** Method to compute the probability of each word the vocabulary.
     * After this method is called, the vocabulary is frozen.
     */
    public void computeProbabilities() {
        if (!frozen) {
            frozen = true;
            allWords.ensureCapacity(idWords.size());
            for (int i = 1; i < idWords.size(); i++) {
                String word = idWords.get(i);
                allWords.add(counter.getProb(word));
            }
        }
    }
    
    /** Method to return the wordID of a word 
     * @param word The word
     * @return The wordID if in the vocabulary, null otherwise
     */
    public Integer getWordId(String word) {
        return wordIds.get(word);
    }
    
    /** Method to return the probability of a given wordID 
     * @param wordID The word id
     * @return The probability.  If wordID is null, null is returned
     */
    public Double getWordProb(Integer wordID) {
        if (wordID == null) return null;
        return allWords.get(wordID);
    }
    
    public double getLaplaseProb(String word) {
        return counter.getLaplaseProb(word).orElse(1.0/(counter.getNumWords() + counter.getNumUniqueWords() - 1));
    }
    
    public Integer getWordCount(String word) {
        return counter.getCount(word);
    }

    /** Method to return the word given the wordID
     *  @param wordID The id of the word
     *  @return The corresponding word
     */
    public String getWord(int wordID) {
        return idWords.get(wordID);
    }
    
    public int numFeatures() {
        return idWords.size();
    }
    
    public int getNumWords() {
        return counter.getNumWords();
    }
    
    public List<String> getWordList() {
        return Collections.unmodifiableList(idWords);
    }

    /**
     * Method to output the Vocabulary in alphabetical order.
     * Output is of the form %lt;word&gt; &lt;probability&gt;
     * @param fileName The name of the file to which the
     * Vocabulary is written.
     */
    public void writeVocabulary(String fileName) {
        SortedMap<String, Double> vocab = new TreeMap<>();
        int maxWordLength = 0;
        for (Map.Entry<String, Integer> e : wordIds.entrySet()) {
            String word = e.getKey();
            Integer id = e.getValue();
            Double prob = getWordProb(id);
            if (maxWordLength < word.length()) maxWordLength = word.length();
            vocab.put(word, prob);
        }
        String format = String.format("%%-%ds%%f%%n", maxWordLength + 2);
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            vocab.forEach((k, v) -> pw.printf(format, k,v));
        } catch (IOException ioex) {
            LOGGER.error("Error writing vocabulary", ioex);
            System.err.printf("Error writing vocabulary %s%n", ioex.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Create a string representation of this vocabulary object. The string
     * representation is a listing of each word id followed by the word and
     * its probability.
     * @return String representation of this vocabulary object.
     */
    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        for (int i = 1; i < idWords.size(); i++) {
            sj.add(String.format("%4s %10s %f", i, idWords.get(i), allWords.get(i)));
        }
        return sj.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.idWords);
        hash = 29 * hash + Objects.hashCode(this.counter);
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() == this.getClass()) {
            Vocabulary other = (Vocabulary) o;
            return other.counter.equals(counter)
                    && other.idWords.equals(idWords);
        } else {
            return false;
        }
    }

}
