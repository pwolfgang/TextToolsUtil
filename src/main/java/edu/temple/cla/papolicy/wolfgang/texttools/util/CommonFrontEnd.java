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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import picocli.CommandLine.Option;

/**
 * Class to process selected command-line arguments and read the data from
 * a database and to update the classification codes.
 * @author Paul Wolfgang
 */
public class CommonFrontEnd {

    
    @Option(names = "--datasource", required = true, description = "File containing the datasource properties")
    private String dataSourceFileName;

    @Option(names = "--table_name", required = true, description = "The name of the table containing the data")
    private String tableName;

    @Option(names = "--id_column", required = true, description = "Column(s) containing the ID")
    private String idColumn;

    @Option(names = "--text_column", required = true, description = "Column(s) containing the text")
    private String textColumn;

    @Option(names = "--code_column", required = true, description = "Column(s) containing the code")
    private String codeColumn;

    @Option(names = "--use_even", description = "Use even numbered samples for training")
    private Boolean useEven = false;

    @Option(names = "--use_odd", description = "Use odd numbered samples for training")
    private Boolean useOdd = false;

    @Option(names = "--compute_major", description = "Major code is computed from minor code")
    private Boolean computeMajor = false;

    @Option(names = "--remove_stopwords", arity = "0..1", description = "Remove common \"stop words\" from the text.")
    private String removeStopWords;

    @Option(names = "--do_stemming", arity = "0..1", description = "Pass all words through stemming algorithm")
    private String doStemming;
    
    /**
     * Load the data from the database. This method reads training cases and
     * classification cases from the database. It then converts the text into
     * a WordCounter containing the individual filtered words. It also computes
     * a Vocabulary of all filtered words.
     * @param theData The individual training/classification cases.
     * @return The Vocabulary of all training/classification cases.
     */
    public Vocabulary loadData(List<Map<String, Object>> theData) {
        Preprocessor preprocessor = new Preprocessor(doStemming, removeStopWords);
        theData.clear();
        Map<String, Double> vocabCounts = new LinkedHashMap<>();
        Util.readFromDatabase(dataSourceFileName,
                tableName,
                idColumn,
                textColumn,
                codeColumn,
                computeMajor,
                useEven,
                useOdd)
                .forEach(m -> {
                    String line = (String) m.get("theText");
                    Map<String, Double> counts = new HashMap<>();
                    preprocessor.preprocess(line)
                            .peek(w -> {vocabCounts.merge(w, 1.0, Double::sum);
                                        vocabCounts.merge("TOTAL_WORDS", 1.0, Double::sum);
                            })
                            .forEach(w -> {counts.merge(w, 1.0, Double::sum);
                                           counts.merge("TOTAL_WORDS", 1.0, Double::sum);
                            });
                    m.put("counts", new WordCounter(counts));
                    m.remove("theText");
                    theData.add(m);
                });
        return new Vocabulary(new WordCounter(vocabCounts));
    }

    /**
     * Method to write the classification results to the database
     *
     * @param tableName The name of the table
     * @param outputCodeCol The column where the results are set
     * @param ids The list of ids
     * @param cats The corresponding list of categories.
     */
    public void outputToDatabase(String tableName, String outputCodeCol, List<String> ids, List<Integer> cats) {
        try {
            SimpleDataSource sds = new SimpleDataSource(dataSourceFileName);
            try (final Connection conn = sds.getConnection();
                    final Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DROP TABLE IF EXISTS NewCodes");
                stmt.executeUpdate("CREATE TABLE NewCodes (ID char(11) primary key, Code int)");
                StringBuilder stb = new StringBuilder("INSERT INTO NewCodes (ID, Code) VALUES");
                StringJoiner sj = new StringJoiner(",\n");
                for (int i = 0; i < ids.size(); i++) {
                    sj.add(String.format("('%s', %d)", ids.get(i), cats.get(i)));
                }
                stb.append(sj);
                stmt.executeUpdate(stb.toString());
                stmt.executeUpdate("UPDATE " + tableName + " join NewCodes on " 
                        + tableName + ".ID=NewCodes.ID SET " + tableName + "." 
                        + outputCodeCol + "=NewCodes.Code");
            } catch (SQLException ex) {
                throw ex;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * @return the dataSourceFileName
     */
    public String getDataSourceFileName() {
        return dataSourceFileName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @return the idColumn
     */
    public String getIdColumn() {
        return idColumn;
    }

    /**
     * @return the textColumn
     */
    public String getTextColumn() {
        return textColumn;
    }

    /**
     * @return the codeColumn
     */
    public String getCodeColumn() {
        return codeColumn;
    }

    /**
     * @return the useEven
     */
    public Boolean getUseEven() {
        return useEven;
    }

    /**
     * @return the useOdd
     */
    public Boolean getUseOdd() {
        return useOdd;
    }

    /**
     * @return the computeMajor
     */
    public Boolean getComputeMajor() {
        return computeMajor;
    }

    /**
     * @return the removeStopWords
     */
    public String getRemoveStopWords() {
        return removeStopWords;
    }

    /**
     * @return the doStemming
     */
    public String getDoStemming() {
        return doStemming;
    }
    
    
    
}
