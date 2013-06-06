package com.aiaraldea.gizartesegurantzawriter.sepe;

import java.util.Formatter;

/**
 *
 * @author inaki
 */
public abstract class MonthlyData {

    private int year;
    private int month;
    private String ineCode;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year < 1900 || year > 2100) {
            throw new RuntimeException("Wrong yeaar");
        }
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month > 12 || month == 0) {
            throw new RuntimeException("Wrong month");
        }
        this.month = month;
    }

    public String getYearMonth() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        formatter.format("%02d", month);
        return year + "/" + sb.toString();
    }

    public String getIneCode() {
        return ineCode;
    }

    public void setIneCode(String ineCode) {
        this.ineCode = ineCode;
    }
}
