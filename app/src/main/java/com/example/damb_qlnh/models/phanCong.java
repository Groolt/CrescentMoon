package com.example.damb_qlnh.models;

import java.util.ArrayList;

public class phanCong {
    private nhanVien nhanVien;
    private String ngayLam;
    private ArrayList<caLam> caLams;

    public phanCong(com.example.damb_qlnh.models.nhanVien nhanVien, String ngayLam, ArrayList<caLam> caLams) {
        this.nhanVien = nhanVien;
        this.ngayLam = ngayLam;
        this.caLams = caLams;
    }

    public com.example.damb_qlnh.models.nhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(com.example.damb_qlnh.models.nhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(String ngayLam) {
        this.ngayLam = ngayLam;
    }

    public ArrayList<caLam> getCaLams() {
        return caLams;
    }

    public void setCaLams(ArrayList<caLam> caLams) {
        this.caLams = caLams;
    }
}
