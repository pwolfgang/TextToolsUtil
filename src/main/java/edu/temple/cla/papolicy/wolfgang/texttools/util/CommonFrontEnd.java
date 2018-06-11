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

import java.util.List;
import picocli.CommandLine.Option;

/**
 *
 * @author Paul
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

    @Option(names = "--model", description = "Directory where model files are written")
    private String modelOutput = "SVM_Model_Dir";

    @Option(names = "--feature_dir", description = "Directory where feature files are written")
    private String featureDir = "SVM_Training_Features";

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

    @Option(names = "--output_vocab", description = "File where vocabulary is written")
    private String outputVocab;

    public void loadData(List<String> ids, List<String> ref, Vocabulary vocabulary, List<WordCounter> counts) {
        Preprocessor preprocessor = new Preprocessor(doStemming, removeStopWords);
        Util.readFromDatabase(dataSourceFileName,
                tableName,
                idColumn,
                textColumn,
                codeColumn,
                computeMajor,
                useEven,
                useOdd)
                .forEach(m -> {
                    ids.add((String) m.get("theID"));
                    ref.add(m.get("theCode").toString());
                    String line = (String) m.get("theText");
                    WordCounter counter = new WordCounter();
                    preprocessor.preprocess(line)
                            .forEach(word -> {
                                counter.updateCounts(word);
                                vocabulary.updateCounts(word);
                            });
                    counts.add(counter);
                });
    }
}
