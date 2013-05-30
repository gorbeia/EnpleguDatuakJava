package com.aiaraldea.gizartesegurantzawriter.councils;

/**
 *
 * @author inaki
 */
public class Council {

    private String provinceCode;
    private String councilCode;
    private String ineName;
    private String name;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCouncilCode() {
        return councilCode;
    }

    public void setCouncilCode(String councilCode) {
        this.councilCode = councilCode;
    }

    public String getIneCode() {
        return provinceCode + councilCode;
    }

    public String getIneName() {
        return ineName;
    }

    public void setIneName(String ineName) {
        if (this.name == null) {
            this.name = ineName.trim();
        }
        this.ineName = ineName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            this.name = this.ineName;
        } else {
            this.name = name.trim();
        }
    }
}
