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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class to provide language specific StopWord lists. The stopword lists were
 * extracted from
 * <a href="http://snowball.tartarus.org/dist/snowball_all.tgz">
 * http://snowball.tartarus.org/dist/snowball_all.tgz </a>
 * The following languages are supported: Danish, Dutch, English, Finnish,
 * French, German, Hungarian, Italian, Norwegian, Portuguese Russian, Spanish,
 * and Swedish.
 *
 * @author Paul Wolfgang
 */
public class StopWord {

    Set<String> wordList = new HashSet<>();
    private static final Map<String, String> charsetNames;
    
    static {
    String[][] charSetNamesArray = {
        {"danish", "ISO-8859-4"},
        {"dutch",  "ISO-8859-1"},
        {"english", "ISO-8859-1"},
        {"french", "ISO-8859-1"},
        {"finnish", "ISO-8859-13"},
        {"german", "ISO-8859-2"},
        {"hungarian", "ISO-8859-2"},
        {"italian", "ISO-8859-1"},
        {"norwegian", "ISO-8859-1"},
        {"portuguese", "ISO-8859-1"},
        {"russian", "ISO-8859-5"},
        {"spanish", "ISO-8859-1"},
        {"swedish", "ISO-8859-4"}};
        charsetNames = new HashMap<>();
        for (var namePair : charSetNamesArray) {
            charsetNames.put(namePair[0], namePair[1]);
        }
        
    }
    
    /**
     * Initialize the wordList with the selected language. If no language is
     * provided, or if the language is "true", then the default list is the one
     * provided by Chris Buckley and Gerard Salton at Cornell University. If the
     * language is "false" or "0", then no StopWord filtering is performed.
     *
     * @param language The selected language.
     */
    public StopWord(String language) {
        String path;
        String charsetName;
        if (language == null || language.isEmpty()) {
            path = "META-INF/StopWordList.txt";
            charsetName = "ISO-8859-1";
        } else {
            language = language.toLowerCase();
            switch (language) {
                case "true":
                    path = "META-INF/StopWordList.txt";
                    charsetName = "ISO-8859-1";
                    break;
                case "false":
                    path = null;
                    charsetName = null;
                    break;
                case "0":
                    path = null;
                    charsetName = null;
                    break;
                default:
                    path = "META-INF/stoplists/" + language + "/stop.txt";
                    charsetName = charsetNames.get(language);
                    break;
            }
        }
        InputStream is = null;
        if (path != null) {
            is = ClassLoader.getSystemResourceAsStream(path);
        }
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, charsetName));){
            br.lines()
                    .filter(l -> !l.startsWith("#"))
                    .map(l -> l.split("\\s*\\|\\s*"))
                    .map(Arrays::stream)
                    .map(s -> s.findFirst())
                    .map(o -> o.orElse(""))
                    .filter(s -> !s.isEmpty())
                    .map(s -> s.split("\\s+"))
                    .flatMap(Arrays::stream)
                    .forEach(w -> wordList.add(w));
            } catch (Exception ioex) {
                throw new RuntimeException(language + " " + charsetName, ioex);
            }
        }
    }

    public boolean isStopWord(String word) {
        return wordList.contains(word.trim());
    }
}
