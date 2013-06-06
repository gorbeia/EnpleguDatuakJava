package com.aiaraldea.gizartesegurantzawriter.ss;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author inaki
 */
public class SSLastDayOfMonthByCouncilParser {

    Set<SSEntry> parsed = new HashSet<>();

    public Set<SSEntry> parse(InputStream is) throws IOException, InvalidFormatException {
        Set<SSEntry> entries = new HashSet<>();
        //Get the workbook instance for XLS file 
        Workbook workbook = WorkbookFactory.create(is);

//Get first sheet from the workbook
        Sheet sheet = workbook.getSheetAt(0);
        String desc = sheet.getRow(0).getCell(0).getStringCellValue();
        desc = desc.substring(16);
        String[] split = desc.split(" ");
        String year = split[2];
        String month = split[0];
        ColumnDefinition cd = ColumnDefinition.parseTitles(sheet);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String stringCellValue = "";
            if (row.getCell(1) != null) {
                stringCellValue = row.getCell(1).getStringCellValue();
            }
            if (stringCellValue.length() > 4) {
                String[] splitted = stringCellValue.split(" ");
                String code = splitted[0];
                if (isNumeric(code)) {
                    String name = splitted[1];
                    SSEntry entry = new SSEntry();
                    entry.setYear(Integer.parseInt(year));
                    entry.setMonth(Months.months.get(month));
                    entry.setIneCode(code);
//                    entry.setCouncilName(name);
                    entry.setGeneral(parseCell(row, cd.generalColumn));
                    entry.setAgrario(parseCell(row, cd.farmingColumn));
                    entry.setMar(parseCell(row, cd.seaColumn));
                    entry.setHogar(parseCell(row, cd.houseColumn));
                    entry.setCarbon(parseCell(row, cd.coalColumn));
                    entry.setAutonomos(parseCell(row, cd.freelanceColumn));
                    entry.setTotal(parseCell(row, cd.totalColumn));
                    entries.add(entry);
                }
            }
        }
        parsed.addAll(entries);
        return entries;
    }

    public Set<SSEntry> getParsed() {
        return parsed;
    }

    private static int parseCell(Row row, int i) {
        Cell cell = row.getCell(i);
        if (cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
            return 1;
        }
        return (int) cell.getNumericCellValue();
    }

    private static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private static class ColumnDefinition {

        int generalColumn = 0;
        int farmingColumn = 0;
        int seaColumn = 0;
        int houseColumn = 0;
        int freelanceColumn = 0;
        int coalColumn = 0;
        int totalColumn = 0;

        static ColumnDefinition parseTitles(Sheet sheet) {
            ColumnDefinition cd = new ColumnDefinition();
            Row titles = sheet.getRow(1);
            Iterator<Cell> titleIterator = titles.cellIterator();
            while (titleIterator.hasNext()) {
                Cell cell = titleIterator.next();
                String stringCellValue = cell.getStringCellValue();
                String normalized = Normalizer.normalize(stringCellValue, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
                if (normalized.contains("general")) {
                    cd.generalColumn = cell.getColumnIndex();
                }
                if (normalized.contains("agrario")) {
                    cd.farmingColumn = cell.getColumnIndex();
                }
                if (normalized.contains("mar")) {
                    cd.seaColumn = cell.getColumnIndex();
                }
                if (normalized.contains("hogar")) {
                    cd.houseColumn = cell.getColumnIndex();
                }
                if (normalized.contains("autonomos")) {
                    cd.freelanceColumn = cell.getColumnIndex();
                }
                if (normalized.contains("carbon")) {
                    cd.coalColumn = cell.getColumnIndex();
                }
                if (normalized.contains("total")) {
                    cd.totalColumn = cell.getColumnIndex();
                }
            }
            return cd;
        }
    }
}
