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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author Paul Wolfgang
 */
public class DatabaseStream {

    private static class ResultSetIterator implements Iterator<Map<String, Object>> {

        private ResultSet rs;
        private boolean nextCalled;
        private boolean thereIsANext;
        private ResultSetMetaData rsMetaData;

        public ResultSetIterator(ResultSet rs) {
            try {
                this.rs = rs;
                rsMetaData = rs.getMetaData();
                nextCalled = false;
                thereIsANext = false;
            } catch (Exception ex) {
                throw new RuntimeException("Error in constructor call", ex);
            }
        }

        @Override
        public boolean hasNext() {
            try {
                if (nextCalled) {
                    return thereIsANext;
                } else {
                    if (rs.next()) {
                        nextCalled = true;
                        thereIsANext = true;
                        return true;
                    } else {
                        nextCalled = true;
                        thereIsANext = false;
                        return false;
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException("Error in calling next", ex);
            }
        }

        @Override
        public Map<String, Object> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            try {
                Map<String, Object> result = new LinkedHashMap<>();
                int numColumns = rsMetaData.getColumnCount();
                for (int i = 1; i <= numColumns; i++) {
                    String columnName = rsMetaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    result.put(columnName, columnValue);
                }
                nextCalled = false;
                return result;
            } catch (Exception ex) {
                throw new RuntimeException("Error in reading row", ex);
            }
        }

    }

    public static Stream<Map<String, Object>> of(ResultSet rs) {
        Iterator<Map<String, Object>> iterator = new ResultSetIterator(rs);
        Spliterator<Map<String, Object>> spliterator
                = Spliterators.spliteratorUnknownSize(iterator, 0);
        return StreamSupport.stream(spliterator, false);
    }
    
    private DatabaseStream() {}

}
