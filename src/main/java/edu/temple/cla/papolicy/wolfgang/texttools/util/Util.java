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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

/**
 * Class to contain static utility methods
 *
 * @author Paul Wolfgang
 */
public class Util {

    private static final Logger LOGGER = Logger.getLogger(Util.class);
    private static final double LOGE2 = Math.log(2.0);
    private static final Map<Character, String> XML_CHAR_MAP = new HashMap<>();
    private static final Map<String, Character> XML_CODE_MAP = new HashMap<>();

    static {
        String[][] xmlTransTable = new String[][]{
            {"<", "&lt;"},
            {">", "&gt;"},
            {"&", "&amp;"},
            {"\"", "&quot;"},
            {"'", "&apos;"}};
        for (String[] entry : xmlTransTable) {
            XML_CHAR_MAP.put(entry[0].charAt(0), entry[1]);
            XML_CODE_MAP.put(entry[1], entry[0].charAt(0));
        }
    }

    /**
     * Method to reads samples from a file. Each input line consists of the
     * Category followed by the text. These are separated by a '|' character.
     *
     * @param inputFileName The name of the input file
     * @param lines The ArrayList to contain the text lines
     * @param ref The ArrayList to contain the categories
     */
    public static void readFromFile(
            String inputFileName,
            List<String> lines,
            List<String> ref) {
        lines.clear();
        ref.clear();
        try (BufferedReader in
                = new BufferedReader(new FileReader(inputFileName))) {
            in.lines().forEach(line -> {
                String[] tokens = line.split("\\s*\\|\\s*");
                if (tokens.length >= 2) {
                    ref.add(tokens[0].trim());
                    lines.add(tokens[1].trim());
                }
            });
        } catch (Exception ex) {
            LOGGER.error("Error reading from file", ex);
            System.err.printf("Error reading from file %s%n", ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Method to read samples from a database. Version used by CommonFrontEnd.
     *
     * @param datasource The datasource referencing the database
     * @param tableName The name of the table
     * @param idColumn The column containing the unique ID
     * @param textColumn The column(s) containing the text
     * @param codeColumn The column containing the code
     * @param computeMajor If true, the major code is computed
     * @param useEven If true, even samples are returned
     * @param useOdd If true, odd samples are returned
     * @return A Stream of selected rows as a Map from column name to value.
     */
    public static Stream<Map<String, Object>> readFromDatabase(
            String datasource,
            String tableName,
            String idColumn,
            String textColumn,
            String codeColumn,
            boolean computeMajor,
            boolean excluedNull,
            boolean useEven,
            boolean useOdd) {
        return readFromDatabase (datasource, tableName, idColumn, textColumn, 
                codeColumn, computeMajor, excluedNull, useEven, useOdd, null);
    }
    
    /**
     * Method to read samples from a database. Version for use by
     * FindNearDuplicateClusters.
     *
     * @param datasource The datasource referencing the database
     * @param tableName The name of the table
     * @param idColumn The column containing the unique ID
     * @param textColumn The column(s) containing the text
     * @param codeColumn The column containing the code
     * @param clusterColumn The column containing the cluster history
     * @param excludeNull If true, rows with a null code value are filtered out.
     * @param computeMajor If true, the major code is computed
     * @param useEven If true, even samples are returned
     * @param useOdd If true, odd samples are returned
     * @return A Stream of selected rows as a Map from column name to value.
     */
    public static Stream<Map<String, Object>>  readFromDatabase(
            String datasource,
            String tableName,
            String idColumn,
            String textColumn,
            String codeColumn,
            boolean computeMajor,
            boolean excludeNull,
            boolean useEven,
            boolean useOdd,
            String clusterColumn) {
        String query;
        if (clusterColumn == null) {
            query = "SELECT ("
                + idColumn + ") as theID, ("
                + textColumn + ") as theText, ("
                + codeColumn + ") as theCode FROM ("
                + tableName + ")";
        } else {
            query = "SELECT ("
                + idColumn + ") as theID, ("
                + textColumn + ") as theText, ("
                + codeColumn + ") as theCode, ("
                + clusterColumn + ") FROM ("
                + tableName + ")";
        }

        try {
            SimpleDataSource sds = new SimpleDataSource(datasource);
            DatabaseStream dbStream = new DatabaseStream(sds);
            System.out.println(query);
            EvenOddFilter evenOddFilter;
            if (!useEven && !useOdd) {
                evenOddFilter = new EvenOddFilter(EvenOddFilter.TYPE.BOTH);
            } else if (useEven && !useOdd) {
                evenOddFilter = new EvenOddFilter(EvenOddFilter.TYPE.EVEN);
            } else if (!useEven && useOdd) {
                evenOddFilter = new EvenOddFilter(EvenOddFilter.TYPE.ODD);
            } else {
                throw new RuntimeException("Cannot Select both UseEven and UseOdd");
            }
            Function<Map<String, Object>, Map<String, Object>> doComputeMajor;
            if (computeMajor) {
                doComputeMajor = (m -> {
                    Integer code = (Integer) m.get("theCode");
                    if (code == null) return m;
                    m.put("theCode", code / 100);
                    return m;
                });
            } else {
                doComputeMajor = (m -> m);
            }
            Predicate<Map<String, Object>> doExcludeNull;
            if (excludeNull) {
                doExcludeNull = m -> m.get("theCode") != null;
            } else {
                doExcludeNull = m -> true;
            }
            return dbStream.of(query)
                    .filter(m -> m.get("theText") != null)
                    .filter(m -> !"".equals(m.get("theText")))
                    .filter(evenOddFilter)
                    .filter(doExcludeNull)
                    .map(doComputeMajor);
        } catch (Exception ex) { // Want to catch unchecked exceptions as well
            LOGGER.error("Error reading from database", ex);
            System.err.printf("Error reading from database %s%n", ex.getMessage());
            throw new RuntimeException("Error reading from database", ex);
        }
    }


    /**
     * Method to compute attribute values. The attribute values is the
     * log2(p(w|t)/p(w)) where p(w|t) is the probability of the word in the text
     * sample, and p(w) is the probability of the word over all of the training
     * samples.
     *
     * @param counter WordCounter for the sample
     * @param vocab Vocabulary of the training data
     * @param logCutoff The minimum value of an attribute
     * @return A sorted map from wordIDs to attribute value
     */
    public static SortedMap<Integer, Double> computeAttributes(
            WordCounter counter,
            Vocabulary vocab,
            double logCutoff) {
        SortedMap<Integer, Double> result = new TreeMap<>();
        Set<String> words = counter.getWords();
        words.forEach(word -> {
            Integer wordID = vocab.getWordId(word);
            if (wordID != null) {
                double pWgivenT = counter.getProb(word);
                double pW = vocab.getWordProb(wordID);
                double attribute = Math.log(pWgivenT / pW) / LOGE2;
                if (attribute > logCutoff) {
                    result.put(wordID, attribute);
                }
            }
        });
        return result;
    }

    /**
     * Method to write a feature line. A feature line begins with the value (+1,
     * -1, or 0) followed by feature pairs. Each feature pair is a feature
     * number followed by a colon and then a feature value
     *
     * @param out The print writer to write the line to
     * @param value The value of this featureMap
     * @param featureMap The SortedMap containing the features
     */
    public static void writeFeatureLine(
            PrintWriter out,
            int value,
            SortedMap<Integer, Double> featureMap) {
        out.print(value);
        featureMap.forEach((k, v) -> out.print(" " + k + ":" + v));
        out.println();
    }

    /**
     * Method to recursively delete a directory
     *
     * @param dir The directory to be deleted
     */
    public static void delDir(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();
            for (String childName : children) {
                File child = new File(dir, childName);
                if (child.exists()) {
                    if (child.isDirectory()) {
                        delDir(child);
                    } else {
                        child.delete();
                    }
                }
            }
            dir.delete();
        }
    }

    /**
     * Method to remove multiple spaces and replace with a single space
     *
     * @param line The line containing the text
     * @return The line with extra spaces removed
     */
    public static String removeExtraSpaces(String line) {
        StringBuilder result = new StringBuilder();
        boolean seenSpace = false;
        for (int i = 0; i < line.length(); i++) {
            Character c = line.charAt(i);
            if (Character.isWhitespace(c)) {
                if (!seenSpace) {
                    result.append(' ');
                    seenSpace = true;
                }
            } else {
                if (seenSpace) {
                    seenSpace = false;
                }
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Method to convert string content to XML format.
     *
     * @param source The source string
     * @return The converted string
     */
    public static String convertToXML(String source) {
        if (source == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String replacement = XML_CHAR_MAP.get(c);
            if (replacement != null) {
                result.append(replacement);
            } else if (c > 127) {
                replacement = String.format("&#%d;", (int) c);
                result.append(replacement);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Method to convert string content from XML format.
     *
     * @param source The source string
     * @return The converted string
     */
    public static String convertFromXML(String source) {
        if (source == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (c == '&') {
                int nextSemi = source.indexOf(";", i);
                if (nextSemi != -1) {
                    String code = source.substring(i, nextSemi + 1);
                    Character cprime = XML_CODE_MAP.get(code);
                    if (cprime != null) {
                        result.append(cprime);
                        i = nextSemi;
                    } else {
                        code = code.substring(2, code.length() - 1);
                        try {
                            cprime = (char) Integer.parseInt(code);
                            result.append(cprime);
                            i = nextSemi;
                        } catch (NumberFormatException ex) {
                            result.append(c);
                        }
                    }
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Method to compute the innerproduct of two attribute sets. The attribute
     * sets are represented as sortedMaps from feature index to feature value.
     * The innerproduct is the sum of the product of the feature values where
     * the feature indices are equal.
     *
     * @param x The first attribute set
     * @param y The second attribute set
     * @return the innerproduct
     */
    public static double innerProduct(SortedMap<Integer, Double> x,
            SortedMap<Integer, Double> y) {
        double result = 0.0;
        Iterator<Map.Entry<Integer, Double>> itrx = x.entrySet().iterator();
        Iterator<Map.Entry<Integer, Double>> itry = y.entrySet().iterator();
        Map.Entry<Integer, Double> xEntry = null;
        Map.Entry<Integer, Double> yEntry = null;
        while (itrx.hasNext() & itry.hasNext()) {
            if (xEntry == null) {
                xEntry = itrx.next();
            }
            if (yEntry == null) {
                yEntry = itry.next();
            }
            if (xEntry.getKey().compareTo(yEntry.getKey()) < 0) {
                xEntry = null;
            } else if (xEntry.getKey().compareTo(yEntry.getKey()) > 0) {
                yEntry = null;
            } else {
                result += xEntry.getValue() * yEntry.getValue();
                xEntry = null;
                yEntry = null;
            }
        }
        return result;
    }


    public static void outputFile(File modelParent, String name, Object object) {
        File outFile = new File(modelParent, name);
        try (final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outFile))) {
            oos.writeObject(object);
        } catch (Exception ex) {
            throw new RuntimeException("Error writing " + outFile.getPath(), ex);
        }
    }

    public static Object readFile(File modelParent, String name) {
        File inputFile = new File(modelParent, name);
        try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile))) {
            return ois.readObject();
        } catch (ClassNotFoundException | IOException ioex) {
            throw new RuntimeException("Error reading " + inputFile.getPath(), ioex);
        }
    }
    
    public static void copySourceFile(InputStream ins, File outFile) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(ins));
            PrintWriter out = new PrintWriter(new FileWriter(outFile))) {
                in.lines().forEach(out::println);
        } catch (IOException ioex) {
            throw new UncheckedIOException(ioex);
        }
    }

}
