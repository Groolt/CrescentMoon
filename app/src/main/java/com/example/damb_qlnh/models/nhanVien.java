package com.example.damb_qlnh.models;

import java.util.ArrayList;

public class nhanVien {
    private String viTri;
    private String tenNV;
    private String maNV;
    private String gioiTinh;
    private String sdt;
    private String cccd;
    private int loaiNV;

    public nhanVien(String viTri,  int loaiNV, String tenNV, String maNV, String gioiTinh, String sdt, String cccd) {
        this.viTri = viTri;
        this.tenNV = tenNV;
        this.maNV = maNV;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.cccd = cccd;
        this.loaiNV = loaiNV;
    }

    public int getLoaiNV() {
        return loaiNV;
    }

    public void setLoaiNV(int loaiNV) {
        this.loaiNV = loaiNV;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

}
