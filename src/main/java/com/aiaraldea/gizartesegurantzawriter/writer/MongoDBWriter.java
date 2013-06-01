package com.aiaraldea.gizartesegurantzawriter.writer;

import com.aiaraldea.gizartesegurantzawriter.AppConfig;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 *
 * @author inaki
 */
public class MongoDBWriter implements OutputWriter {

    private MongoDBUtil mongoDBUtil;
    private ObjectMapper mapper = new ObjectMapper();
    private ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
    private List<DBObject> documents = new ArrayList<>();
    private DBCollection coll;
    private final String collectionName;

    public MongoDBWriter(String collectionName) {
        this.collectionName = collectionName;
    }

    private void init() {
        if (coll == null) {
            mongoDBUtil = new MongoDBUtil(AppConfig.getMongoDBConfig());
            mongoDBUtil.authenticate();
            coll = mongoDBUtil.getCollection(collectionName);
        }
    }

    @Override
    public void write(Object o) {
        init();
        try {
            String jsonOutput = writer.writeValueAsString(o);
//            System.out.println(jsonOutput);
            DBObject document = (DBObject) JSON.parse(jsonOutput);
            documents.add(document);
        } catch (IOException ex) {
            Logger.getLogger(SimpleOutputWriter.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Unable to write", ex);
        }
    }

    @Override
    public void flush() {
        WriteResult insert = coll.insert(documents);
        documents.clear();
    }
}
