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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author Paul
 */
public class WordCounterTest {
    
    private WordCounter testInstance;
    private Set<String> getWordsExpectedResult;
    private Map<String, Double> map;
    
    public WordCounterTest() {
    }
    
    @Before
    public void setUp() {
        testInstance = new WordCounter();
        String[] words1 = "the quick brown fox jumps over the lazy dog".split("\\s+");
        String[] words2 = "now is the time for all good men to come to the aid of the party".split("\\s");
        String[] words3 = "the slow brown fox cannot jump over the fast dog".split("\\s+");
        String[] words4 = "it's party time".split("\\s+");
        loadWords(words1);
        loadWords(words2);
        loadWords(words3);
        loadWords(words4);
        getWordsExpectedResult = new HashSet<>();
        loadWordsIntoSet(words1);
        loadWordsIntoSet(words2);
        loadWordsIntoSet(words3);
        loadWordsIntoSet(words4);
        getWordsExpectedResult.add("TOTAL_WORDS");
        map = new HashMap<>();
        loadWordsIntoMap(words1);
        loadWordsIntoMap(words2);
        loadWordsIntoMap(words3);
        loadWordsIntoMap(words4);
    }
    
    private void loadWords(String[] words) {
        for(String word:words) {
            testInstance.updateCounts(word);
        }
    }
    
    private void loadWordsIntoSet(String[] words) {
        getWordsExpectedResult.addAll(Arrays.asList(words));
    }
    
    private void loadWordsIntoMap(String[] words) {
        for (String word:words) {
            map.merge(word, 1.0, Double::sum);
            map.merge("TOTAL_WORDS", 1.0, Double::sum);
        }
    }

//    @Test
//    public void testUpdateCounts_WordCounter() {
//        System.out.println("updateCounts");
//        WordCounter wordCounter = null;
//        WordCounter instance = new WordCounter();
//        instance.updateCounts(wordCounter);
//        fail("The test case is a prototype.");
//    }

    @Test
    public void testGetNumWords() {
        System.out.println("getNumWords");
        int expResult = 38;
        int result = testInstance.getNumWords();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCount() {
        System.out.println("getCount");
        String word = "the";
        int expResult = 7;
        int result = testInstance.getCount(word);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetProb() {
        System.out.println("getProb");
        String word = "the";
        double expResult = 0.18421052631578946;
        double result = testInstance.getProb(word);
        assertEquals(expResult, result, 1e-9);
    }

    @Test
    public void testGetLaplaseProb() {
        System.out.println("getLaplaseProb");
        String word = "the";
        Optional<Double> expResult = Optional.of(0.12698412698412698);
        Optional<Double> result = testInstance.getLaplaseProb(word);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetWords() {
        System.out.println("getWords");
        Set<String> result = testInstance.getWords();
        assertEquals(getWordsExpectedResult, result);
    }

}
