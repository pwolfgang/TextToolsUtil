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

import edu.temple.cla.papolicy.wolfgang.texttools.util.Preprocessor;
import edu.temple.cla.papolicy.wolfgang.texttools.util.Vocabulary;
import edu.temple.cla.papolicy.wolfgang.texttools.util.WordCounter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class PreprocessorTest {


    public PreprocessorTest() {
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

    @Test
    public void testPreprocess() {
        System.out.println("preprocess");
        String line1 = "'The quick brown fox can't jump'";
        List<String> expResult1 = 
                Arrays.asList("quick", "brown", "fox", "jump");
        String line2 = "Proposición de Ley sobre";
        List<String> expResult2 =
                Arrays.asList("proposición", "ley", "sobr");
        Preprocessor instance1 = new Preprocessor("porter", "english");
        Preprocessor instance2 = new Preprocessor("french", "french");
        List<String> result1 = instance1.preprocess(line1).collect(Collectors.toList());
        List<String> result2 = instance2.preprocess(line2).collect(Collectors.toList());
        assertEquals(expResult1, result1);
        assertEquals(expResult2, result2);
    }
    
    @Test
    public void testCreatingTrainingFeatures() {
        List<WordCounter> counts = new ArrayList<>();
        Vocabulary vocabulary = new Vocabulary();
        List<String> lines = Arrays.asList (
                "The quick brown fox jumps over the lazy dog.",
                "Now is the time for all good men to come to the aid of the party.",
                "The slow brown fox cannot jump over the fast dog.",
                "It's party time."
                );
        Preprocessor preprocessor = new Preprocessor("porter", "english");
        lines.stream()
             .map(line -> preprocessor.preprocess(line))
             .forEach(words -> {
                WordCounter counter = new WordCounter();
                words.forEach(word -> {
                    counter.updateCounts(word);
                    vocabulary.updateCounts(word);
                });
                counts.add(counter);
            });
        vocabulary.computeProbabilities();
        String wordCounterExpected = 
                "[{quick -> 1, lazi -> 1, brown -> 1, dog -> 1, fox -> 1, jump -> 1}\n" +
               ", {now -> 1, men -> 1, come -> 1, time -> 1, good -> 1, aid -> 1, parti -> 1}\n" +
               ", {fast -> 1, slow -> 1, brown -> 1, dog -> 1, fox -> 1, jump -> 1}\n" +
               ", {time -> 1, parti -> 1}\n" +
                "]";
        String vocabularyExpected = 
                "   1      quick 0.047619\n" +
                "   2      brown 0.095238\n" +
                "   3        fox 0.095238\n" +
                "   4       jump 0.095238\n" +
                "   5       lazi 0.047619\n" +
                "   6        dog 0.095238\n" +
                "   7        now 0.047619\n" +
                "   8       time 0.095238\n" +
                "   9       good 0.047619\n" +
                "  10        men 0.047619\n" +
                "  11       come 0.047619\n" +
                "  12        aid 0.047619\n" +
                "  13      parti 0.095238\n" +
                "  14       slow 0.047619\n" +
                "  15       fast 0.047619";
        assertEquals(wordCounterExpected, counts.toString());
        assertEquals(vocabularyExpected, vocabulary.toString());
    }
            

}