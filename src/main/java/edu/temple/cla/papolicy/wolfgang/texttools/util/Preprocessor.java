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

import edu.temple.cla.papolicy.wolfgang.stemmer.Stemmer;
import edu.temple.cla.papolicy.wolfgang.stemmer.StemmerFactory;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author Paul Wolfgang
 */
public class Preprocessor {

    private static final Pattern NOT_WORD = Pattern.compile("[^\\p{L}']+");
    private final Stemmer stemmer;
    private final StopWord stopWord;

    /**
     * The constructor..
     * @param stemmerLanguage Language to be used for stemming.
     * @param stopWordLanguage Language to be used to find stop words.
     */
    public Preprocessor(String stemmerLanguage, String stopWordLanguage) {
        stemmer = StemmerFactory.getInstance(stemmerLanguage);
        stopWord = new StopWord(stopWordLanguage);
    }

    /**
     * Method to split a string into a sequence of stemmed words. A word is
     * defined as a sequence of upper and lower case letters. The apostrophy
     * character is also considered a letter provided it is preceeded and
     * followed by a letter. All words are converted to lower case.
     *
     * @param line The string to be split
     * @return A stream of stemmed words
     */
    public Stream<String> preprocess(String line) {
        return NOT_WORD.splitAsStream(line)
                .filter(w -> !w.isEmpty())
                .map(String::toLowerCase)
                .map(this::stripSingleQuotes)
                .filter(w -> !w.isEmpty())
                .filter(w -> !stopWord.isStopWord(w))
                .map(stemmer)
                .filter(w -> !w.isEmpty());
    }
    
    private String stripSingleQuotes(String s) {
        char[] chars = s.toCharArray();
        int firstIndex = 0;
        int lastIndex = chars.length - 1;
        if (chars[0] == '\'') firstIndex = 1;
        if (chars[lastIndex] == '\'') lastIndex--;
        if (lastIndex < firstIndex) return "";
        return new String(chars, firstIndex, lastIndex - firstIndex + 1);
    }
}
