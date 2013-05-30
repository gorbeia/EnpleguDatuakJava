package com.aiaraldea.gizartesegurantzawriter.writer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 *
 * @author inaki
 */
public class SimpleOutputWriter implements OutputWriter {

    private ObjectMapper mapper = new ObjectMapper();
    private ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    @Override
    public void write(Object o) {
        try {
            String jsonOutput = writer.writeValueAsString(o);
            System.out.println(jsonOutput);
        } catch (IOException ex) {
            Logger.getLogger(SimpleOutputWriter.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Unable to write", ex);
        }
    }

    @Override
    public void flush() {
// already flushed
    }
}
