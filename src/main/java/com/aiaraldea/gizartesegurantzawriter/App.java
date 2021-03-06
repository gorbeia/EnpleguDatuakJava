package com.aiaraldea.gizartesegurantzawriter;

import com.aiaraldea.gizartesegurantzawriter.writer.OutputWriter;
import com.aiaraldea.gizartesegurantzawriter.writer.OutputWriterFactory;
import com.aiaraldea.gizartesegurantzawriter.ss.SSEntry;
import com.aiaraldea.gizartesegurantzawriter.ss.SSLastDayOfMonthByCouncilParser;
import com.aiaraldea.gizartesegurantzawriter.councils.IneCouncilParser;
import com.aiaraldea.gizartesegurantzawriter.councils.Council;
import com.aiaraldea.gizartesegurantzawriter.sepe.SepeLoader;
import com.aiaraldea.gizartesegurantzawriter.ss.web.ListLinks;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidFormatException, ParseException {
        // create Options object
        Options options = new Options();
        // add t option

        options.addOption(
                OptionBuilder.withLongOpt("type").
                withArgName("type").
                withDescription("Operation type. It can be 'ss' or 'sepe'").
                hasArg().
                isRequired().
                create("t"));
        options.addOption(
                OptionBuilder.withLongOpt("files").
                withArgName("files").
                withDescription("Input files").
                hasArgs().
                create("f"));
        options.addOption(
                OptionBuilder.withLongOpt("database").
                withDescription("Insert in the database specified in the configuration file").
                create("b"));
        options.addOption(
                OptionBuilder.withLongOpt("configuration").
                withArgName("configuration").
                withDescription("Configuration file").
                hasArg().
                isRequired().
                create("c"));
        options.addOption(
                OptionBuilder.withLongOpt("help").
                withDescription("Help").
                create("h"));
        CommandLine cmd;
        try {
            CommandLineParser parser = new PosixParser();
            cmd = parser.parse(options, args);
        } catch (MissingOptionException ex) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("EnpleguDatuak -t <action> -c <configuration file>", ex.getLocalizedMessage(), options, null);
            System.exit(0);
            return;
        }
        String configFile = cmd.getOptionValue("c");
        AppConfig.init(configFile);

        if (cmd.hasOption("f")) {
            AppConfig.setFilePaths(Arrays.asList(cmd.getOptionValues("f")));
        }
        if (cmd.hasOption("b")) {
            AppConfig.setOutputWriter("mongo");
        } else {
            AppConfig.setOutputWriter(OutputWriterFactory.PROPERTY_VALUE_SIMPLE);
        }

        switch (cmd.getOptionValue("t")) {
            case "councils":
                loadCouncils();
                break;
            case "councilsMap":
                loadCouncilsMap();
                break;
            case "ss":
                if (cmd.hasOption("f")) {
                    loadSSDAta();
                } else {
                    loadSSDataInet();
                }
                break;
            case "sepe":
                if (cmd.hasOption("f")) {
                    SepeLoader.loadSepeData();
                } else {
                    SepeLoader.loadSepeDataInet();
                }
                break;
            default:
                loadSSDAta();
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
//            System.err.println("Inserting " + string);
        writer.flush();
    }

    public static void loadSSDAta() throws FileNotFoundException, IOException, InvalidFormatException {
        OutputWriter writer = OutputWriterFactory.getOutputWriter("SSLastDayOfMonthByCouncil");
        for (String string : AppConfig.getFilePaths()) {
            FileInputStream file = new FileInputStream(new File(string));
            Set<SSEntry> parsed = new SSLastDayOfMonthByCouncilParser().parse(file);
            Iterator<SSEntry> iterator = parsed.iterator();
            while (iterator.hasNext()) {
                SSEntry sSEntry = iterator.next();
                writer.write(sSEntry);
            }
//            System.err.println("Inserting " + string);
            writer.flush();
        }
    }

    private static void loadCouncils() throws FileNotFoundException, IOException, InvalidFormatException {
        System.err.println("loadCouncils");
        if (AppConfig.getFilePaths() == null) {
            throw new IllegalArgumentException(
                    "The file is mandatory to load the councils. "
                    + "Load form the web page is not implemented");
        }
        OutputWriter writer = OutputWriterFactory.getOutputWriter("IneCouncils");
        for (String string : AppConfig.getFilePaths()) {
            FileInputStream file = new FileInputStream(new File(string));
            Set<Council> parsed = IneCouncilParser.parse(file);
            Iterator<Council> iterator = parsed.iterator();
            while (iterator.hasNext()) {
                Council entry = iterator.next();
                writer.write(entry);
            }
//            System.err.println("Inserting " + string);
            writer.flush();
        }
    }

    private static void loadCouncilsMap() throws FileNotFoundException, IOException, InvalidFormatException {
        System.err.println("loadCouncilsMap");
        if (AppConfig.getFilePaths() == null) {
            throw new IllegalArgumentException(
                    "The file is mandatory to load the councils. "
                    + "Load form the web page is not implemented");
        }
        OutputWriter writer = OutputWriterFactory.getOutputWriter("IneCouncils");
        Map<String, String> councilMap = new HashMap<>();
        for (String string : AppConfig.getFilePaths()) {
            FileInputStream file = new FileInputStream(new File(string));
            Set<Council> parsed = IneCouncilParser.parse(file);
            Iterator<Council> iterator = parsed.iterator();
            while (iterator.hasNext()) {
                Council entry = iterator.next();

                String normalized = Normalizer.normalize(entry.getIneName(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
                councilMap.put(
                        normalized,
                        entry.getIneCode());

                if (normalized.contains("/")) {
                    String[] split = normalized.split("/");
                    for (String string1 : split) {
                        councilMap.put(
                                string1,
                                entry.getIneCode());
                    }
                }
                normalized = Normalizer.normalize(entry.getName(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
                councilMap.put(
                        normalized,
                        entry.getIneCode());
            }
//            System.err.println("Inserting " + string);
            writer.write(councilMap);
            writer.flush();
            councilMap.clear();
        }
    }
}
