/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cis.wolfgang.util;

import edu.temple.cla.papolicy.wolfgang.texttools.util.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class UtilTest {
    
    public UtilTest() {
    }


    @Test
    public void testConvertToXML() {
        System.out.println("convertToXML");
        String source = "The \"quick\" fox's are < “fast” dogs";
        String expResult = "The &quot;quick&quot; fox&apos;s are &lt; &#8220;fast&#8221; dogs";
        String result = Util.convertToXML(source);
        assertEquals(expResult, result);
    }

    @Test
    public void testConvertFromXML() {
        System.out.println("convertFromXML");
        String source = " A Resolution recognizing April 26, 2010, as &quot;National Healthy Schools Day&quot; in Pennsylvania. ";
        String expResult = " A Resolution recognizing April 26, 2010, as \"National Healthy Schools Day\" in Pennsylvania. ";
        String result = Util.convertFromXML(source);
        assertEquals(expResult, result);
    }

}
