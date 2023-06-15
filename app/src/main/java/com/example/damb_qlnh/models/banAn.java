package com.example.damb_qlnh.models;

public class banAn {
    private String maBan;
    private int tinhTrang;
    private int loaiBan;
    private String phong;
    private int tang;
    private String id;

    public banAn(String maBan, int tinhTrang, int loaiBan, String phong, int tang, String id) {
        this.maBan = maBan;
        this.tinhTrang = tinhTrang;
        this.loaiBan = loaiBan;
        this.phong = phong;
        this.tang = tang;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public banAn() {
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getPhong() {
        return phong;
    }

    public void setPhong(String phong) {
        this.phong = phong;
    }

    public int getTang() {
        return tang;
    }

    public void setTang(int tang) {
        this.tang = tang;
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