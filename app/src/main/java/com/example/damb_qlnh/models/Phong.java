package com.example.damb_qlnh.models;

public class Phong {
    private String tenPhong;
    private int tinhTrang;

    public Phong(String tenPhong, int tinhTrang) {
        this.tenPhong = tenPhong;
        this.tinhTrang = tinhTrang;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
