package com.example.damb_qlnh.models;

import java.io.Serializable;

public class CTHD implements Serializable {
    private String maHD;
    private com.example.damb_qlnh.models.monAn monAn;
    private int soLuong;
    private int tinhTrang; //0:chua goi 1:da goi

    public CTHD(com.example.damb_qlnh.models.monAn monAn, int soLuong, String maHD) {
        this.monAn = monAn;
        this.soLuong = soLuong;
        tinhTrang = 0;
        this.maHD = maHD;
    }
    public CTHD(){

    }

    public com.example.damb_qlnh.models.monAn getMonAn() {
        return monAn;
    }

    public void setMonAn(com.example.damb_qlnh.models.monAn monAn) {
        this.monAn = monAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }
    @Override
    public boolean equals(Object obj) {
        CTHD other = (CTHD) obj;
        return monAn.getMaMA().equals(other.getMonAn().getMaMA());
    }
}
