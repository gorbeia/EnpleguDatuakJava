package com.aiaraldea.gizartesegurantzawriter.councils;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author inaki
 */
public class CouncilCodeHelper {

    private Map<String, String> readTree;

    private void init() {
        try {
            InputStream resourceAsStream = CouncilCodeHelper.class.getResourceAsStream("/councilsMap.json");
            ObjectMapper mapper = new ObjectMapper();
            readTree = mapper.readValue(resourceAsStream, Map.class);
//            for (String string : readTree.keySet()) {
//                System.err.println(string);
//            }
        } catch (JsonParseException ex) {
            Logger.getLogger(CouncilCodeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonMappingException ex) {
            Logger.getLogger(CouncilCodeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CouncilCodeHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCode(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll("[.;]*", "").toLowerCase();

        if (readTree == null) {
            init();
        }
        String code = readTree.get(normalized);
//        normalized.replace('/', '-');
        if (code == null) {
            // Some council names separate the two language names by a dash.
            code = readTree.get(normalized.replace('/', '-'));
        }

        if (code == null) {
            // attempt to use the first part of the name.
            code = readTree.get(normalized.split("/")[0]);
        }
        if (code == null) {
            // attempt to use the first part of the name.
            code = readTree.get(normalized.split("-")[0]);
        }
        if (code == null) {
            // not yet? use the comma as separator.
            code = readTree.get(normalized.split(",")[0]);
        }
        if (code == null) {
            System.err.println("getCode TODO: " + normalized + " \nkey: " + readTree.get(normalized));
        }

        return code;
        /* TODO
         * Maybe special expections
         * Legutiano: Legutio
         * Garay: Garai
         */
    }
}
