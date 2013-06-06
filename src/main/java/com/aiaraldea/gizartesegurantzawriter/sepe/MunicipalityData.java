package com.aiaraldea.gizartesegurantzawriter.sepe;

/**
 *
 * @author inaki
 */
public class MunicipalityData extends MonthlyData {

    private String municipality;
    private double total;

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
