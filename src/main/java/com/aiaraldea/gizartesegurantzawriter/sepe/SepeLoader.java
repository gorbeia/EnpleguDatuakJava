package com.aiaraldea.gizartesegurantzawriter.sepe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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

    private static final Set<String> provinces = new HashSet<String>();

    static {
        provinces.add("ALAVA");
        provinces.add("ARABA");
        provinces.add("VIZCAYA");
        provinces.add("BIZKAIA");
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidFormatException {
        UnemploymentData data = new UnemploymentData();
        for (String string : args) {
            FileInputStream file = new FileInputStream(new File(string));
            readFile(file, data);
        }

        ObjectMapper mapper = new ObjectMapper();
        
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String jsonOutput = writer.writeValueAsString(data);
        System.out.println(jsonOutput);
    }

    private static void readFile(FileInputStream file, UnemploymentData data) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(file);

//Get first sheet from the workbook
        Sheet sheet = getParoSheet(workbook);

        String province;
        String date = "";
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
        while (rowIterator.hasNext()) {
            MunicipalityData unenploymentData = new MunicipalityData();
            Row row = rowIterator.next();

            String stringCellValue;
            if (row.getCell(1) != null && row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
                stringCellValue = row.getCell(1).getStringCellValue();
                unenploymentData.setMunicipality(stringCellValue);
                if (row.getCell(2) != null && row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    double total = row.getCell(2).getNumericCellValue();
                    unenploymentData.setTotal(total);
                    data.addData(date, unenploymentData);
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
}
