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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
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
        if (language == null || language.isEmpty()) {
            path = "META-INF/StopWordList.txt";
        } else {
            language = language.toLowerCase();
            switch (language) {
                case "true":
                    path = "META-INF/StopWordList.txt";
                    break;
                case "false":
                    path = null;
                    break;
                case "0":
                    path = null;
                    break;
                default:
                    path = "META-INF/stoplists/" + language + "/stop.txt";
                    break;
            }
        }
        InputStream is = null;
        if (path != null) {
            is = ClassLoader.getSystemResourceAsStream(path);
        }
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            br.lines()
                    .map(l -> l.split("\\s*\\|\\s*"))
                    .map(Arrays::stream)
                    .map(s -> s.findFirst())
                    .map(o -> o.orElse(""))
                    .filter(s -> !s.isEmpty())
                    .map(s -> s.split("\\s+"))
                    .flatMap(Arrays::stream)
                    .forEach(w -> wordList.add(w));
        }
    }

    public boolean isStopWord(String word) {
        return wordList.contains(word.trim());
    }
}
