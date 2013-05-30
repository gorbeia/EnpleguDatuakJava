package com.aiaraldea.gizartesegurantzawriter.ss.web;

import com.aiaraldea.gizartesegurantzawriter.AppConfig;
import com.aiaraldea.gizartesegurantzawriter.ss.SSLastDayOfMonthByCouncilParser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author inaki
 */
public class ListLinks {

    public static Collection<String> fetchLinks(String url) throws IOException {
        List<String> urls = new ArrayList<>();
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        for (Element link : links) {
            if (link.attr("abs:href").startsWith("http://www.seg-social.es/prdi00/idcpl")) {
                print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
                urls.add(link.attr("abs:href"));
            }
        }
        return urls;
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width) {
            return s.substring(0, width - 1) + ".";
        } else {
            return s;
        }
    }

    private static void fetchFiles(Collection<String> urls) {
        int i = 0;
        for (String url : urls) {
            try {
                //Open a URL Stream
                System.out.println("dowwnloading " + url);
                Response resultImageResponse = Jsoup.connect(url).ignoreContentType(true).execute();
                System.out.println("contentType: " + resultImageResponse.contentType());
                System.out.println("Content-disposition: " + resultImageResponse.header("Content-disposition"));
                String[] split = resultImageResponse.header("Content-disposition").split("\"");
                if (split.length > 1) {
                    try (FileOutputStream out = new FileOutputStream(new java.io.File("/tmp/" + split[1]))) {
                        out.write(resultImageResponse.bodyAsBytes());
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ListLinks.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public static void fetchUnfetchedFiles(Collection<String> urls, SSLastDayOfMonthByCouncilParser parser) throws IOException {
        AppConfig config = new AppConfig();
        String readFilesPath = config.getReadFilesPath();
        Collection<String> readFiles = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        ObjectReader reader = mapper.reader();
        File file = new File(readFilesPath);
        JsonNode readTree;
        try {
            InputStream is = new FileInputStream(file);
            readTree = reader.readTree(is);
            Iterator<JsonNode> elements = readTree.getElements();
            while (elements.hasNext()) {
                JsonNode jsonNode = elements.next();
                String asText = jsonNode.asText();
                urls.remove(asText);
                readFiles.add(asText);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ListLinks.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            for (String url : urls) {
                //Open a URL Stream
                System.out.println("dowwnloading " + url);
                Response resultImageResponse = Jsoup.connect(url).ignoreContentType(true).execute();
                String[] split = resultImageResponse.header("Content-disposition").split("\"");
                if (split.length > 1) {
                    try (FileOutputStream out = new FileOutputStream(new java.io.File("/tmp/" + split[1]))) {
                        parser.parse(new ByteArrayInputStream(resultImageResponse.bodyAsBytes()));
                        readFiles.add(url);
                    }
                }
            }
        } catch (InvalidFormatException | IOException ex) {
            Logger.getLogger(ListLinks.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } finally {
            mapper.writer().withDefaultPrettyPrinter();
            writer.writeValue(file, readFiles);
        }
    }
}
