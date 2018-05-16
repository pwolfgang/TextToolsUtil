package edu.temple.cla.papolicy.wolfgang.texttools.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Class to encapsulate the vocabulary
 * @author Paul Wolfgang
 */
public
        class Vocabulary implements Serializable {

    /** List of word probabilities indexed by word id */
    private final ArrayList<Double> allWords;
    /** Map of words to word id */
    private final  Map<String, Integer> wordIds;
    /** Arraylist of word indexed by word id */
    private final List<String> idWords;

    /** WordCounter to accumulate the count of the words */
    private final WordCounter counter;
    
    /** Flag to indicate that new words are not to be entered into the vocabulary */
    private boolean frozen = false;
    
    /** Count of the total number of words entered */
    int wordCount;

    /** Construct an empty Vocabulary */
    public Vocabulary() {
        allWords = new ArrayList<>();
        allWords.add(0.0); // there is no word zero

        wordIds = new HashMap<>();
        idWords = new ArrayList<>();
        idWords.add(""); // there is no word zero
        counter = new WordCounter();
    }

    /** Method to add words to the vocabulary and update the counts 
     * @param words A list of words to be added
     */
    public void updateCounts(List<String> words) {
        if (!frozen) {
            words.forEach(word ->{
                Integer thisWordID = wordIds.get(word);
                if (thisWordID == null) {
                    thisWordID = idWords.size();
                    wordIds.put(word, thisWordID);
                    idWords.add(word);
                }
            });
            counter.updateCounts(words);
        }
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

    /** Method to return the word given the wordID
     *  @param wordID The id of the word
     *  @return The corresponding word
     */
    public String getWord(int wordID) {
        return idWords.get(wordID);
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
            ioex.printStackTrace();
            System.exit(1);
        }
    }

}
