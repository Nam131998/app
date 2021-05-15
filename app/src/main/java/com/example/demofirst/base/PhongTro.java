package com.example.demofirst.base;

import java.io.Serializable;

public class PhongTro implements Serializable {
    private String ID;
    private String TEN_PHONG;
    private String SUC_CHUA;
    private String DIEN_TICH;
    private String GIA_THUE;
    private String THONG_TIN_KHAC;
    private String SO_DIEN;
    private String SO_NUOC;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTEN_PHONG() {
        return TEN_PHONG;
    }

    public void setTEN_PHONG(String TEN_PHONG) {
        this.TEN_PHONG = TEN_PHONG;
    }

    public String getSUC_CHUA() {
        return SUC_CHUA;
    }

    public void setSUC_CHUA(String SUC_CHUA) {
        this.SUC_CHUA = SUC_CHUA;
    }

    public String getDIEN_TICH() {
        return DIEN_TICH;
    }

    public void setDIEN_TICH(String DIEN_TICH) {
        this.DIEN_TICH = DIEN_TICH;
    }

    public String getGIA_THUE() {
        return GIA_THUE;
    }

    public void setGIA_THUE(String GIA_THUE) {
        this.GIA_THUE = GIA_THUE;
    }

    public String getTHONG_TIN_KHAC() {
        return THONG_TIN_KHAC;
    }

    public void setTHONG_TIN_KHAC(String THONG_TIN_KHAC) {
        this.THONG_TIN_KHAC = THONG_TIN_KHAC;
    }

    public String getSO_DIEN() {
        return SO_DIEN;
    }

    public void setSO_DIEN(String SO_DIEN) {
        this.SO_DIEN = SO_DIEN;
    }

    public String getSO_NUOC() {
        return SO_NUOC;
    }

    public void setSO_NUOC(String SO_NUOC) {
        this.SO_NUOC = SO_NUOC;
    }
}
