/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cis.wolfgang.util;

import edu.temple.cla.papolicy.wolfgang.texttools.util.StopWord;
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
        assertEquals(expResult, result);
    }

    /**
     * Test of isStopWord method, of class StopWord.
     * arf is not a stop word
     */
    @Test
    public void testIsStopWord2() {
        System.out.println("isStopWord2");
        String word = "arf";
        boolean expResult = false;
        boolean result = stopWord.isStopWord(word);
        assertEquals(expResult, result);
    }
}