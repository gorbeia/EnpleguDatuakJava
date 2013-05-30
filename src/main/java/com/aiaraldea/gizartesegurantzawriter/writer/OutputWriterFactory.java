package com.aiaraldea.gizartesegurantzawriter.writer;

/**
 *
 * @author inaki
 */
public class OutputWriterFactory {

    static final String PROPERTY_NAME = "outputWriter";
    static final String PROPERTY_VALUE_SIMPLE = "simple";

    public static OutputWriter getOutputWriter(String collectionName) {
        String outputWriter = System.getProperty(PROPERTY_NAME);
        if (PROPERTY_VALUE_SIMPLE.equals(outputWriter)) {
            return new SimpleOutputWriter();
        }
        return new MongoDBWriter(collectionName);
    }
}
