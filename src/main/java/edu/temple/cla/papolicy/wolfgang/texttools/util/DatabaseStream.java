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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.sql.DataSource;

/**
 * Convert a ResultSet into a Stream. The resulting Stream consists of
 * Map&lt;String, Object&gt; object for each row where the key is the column
 * label and the value is the value.
 *
 * @author Paul Wolfgang
 */
public class DatabaseStream {

    private DataSource dataSource;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    public DatabaseStream(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
        } catch (SQLException sqlex) {
            throw new RuntimeException(sqlex);
        }
    }
    
    /**
     * An iterator over the rows of a ResultSet.
     */
    private static class ResultSetIterator implements Iterator<Map<String, Object>> {

        private ResultSet rs;
        private boolean nextCalled;
        private boolean thereIsANext;
        private ResultSetMetaData rsMetaData;

        /**
         * Constructor. The constructor initializes the iterator by getting the
         * MetaData from the ResultSet and initializing the state variables.
         *
         * @param rs
         */
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

        /**
         * Determine if there is a next row. This method calls the ResultSet
         * next method if the Iterator next method has not been called. If the
         * ResultSet next method returns false, then thereIsANext is set false
         * and false is returned. Otherwise, thereIsANext is set true and true
         * is returned. In both cases nextCalled is set true.
         *
         * @return true if there is a call to next will succeed.
         */
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

        /**
         * Retrieve the next row from the ResultSet. A new LinkedHashMap is
         * created and the value of each column as defined by the
         * ResultSetMetaData is placed into the map.
         *
         * @return a Map representation of the row&apos;s content.
         */
        @Override
        public Map<String, Object> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            try {
                Map<String, Object> result = new LinkedHashMap<>();
                int numColumns = rsMetaData.getColumnCount();
                for (int i = 1; i <= numColumns; i++) {
                    String columnName = rsMetaData.getColumnLabel(i);
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

    /**
     * Create a stream from a ResultSet
     *
     * @param query The query to create the result set.
     * @return A Stream that returns each row of the ResultSet as a Map.
     */
    public Stream<Map<String, Object>> of(String query) {
        try {
            rs = stmt.executeQuery(query);
            Iterator<Map<String, Object>> iterator = new ResultSetIterator(rs);
            Spliterator<Map<String, Object>> spliterator
                    = Spliterators.spliteratorUnknownSize(iterator, 0);
            Stream<Map<String, Object>> stream = StreamSupport.stream(spliterator, false);
            stream.onClose(() -> {
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            return stream;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private DatabaseStream() {
    }

}
