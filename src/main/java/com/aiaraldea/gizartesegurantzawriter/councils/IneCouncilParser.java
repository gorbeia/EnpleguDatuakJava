package com.aiaraldea.gizartesegurantzawriter.councils;

import com.aiaraldea.gizartesegurantzawriter.councils.Council;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author inaki
 */
public class IneCouncilParser {

    public static Set<Council> parse(InputStream is) throws IOException, InvalidFormatException {

        Set<Council> entries = new HashSet<Council>();
        //Get the workbook instance for XLS file 
        Workbook workbook = WorkbookFactory.create(is);
//Get first sheet from the workbook
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Council council = new Council();
            Row row = rowIterator.next();
            council.setProvinceCode(row.getCell(0).getStringCellValue());
            council.setCouncilCode(row.getCell(1).getStringCellValue());
            council.setCouncilCode(row.getCell(1).getStringCellValue());
            council.setIneName(row.getCell(3).getStringCellValue());
            if (row.getCell(4) != null) {
                council.setName(row.getCell(4).getStringCellValue());
            }
            entries.add(council);
        }
        return entries;
    }
}
