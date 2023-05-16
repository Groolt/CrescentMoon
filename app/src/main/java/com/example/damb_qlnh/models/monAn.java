package com.example.damb_qlnh.models;

import com.example.damb_qlnh.R;

public class monAn {
    private String maMA;
    private String tenMA;
    private String loaiMA;
    private String giaTien;
    private int anhMA;

    public monAn(String maMA, String tenMA, String loaiMA, String giaTien, int anhMA) {
        this.maMA = maMA;
        this.tenMA = tenMA;
        this.loaiMA = loaiMA;
        this.giaTien = giaTien;
        this.anhMA = anhMA;
    }

    public int getAnhMA() {
        return anhMA;
    }

    public void setAnhMA(int anhMA) {
        this.anhMA = anhMA;
    }

    public String getMaMA() {
        return maMA;
    }

    public void setMaMA(String maMA) {
        this.maMA = maMA;
    }

    public String getTenMA() {
        return tenMA;
    }

    public void setTenMA(String tenMA) {
        this.tenMA = tenMA;
    }

    public String getLoaiMA() {
        return loaiMA;
    }

    public void setLoaiMA(String loaiMA) {
        this.loaiMA = loaiMA;
    }

    public String getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(String giaTien) {
        this.giaTien = giaTien;
    }
}
