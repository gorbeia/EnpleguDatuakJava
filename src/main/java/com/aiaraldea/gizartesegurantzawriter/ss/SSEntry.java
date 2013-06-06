package com.aiaraldea.gizartesegurantzawriter.ss;

import com.aiaraldea.gizartesegurantzawriter.sepe.MonthlyData;

/**
 *
 * @author inaki
 */
public class SSEntry extends MonthlyData {

    private int general;
    private int agrario;
    private int mar;
    private int hogar;
    private int autonomos;
    private int carbon;
    private int total;

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
