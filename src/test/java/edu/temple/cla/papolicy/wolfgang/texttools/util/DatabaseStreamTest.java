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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Paul
 */
public class DatabaseStreamTest {

    public DatabaseStreamTest() {
    }

    @BeforeClass
    public static void beforeClass() {
        TestDatabase.createTestTable();
    }


    @Test
    public void testOf() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testdb", "paul", "secret");
                Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery("SELECT ID, Abstract, Code from TestTable");
            List<Map<String, Object>> result = new ArrayList<>();
            DatabaseStream.of(rs)
                    .forEach(result::add);
            List<Map<String, Object>> expected = TestDatabase.buildExpectedResult();
            assertTrue(compareLists(expected, result));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
    
    
    private boolean compareLists(List<?> list1, List<?> list2) {
        if (list1.size() != list2.size()) {
            System.err.println("Lists are of different size");
        }
        for (int i = 0; i < list1.size(); i++) {
            Object o1 = list1.get(i);
            Object o2 = list2.get(i);
            if (!Objects.equals(o1, o2)) {
                System.err.println("At item " + 1);
                System.err.println(o1);
                System.err.println(o2);
                return false;
            }
        }
        return true;
    }

}
