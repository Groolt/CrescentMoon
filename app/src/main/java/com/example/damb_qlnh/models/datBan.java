package com.example.damb_qlnh.models;

public class datBan {
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private String id;
    private String avatar;
    private String maKH;
    private String maBan;
    private String name;
    private String phone;
    private String date;
    private String num;
    private String time;
    private String note;

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getMaKH() {
        return maKH;
    }

    public datBan(String avatar, String maKH, String maBan, String name, String phone, String date, String num, String time, String note) {
        this.avatar = avatar;
        this.maKH = maKH;
        this.maBan = maBan;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.num = num;
        this.time = time;
        this.note = note;
    }
    public datBan(){}

    public datBan(String name, String phone, String date, String num, String time, String note, String maBan) {
        this.maBan = maBan;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.num = num;
        this.time = time;
        this.note = note;
    }
    public datBan(String time, String maBan, String name){
        this.time = time;
        this.maBan = maBan;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

