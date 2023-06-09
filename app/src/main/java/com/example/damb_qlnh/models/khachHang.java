package com.example.damb_qlnh.models;

import java.io.Serializable;

public class khachHang implements Serializable {
    private String id;
    private String maKH;
    private String tenKH;
    private String gioiTinh;
    private String dob;
    private String SDT;
    private String xepHang;
    private String img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public khachHang(String maKH, String tenKH, String gioiTinh, String SDT, String xepHang, String dob, String img) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.gioiTinh = gioiTinh;
        this.SDT = SDT;
        this.xepHang = xepHang;
        this.dob = dob;
        this.img = img;
    }

    public khachHang() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getXepHang() {
        return xepHang;
    }

    public void setXepHang(String xepHang) {
        this.xepHang = xepHang;
    }
}