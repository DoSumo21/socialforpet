package com.example.socialforpet;

import java.util.Date;

public class Pet {
    private String Ten;
    private String Loai;
    private String Giong;
    private float TrongLuong;
    private boolean GioiTinh;
    private String NgaySinh;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;


    public Pet() {

    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getLoai() {
        return Loai;
    }

    public void setLoai(String loai) {
        Loai = loai;
    }

    public String getGiong() {
        return Giong;
    }

    public void setGiong(String giong) {
        Giong = giong;
    }

    public float getTrongLuong() {
        return TrongLuong;
    }

    public void setTrongLuong(float trongLuong) {
        TrongLuong = trongLuong;
    }

    public boolean isGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public void print(){
        System.out.print(Ten + Giong + Loai + GioiTinh + TrongLuong + NgaySinh);
    }
}
