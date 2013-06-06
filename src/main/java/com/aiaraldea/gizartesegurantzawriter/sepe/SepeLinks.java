package com.aiaraldea.gizartesegurantzawriter.sepe;

import com.aiaraldea.gizartesegurantzawriter.ss.web.*;
import com.aiaraldea.gizartesegurantzawriter.AppConfig;
import com.aiaraldea.gizartesegurantzawriter.writer.OutputWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class SepeLinks {

    public static Collection<String> fetchLinks(String url) throws IOException {
        // TODO I should save the already fetched pages here instead the alaready fetched files.
        List<String> urls = new ArrayList<>();
        print("Fetching %s...", url);
        Collection<String> readFiles = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.reader();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String readFilesPath = AppConfig.getReadFilesPathSepe();
        File file = new File(readFilesPath);

        JsonNode readTree;
        try {
            InputStream is = new FileInputStream(file);
            readTree = reader.readTree(is);
            Iterator<JsonNode> elements = readTree.getElements();
            while (elements.hasNext()) {
                JsonNode jsonNode = elements.next();
                String asText = jsonNode.asText();
//                urls.remove(asText);
                readFiles.add(asText);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ListLinks.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        try {
            for (Element link : links) {
                if (link.attr("abs:href").startsWith("http://www.sepe.es/contenido/estadisticas/datos_estadisticos/municipios/")
                        && !link.attr("abs:href").startsWith("http://www.sepe.es/contenido/estadisticas/datos_estadisticos/municipios/#")
                        && !readFiles.contains(link.attr("abs:href"))) {
                    String url2 = link.attr("abs:href");
                    Document doc2 = Jsoup.connect(url2).get();
                    Elements links2 = doc2.select("a[href]");
                    for (Element link2 : links2) {
                        if (link2.attr("abs:href").startsWith("http://www.sepe.es/contenido/estadisticas/datos_estadisticos/municipios/")
                                && link2.attr("abs:href").endsWith(".xls")
                                && link2.attr("abs:href").contains("MUNI_")) {
                            print(" * a: <%s>  (%s)", link2.attr("abs:href"), trim(link2.text(), 35));
                            urls.add(link2.attr("abs:href"));
                        }
                    }
                    readFiles.add(url2);
                }
            }
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            mapper.writer().withDefaultPrettyPrinter();
            writer.writeValue(file, readFiles);
        }
        return urls;
    }

    private static void print(String msg, Object... args) {
        System.err.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width) {
            return s.substring(0, width - 1) + ".";
        } else {
            return s;
        }
    }

    public static void fetchUnfetchedFiles(Collection<String> urls, SepeLoader parser, OutputWriter w) throws IOException {

        try {
            for (String url : urls) {
                //Open a URL Stream
                System.err.println("dowwnloading " + url);
                Response resultImageResponse = Jsoup.connect(url).ignoreContentType(true).execute();
                Set<MunicipalityData> data = parser.parse(new ByteArrayInputStream(resultImageResponse.bodyAsBytes()));
                w.write(data);
            }
        } catch (IOException ex) {
            Logger.getLogger(ListLinks.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
