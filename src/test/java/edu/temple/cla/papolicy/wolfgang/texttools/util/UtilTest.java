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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    
    @Test
    public void testReadFromDatabaseMajorCode() {
        System.out.println("testReadFromDatabaseMajorCode");
        TestDatabase.createTestTable();
        List<Map<String, Object>> result = new ArrayList<>();
        Util.readFromDatabase("MockDB.txt", "TestTable", "ID", "Abstract", "Code", true, false, false)
                .forEach(m -> result.add(m));
        assertEquals(TestDatabase.buildExpectedResultMajorCode(), result);
    }
    
    @Test
    public void testReadFromDatabaseCode() {
        System.out.println("testReadFromDatabaseCode");
        TestDatabase.createTestTable();
        List<Map<String, Object>> result = new ArrayList<>();
        Util.readFromDatabase("MockDB.txt", "TestTable", "ID", "Abstract", "Code", false, false, false)
                .forEach(m -> result.add(m));
        assertEquals(TestDatabase.buildExpectedResultMinorCode(), result);
    }

}
