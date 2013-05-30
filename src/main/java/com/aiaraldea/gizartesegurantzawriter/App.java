package com.aiaraldea.gizartesegurantzawriter;

import com.aiaraldea.gizartesegurantzawriter.writer.OutputWriter;
import com.aiaraldea.gizartesegurantzawriter.writer.OutputWriterFactory;
import com.aiaraldea.gizartesegurantzawriter.ss.SSEntry;
import com.aiaraldea.gizartesegurantzawriter.ss.SSLastDayOfMonthByCouncilParser;
import com.aiaraldea.gizartesegurantzawriter.councils.IneCouncilParser;
import com.aiaraldea.gizartesegurantzawriter.councils.Council;
import com.aiaraldea.gizartesegurantzawriter.ss.web.ListLinks;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidFormatException {
        String action = args[0];
        switch (action) {
            case "councils":
                loadCouncils(args);
                break;
            case "ssInet":
                loadSSDataInet();
                break;
            default:
                loadSSDAta(args);
                break;
        }
    }

    public static void loadSSDataInet() throws IOException {
        OutputWriter writer = OutputWriterFactory.getOutputWriter("SSLastDayOfMonthByCouncil");
        SSLastDayOfMonthByCouncilParser parser = new SSLastDayOfMonthByCouncilParser();
        String url = "http://www.seg-social.es/Internet_1/Estadistica/Est/AfiliacionAltaTrabajadores/AfiliacionesAltaLaboral/Afiliaci_n__ltimo_d_a_del_mes/AfiliacionMunicipios/index.htm";
        Collection<String> urls = ListLinks.fetchLinks(url);
        ListLinks.fetchUnfetchedFiles(urls, parser);
        Iterator<SSEntry> iterator = parser.getParsed().iterator();
        while (iterator.hasNext()) {
            SSEntry sSEntry = iterator.next();
            writer.write(sSEntry);
        }
//            System.out.println("Inserting " + string);
        writer.flush();
    }

    public static void loadSSDAta(String[] args) throws FileNotFoundException, IOException, InvalidFormatException {
        OutputWriter writer = OutputWriterFactory.getOutputWriter("SSLastDayOfMonthByCouncil");
        for (int i = 1; i < args.length; i++) {
            String string = args[i];
            FileInputStream file = new FileInputStream(new File(string));
            Set<SSEntry> parsed = new SSLastDayOfMonthByCouncilParser().parse(file);
            Iterator<SSEntry> iterator = parsed.iterator();
            while (iterator.hasNext()) {
                SSEntry sSEntry = iterator.next();
                writer.write(sSEntry);
            }
//            System.out.println("Inserting " + string);
            writer.flush();
        }
    }

    private static void loadCouncils(String[] args) throws FileNotFoundException, IOException, InvalidFormatException {
        System.out.println("loadCouncils");
        OutputWriter writer = OutputWriterFactory.getOutputWriter("IneCouncils");
        for (int i = 1; i < args.length; i++) {
            System.out.println(i);
            String string = args[i];
            FileInputStream file = new FileInputStream(new File(string));
            Set<Council> parsed = IneCouncilParser.parse(file);
            Iterator<Council> iterator = parsed.iterator();
            while (iterator.hasNext()) {
                Council entry = iterator.next();
                writer.write(entry);
            }
//            System.out.println("Inserting " + string);
            writer.flush();
        }
    }
}
