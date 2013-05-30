package com.aiaraldea.gizartesegurantzawriter.writer;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author inaki
 */
public class MongoDBUtil {

    private MongoDBConfig config;
    private MongoClient mongoClient;
    private DB db;

    public MongoDBUtil(MongoDBConfig config) {
        this.config = config;
        try {
            mongoClient = new MongoClient(config.getUrl(), config.getPort());
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoDBUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Unable to connect to host", ex);
        }
    }

    public void authenticate() {
        db = mongoClient.getDB(config.getDataBaseName());
        boolean auth = db.authenticate(
                config.getUsername(),
                config.getPassword().toCharArray());
        if (auth == false) {
            throw new RuntimeException("Unable to authenticate");
        }
    }

    public DBCollection getCollection(String collectionName) {
        return db.getCollection(collectionName);
    }

    public static class MongoDBConfig {

        private String url;
        private int port;
        private String dataBaseName;
        private String username;
        private String password;

        public MongoDBConfig(Properties prop) {
            url = prop.getProperty("mongodb.url");
            port = Integer.parseInt(prop.getProperty("mongodb.port"));
            dataBaseName = prop.getProperty("mongodb.databaseName");
            username = prop.getProperty("mongodb.username");
            password = prop.getProperty("mongodb.password");
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDataBaseName() {
            return dataBaseName;
        }

        public void setDataBaseName(String dataBaseName) {
            this.dataBaseName = dataBaseName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
