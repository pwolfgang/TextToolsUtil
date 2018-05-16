/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.wolfgang.texttools.util;

import edu.temple.cla.papolicy.wolfgang.stemmerfactory.StemmerFactory;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.tartarus.snowball.javastemmers.SnowballStemmer;

/**
 *
 * @author Paul Wolfgang
 */
public class Preprocessor {

    private static final Pattern NOT_WORD = Pattern.compile("[^\\p{L}']+");
    private final SnowballStemmer stemmer;
    private final StopWord stopWord;

    public Preprocessor(String stemmerLanguage, String stopWordLanguage) {
        stemmer = StemmerFactory.getInstance(stemmerLanguage);
        stopWord = new StopWord(stopWordLanguage);
    }

    /** Method to split a string into a sequence of stemmed words.
     * A word is defined as a sequence of upper and lower case letters.
     * The apostrophy character is also considered a letter provided it
     * is preceeded and followed by a letter.
     * All words are converted to lower case.
     * @param line The string to be split
     * @return An array list of stemmed words
     */
    public ArrayList<String> preprocess(String line) {
        ArrayList<String> result = new ArrayList<>();
        String[] tokens = NOT_WORD.split(line);
        for (String newWord : tokens) {
            newWord = newWord.toLowerCase().trim();
            if (newWord.startsWith("'")) {
                newWord = newWord.substring(1);
            }
            if (newWord.endsWith("'")) {
                newWord = newWord.substring(0, newWord.length() - 1);
            }
            if (!newWord.isEmpty()) {
                if (!stopWord.isStopWord(newWord)) {
                    stemmer.setCurrent(newWord);
                    stemmer.stem();
                    newWord = stemmer.getCurrent().trim();
                    if (!newWord.isEmpty()) {
                        result.add(newWord);
                    }
                }
            }
        }
        return result;
    }
}
