package com.ivan.skripsi.skripsi_ivan;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Activity_Tentang extends AppCompatActivity {

    private ViewPager viewPager;
    private SlideAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__tentang);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        myAdapter = new SlideAdapter(this);
        viewPager.setAdapter(myAdapter);
    }
}
