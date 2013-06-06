package com.aiaraldea.gizartesegurantzawriter.sepe;

import com.aiaraldea.gizartesegurantzawriter.ss.SSEntry;
import com.aiaraldea.gizartesegurantzawriter.ss.SSLastDayOfMonthByCouncilParser;
import com.aiaraldea.gizartesegurantzawriter.ss.web.ListLinks;
import com.aiaraldea.gizartesegurantzawriter.writer.OutputWriter;
import com.aiaraldea.gizartesegurantzawriter.writer.OutputWriterFactory;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author inaki
 */
public class FileRetriever {

    public static void loadSSDataInet() throws IOException {
        OutputWriter writer = OutputWriterFactory.getOutputWriter("SepeLastDayOfMonthByCouncil");
        SSLastDayOfMonthByCouncilParser parser = new SSLastDayOfMonthByCouncilParser();
        String url = "http://www.sepe.es/contenido/estadisticas/datos_estadisticos/municipios/";
        Collection<String> urls = ListLinks.fetchLinks(url);
        ListLinks.fetchUnfetchedFiles(urls, parser);
        Iterator<SSEntry> iterator = parser.getParsed().iterator();
        while (iterator.hasNext()) {
            SSEntry sSEntry = iterator.next();
            writer.write(sSEntry);
        }
//            System.err.println("Inserting " + string);
        writer.flush();
    }
}
