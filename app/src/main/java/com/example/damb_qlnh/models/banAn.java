package com.example.damb_qlnh.models;

public class banAn {
    private String maBan;
    private int tinhTrang;
    private int loaiBan;

    public banAn(String maBan, int tinhTrang, int loaiBan) {
        this.maBan = maBan;
        this.tinhTrang = tinhTrang;
        this.loaiBan = loaiBan;
    }

    public String getTenBan() {
        return maBan;
    }

    public void setTenBan(String tenBan) {
        this.maBan = tenBan;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public int getLoaiBan() {
        return loaiBan;
    }

    public void setLoaiBan(int loaiBan) {
        this.loaiBan = loaiBan;
    }
}
