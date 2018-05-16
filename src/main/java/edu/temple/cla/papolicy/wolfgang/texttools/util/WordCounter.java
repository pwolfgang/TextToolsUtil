package edu.temple.cla.papolicy.wolfgang.texttools.util;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
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
     * Update the counts using the tokens in the input list
     *
     * @param tokens The list of tokens
     */
    public void updateCounts(List<String> tokens) {
        tokens.forEach(token -> {
            Counter count = wordMap.get(token);
            if (count == null) {
                count = new Counter();
                wordMap.put(token, count);
            }
            count.increment();
            ++numWords;
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
