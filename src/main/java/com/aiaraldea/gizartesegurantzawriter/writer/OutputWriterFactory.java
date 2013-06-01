package com.aiaraldea.gizartesegurantzawriter.writer;

import com.aiaraldea.gizartesegurantzawriter.AppConfig;

/**
 *
 * @author inaki
 */
public class OutputWriterFactory {

    public static final String PROPERTY_VALUE_SIMPLE = "simple";

    public static OutputWriter getOutputWriter(String collectionName) {
        if (PROPERTY_VALUE_SIMPLE.equals(AppConfig.getOutputWriter())) {
            return new SimpleOutputWriter();
        }
        return new MongoDBWriter(collectionName);
    }
}
