package com.aiaraldea.gizartesegurantzawriter.ss;

import java.util.Formatter;

/**
 *
 * @author inaki
 */
public class SSEntry {

    private int year;
    private int month;
//    private String provinceCode;
//    private String provinceName;
    private String councilCode;
//    private String councilName;
    private int general;
    private int agrario;
    private int mar;
    private int hogar;
    private int autonomos;
    private int carbon;
    private int total;

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

    public String getCouncilCode() {
        return councilCode;
    }

    public void setCouncilCode(String councilCode) {
        this.councilCode = councilCode;
    }

//    public String getCouncilName() {
//        return councilName;
//    }
//
//    public void setCouncilName(String councilName) {
//        this.councilName = councilName;
//    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public int getAgrario() {
        return agrario;
    }

    public void setAgrario(int agrario) {
        this.agrario = agrario;
    }

    public int getMar() {
        return mar;
    }

    public void setMar(int mar) {
        this.mar = mar;
    }

    public int getHogar() {
        return hogar;
    }

    public void setHogar(int hogar) {
        this.hogar = hogar;
    }

    public int getAutonomos() {
        return autonomos;
    }

    public void setAutonomos(int autonomos) {
        this.autonomos = autonomos;
    }

    public int getCarbon() {
        return carbon;
    }

    public void setCarbon(int carbon) {
        this.carbon = carbon;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
