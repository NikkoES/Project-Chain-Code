package com.ivan.skripsi.skripsi_ivan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Galeri_Activity2 extends AppCompatActivity {

    private TextView tvjudul, tvdeskripsi, tvkategori;
    private ImageView g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri_2);

        tvjudul = (TextView) findViewById(R.id.txt_judul);
        tvdeskripsi = (TextView) findViewById(R.id.txt_deskripsi);
        tvkategori = (TextView) findViewById(R.id.txt_kategori);
        g = (ImageView)findViewById(R.id.galerigambar);


        //Receive data
        Intent intent = getIntent();
        String Judul = intent.getExtras().getString("Judul");
        String Deskripsi = intent.getExtras().getString("Deskripsi");
        int Gambar = intent.getExtras().getInt("Gambar");

        //Setting values
        tvjudul.setText(Judul);
        tvdeskripsi.setText(Deskripsi);
        g.setImageResource(Gambar);
    }
}
