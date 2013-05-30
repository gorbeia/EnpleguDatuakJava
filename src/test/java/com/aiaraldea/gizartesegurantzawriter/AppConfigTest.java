package com.aiaraldea.gizartesegurantzawriter;

import com.aiaraldea.gizartesegurantzawriter.writer.MongoDBUtil;
import java.net.URL;
import junit.framework.TestCase;

/**
 *
 * @author inaki
 */
public class AppConfigTest extends TestCase {
    
    public AppConfigTest(String testName) {
        super(testName);
    }

    /**
     * Test of getMongoDBConfig method, of class AppConfig.
     */
    public void testGetMongoDBConfig() {
        System.out.println("getMongoDBConfig");
        AppConfig instance = new AppConfig();
        MongoDBUtil.MongoDBConfig result = instance.getMongoDBConfig();
        assertEquals("dataBaseName", result.getDataBaseName());
        assertEquals(111, result.getPort());
        assertEquals("mongodb.url", result.getUrl());
        assertEquals("username", result.getUsername());
        assertEquals("password", result.getPassword());
    }
    public void testGetMongoDBConfigProperty() {
        System.out.println("getMongoDBConfig");
        
        URL url = this.getClass().getResource("/testConfiguration.properties");
        
        System.setProperty(AppConfig.PROPERTY_NAME, url.getPath());
        AppConfig instance = new AppConfig();
        MongoDBUtil.MongoDBConfig result = instance.getMongoDBConfig();
        assertEquals("dataBaseName.test", result.getDataBaseName());
        assertEquals(222, result.getPort());
        assertEquals("mongodb.url.test", result.getUrl());
        assertEquals("username.test", result.getUsername());
        assertEquals("password.test", result.getPassword());
    }
}
