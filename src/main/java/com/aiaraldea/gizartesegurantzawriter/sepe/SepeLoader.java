package com.aiaraldea.gizartesegurantzawriter.sepe;

import com.aiaraldea.gizartesegurantzawriter.AppConfig;
import com.aiaraldea.gizartesegurantzawriter.councils.CouncilCodeHelper;
import com.aiaraldea.gizartesegurantzawriter.ss.Months;
import com.aiaraldea.gizartesegurantzawriter.writer.OutputWriter;
import com.aiaraldea.gizartesegurantzawriter.writer.OutputWriterFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 *
 * @author inaki
 */
public class SepeLoader {

    private static final Set<String> provinces = new HashSet<>();

    static {
        provinces.add("ALAVA");
        provinces.add("ARABA");
        provinces.add("VIZCAYA");
        provinces.add("BIZKAIA");
        provinces.add("NAVARRA");
        provinces.add("GUIPUZCOA");
        provinces.add("GIPUZKOA");
    }

    public static void loadSepeDataInet() throws FileNotFoundException, IOException, InvalidFormatException {
        OutputWriter w = OutputWriterFactory.getOutputWriter("SepeLastDayOfMonthByCouncil");
        Collection<String> fetchLinks = SepeLinks.fetchLinks("http://www.sepe.es/contenido/estadisticas/datos_estadisticos/municipios/");
        SepeLinks.fetchUnfetchedFiles(fetchLinks, new SepeLoader(), w);
    }

    public static void loadSepeData() throws FileNotFoundException, IOException, InvalidFormatException {
        Set<MunicipalityData> data = new HashSet<>();
        for (String string : AppConfig.getFilePaths()) {
            FileInputStream file = new FileInputStream(new File(string));
            readFile(file, data);
        }

        ObjectMapper mapper = new ObjectMapper();

        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String jsonOutput = writer.writeValueAsString(data);
        System.out.println(jsonOutput);
    }

    private static void readFile(InputStream file, Set<MunicipalityData> data) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(file);

//Get first sheet from the workbook
        Sheet sheet = getParoSheet(workbook);
        if (sheet == null) {
            return;
        }

        String province;
        int dateYear = 0;
        int dateMonth = 0;
        String date;
        Iterator<Row> rowIterator = sheet.iterator();

        outerloop:
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.iterator();
            boolean isDateRow = false;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (isDateRow && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    date = cell.getStringCellValue();
                    String[] split = date.split(" ");
                    if (split.length > 1) {
                        dateYear = Integer.parseInt(split[1]);
                        dateMonth = Months.months.get(split[0]);
                    }

                    break outerloop;
                }
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    String stringCellValue = cell.getStringCellValue();
                    String[] splitted = stringCellValue.split("/");
                    if (splitted.length > 0 && provinces.contains(splitted[0])) {
                        isDateRow = true;
                        province = splitted[0];
                    }
                }
            }
            row.toString();
        }

//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
//            Iterator<Cell> cellIterator = row.iterator();
//            while (cellIterator.hasNext()) {
//                continue;
//            }
//        }
        CouncilCodeHelper councilCodeHelper = new CouncilCodeHelper();
        while (rowIterator.hasNext()) {
            MunicipalityData unenploymentData = new MunicipalityData();
            Row row = rowIterator.next();

            String stringCellValue;
            if (row.getCell(1) != null && row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
                stringCellValue = row.getCell(1).getStringCellValue();
                if (row.getCell(2) != null && row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    unenploymentData.setIneCode(councilCodeHelper.getCode(stringCellValue));
                    unenploymentData.setMunicipality(stringCellValue);
                    double total = row.getCell(2).getNumericCellValue();
                    unenploymentData.setTotal(total);
                    unenploymentData.setYear(dateYear);
                    unenploymentData.setMonth(dateMonth);
                    data.add(unenploymentData);
                }
            }
        }
    }

    private static Sheet getParoSheet(Workbook workbook) {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            if (sheetName.equals("PARO")) {
                return sheet;
            }
        }
        return null;
    }

    public Set<MunicipalityData> parse(InputStream is) {
        Set<MunicipalityData> data = new HashSet<>();
        try {
            readFile(is, data);
            return data;
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(SepeLoader.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Problem reading.", ex);
        }
    }
}
