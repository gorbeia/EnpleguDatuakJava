package com.aiaraldea.gizartesegurantzawriter.sepe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author inaki
 */
public class UnemploymentData {

    private Map<String, List<MunicipalityData>> data =
            new HashMap<String, List<MunicipalityData>>();

    public Map<String, List<MunicipalityData>> getData() {
        return data;
    }

    public List<MunicipalityData> getData(String month) {
        return data.get(month);
    }

    public void addData(String month, List<MunicipalityData> data) {
        if (!this.data.containsKey(month)) {
            this.data.put(month, new ArrayList<MunicipalityData>());
        }
        this.data.get(month).addAll(data);
    }

    public void addData(String month, MunicipalityData data) {
        if (!this.data.containsKey(month)) {
            this.data.put(month, new ArrayList<MunicipalityData>());
        }
        this.data.get(month).add(data);
    }
}
