/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.wolfgang.texttools.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Paul Wolfgang
 */
public class StopWord implements Serializable {

    private Set<String> wordList = new HashSet<>();
    

    public StopWord(String language) {
        String path = null;
        if (language == null) path = "META-INF/StopWordList.txt";
        else if ("true".equalsIgnoreCase(language)) path = "META-INF/StopWordList.txt";
        else if ("false".equalsIgnoreCase(language)) path = null;
        else if ("0".equalsIgnoreCase(language)) path = null;
        else path = "META-INF/stoplists/" + language + "/stop.txt";
        InputStream is = null;
        if (path != null) is = ClassLoader.getSystemResourceAsStream(path);
        if (is != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String[] words = line.split("\\|");
                    words[0] = words[0].trim();
                    if (!"".equals(words[0]) && !words[0].startsWith("#")) {
                        wordList.add(words[0]);
                    }
                }
            } catch (IOException ex) {
                // Ignore for now
            }
        }
    }
    
    public boolean isStopWord(String word) {
        return wordList.contains(word.trim());
    }
}
