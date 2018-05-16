/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cis.wolfgang.util;

import edu.temple.cla.papolicy.wolfgang.texttools.util.Preprocessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        String line1 = "The quick brown fox can't jump";
        List<String> expResult1 = 
                new ArrayList<>(Arrays.asList("the", "quick", "brown", "fox", "can't", "jump"));
        String line2 = "Proposición de Ley sobre";
        List<String> expResult2 =
                new ArrayList<>(Arrays.asList("proposición", "de", "ley", "sobre"));
        Preprocessor instance = new Preprocessor("false", "false");
        List<String> result1 = instance.preprocess(line1);
        List<String> result2 = instance.preprocess(line2);
        assertEquals(expResult1, result1);
        assertEquals(expResult2, result2);
    }

}