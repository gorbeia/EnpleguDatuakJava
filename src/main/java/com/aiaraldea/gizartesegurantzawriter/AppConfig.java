package com.aiaraldea.gizartesegurantzawriter;

import com.aiaraldea.gizartesegurantzawriter.writer.MongoDBUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author inaki
 */
public class AppConfig {

    private MongoDBUtil.MongoDBConfig mongoDBConfig;
    protected static final String PROPERTY_NAME = "appConfigFilename";
    private String readFilesPath;

    public AppConfig() {
        try {
            InputStream configStream;
            String configFilename = System.getProperty(PROPERTY_NAME);
            if (configFilename == null) {
                configStream = this.getClass().getResourceAsStream("/configuration.properties");
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
        } catch (IOException ex) {
            Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("IO Error", ex);
        }
    }

    public MongoDBUtil.MongoDBConfig getMongoDBConfig() {
        return mongoDBConfig;
    }

    public String getReadFilesPath() {
        return readFilesPath;
    }
}
