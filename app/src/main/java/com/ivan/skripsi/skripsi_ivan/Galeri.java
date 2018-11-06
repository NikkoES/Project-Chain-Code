package com.ivan.skripsi.skripsi_ivan;

public class Galeri {

    private String Judul;
    private String Kategori;
    private String Deskripsi;
    private int Gambar;

    public Galeri() {
    }



    public Galeri(String judul, String kategori, String deskripsi, int gambar) {
        Judul = judul;
        Kategori = kategori;
        Deskripsi = deskripsi;
        Gambar = gambar;
    }

    public String getJudul() {
        return Judul;
    }

    public String getKategori() {
        return Kategori;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public int getGambar() {
        return Gambar;
    }



    public void setJudul(String judul) {
        Judul = judul;
    }

    public void setKategori(String kategori) {
        Kategori = kategori;
    }

    public void setDeskripsi(String deskripsi) {
        Deskripsi = deskripsi;
    }

    public void setGambar(int gambar) {
        Gambar = gambar;
    }
}
