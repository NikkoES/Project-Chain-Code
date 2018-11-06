package com.ivan.skripsi.skripsi_ivan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Activity_Galeri extends AppCompatActivity {

    List<Galeri> listGaleri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__galeri);

        listGaleri = new ArrayList<>();
        listGaleri.add(new Galeri("Roger","Hero Fighter","Deskripsi Hero",R.drawable.roger));
        listGaleri.add(new Galeri("Layla","Hero Marksman","Deskripsi Hero",R.drawable.layla));
        listGaleri.add(new Galeri("Gatotkaca","Hero Tanker","Deskripsi Hero",R.drawable.gatotkaca));
        listGaleri.add(new Galeri("Irithel","Hero Marksman","Deskripsi Hero",R.drawable.irithel));
        listGaleri.add(new Galeri("Zilong","Hero Fighter/Assasin","Deskripsi Hero",R.drawable.zilong));

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_galeri);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,listGaleri);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);
    }
}
