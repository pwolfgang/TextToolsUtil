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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class StopWordTest {

    private final StopWord stopWord;

    public StopWordTest() {
        stopWord = new StopWord("English");
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isStopWord method, of class StopWord.
     * yourself is a stop word
     */
    @Test
    public void testIsStopWord1() {
        System.out.println("isStopWord1");
        String word = "yourself";
        boolean expResult = true;
        boolean result = stopWord.isStopWord(word);
        assertTrue("'yourself' is a stopword", result);
    }

    /**
     * Test of isStopWord method, of class StopWord.
     * arf is not a stop word
     */
    @Test
    public void testIsStopWord2() {
        System.out.println("isStopWord2");
        String word = "arf";
        boolean result = stopWord.isStopWord(word);
        assertFalse("'arf' is not a stopword", result);
    }
    
    /**
     * Test to see if all languages are readable.
     */
    @Test
    public void testLanguages() {
        String[] languages = {
        "danish",
        "dutch",
        "english",
        "finnish",
        "french",
        "german",
        "hungarian",
        "italian",
        "norwegian",
        "portuguese",
        "russian",
        "spanish",
        "swedish"    
        };
        for (String language : languages) {
            var stopWd = new StopWord(language);
            System.out.println(language);
            stopWd.wordList.stream()
                    .limit(2)
                    .forEach(System.out::println);
            System.out.println();
        }
    }
    
    /**
     * Verify that lines beginning with '#' are skipped.
     */
    @Test
    public void testIsStopWord() {
        StopWord stopWord = new StopWord(null);
        assertTrue(stopWord.isStopWord("and"));
        assertFalse(stopWord.isStopWord("Buckley"));
    }  

    
}