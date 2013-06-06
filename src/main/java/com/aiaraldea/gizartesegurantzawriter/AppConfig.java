package com.aiaraldea.gizartesegurantzawriter;

import com.aiaraldea.gizartesegurantzawriter.writer.MongoDBUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author inaki
 */
public class AppConfig {

    private static MongoDBUtil.MongoDBConfig mongoDBConfig;
    protected static final String PROPERTY_NAME = "appConfigFilename";
    private static String readFilesPath;
    private static String readFilesPathSepe;
    private static String outputWriter;
    private static List<String> filePaths;

    public static void init() {
        init(null);
    }

    public static void init(String configFilename) {
        try {
            InputStream configStream;
            if (configFilename == null) {
                configStream = AppConfig.class.getResourceAsStream("/configuration.properties");
            } else {
                try {
                    configStream = new FileInputStream(configFilename);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException("File not found", ex);
                }
            }
            Properties prop = new Properties();
            prop.load(configStream);
            mongoDBConfig = new MongoDBUtil.MongoDBConfig(prop);
            readFilesPath = prop.getProperty("filesAlreadyParsed");
            readFilesPathSepe = prop.getProperty("filesAlreadyParsedSepe");
        } catch (IOException ex) {
            Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("IO Error", ex);
        }
    }

    public static MongoDBUtil.MongoDBConfig getMongoDBConfig() {
        return mongoDBConfig;
    }

    public static String getReadFilesPath() {
        return readFilesPath;
    }

    public static void setFilePaths(List<String> filePaths) {
        AppConfig.filePaths = filePaths;
    }

    public static String getReadFilesPathSepe() {
        return readFilesPathSepe;
    }

    public static void setReadFilesPathSepe(String readFilesPathSepe) {
        AppConfig.readFilesPathSepe = readFilesPathSepe;
    }

    public static List<String> getFilePaths() {
        return filePaths;
    }

    public static String getOutputWriter() {
        return outputWriter;
    }

    public static void setOutputWriter(String outputWriter) {
        AppConfig.outputWriter = outputWriter;
    }
}
