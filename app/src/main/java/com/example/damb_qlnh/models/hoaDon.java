package com.example.damb_qlnh.models;

import com.example.damb_qlnh.models.CTHD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class hoaDon {
    private String maHD;
    private String maKH;
    private String maNV;
    private String maBan;
    private String maVoucher;
    private String thoiGian;
    private String tongTien_T;
    private String tongTien_S;
    private ArrayList<CTHD> cthds;

    public hoaDon(String maHD, String thoiGian, String maKH, String maNV, String maBan, String maVoucher, String tongTien_T, String tongTien_S, ArrayList<CTHD> cthds) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.maNV = maNV;
        this.maBan = maBan;
        this.maVoucher = maVoucher;
        this.thoiGian = thoiGian;
        this.tongTien_T = tongTien_T;
        this.tongTien_S = tongTien_S;
        this.cthds = cthds;
    }
    public hoaDon(String maKH){
        tongTien_T = "0";
        tongTien_S = "0";
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
        Date date = new Date();
        thoiGian = format.format(date);
        maVoucher = "";
        maHD = "";
        this.maKH = maKH;
        maNV = "";
        maBan = "";
        cthds = new ArrayList<>();
    }
    public void addCTHD(CTHD cthd){
        cthds.add(cthd);
        int n = Integer.parseInt(cthd.getMonAn().getGiaTien()) * cthd.getSoLuong();
        tongTien_T = Integer.toString(Integer.parseInt(tongTien_T) + n);
        tongTien_S = tongTien_T;
    }
    public void useVoucher(int giaTri){
        int temp = Integer.parseInt(tongTien_S) - giaTri;
        tongTien_S = Integer.toString((temp < 0) ? 0 : temp);
    }
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getTongTien_T() {
        return tongTien_T;
    }

    public void setTongTien_T(String tongTien_T) {
        this.tongTien_T = tongTien_T;
    }

    public String getTongTien_S() {
        return tongTien_S;
    }

    public void setTongTien_S(String tongTien_S) {
        this.tongTien_S = tongTien_S;
    }
}
