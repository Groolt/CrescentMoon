package com.example.damb_qlnh.models;

public class caLam {
    private int maCa;
    private String thoiGian;

    public caLam(int maCa, String thoiGian) {
        this.maCa = maCa;
        this.thoiGian = thoiGian;
    }

    public int getMaCa() {
        return maCa;
    }

    public void setMaCa(int maCa) {
        this.maCa = maCa;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }
    public String getCalam(){
        return "Ca " + maCa + ": " + thoiGian;
    }
}
