package com.example.damb_qlnh.models;

import java.util.Date;

public class vouCher {
    private String maVoucher;
    private String ngayBD;
    private String ngayKT;
    private int soLuong;
    private int giaTri;

    public vouCher(String maVoucher, String ngayBD, String ngayKT, int soLuong, int giaTri) {
        this.maVoucher = maVoucher;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.soLuong = soLuong;
        this.giaTri = giaTri;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getNgayBD() {
        return ngayBD;
    }

    public void setNgayBD(String ngayBD) {
        this.ngayBD = ngayBD;
    }

    public String getNgayKT() {
        return ngayKT;
    }

    public void setNgayKT(String ngayKT) {
        this.ngayKT = ngayKT;
    }


    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(int giaTri) {
        this.giaTri = giaTri;
    }
}

