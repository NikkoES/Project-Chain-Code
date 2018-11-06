package com.ivan.skripsi.skripsi_ivan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView KameraCard, GaleriCard, MetodeCard, TentangCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //perizinan untuk akses kamera dan storage
        int Permissiondulu = 1;
        String[]Permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL};
        if (!hasPermission(this, Permissions)){
            ActivityCompat.requestPermissions(this,Permissions,Permissiondulu);
        }

        //definisi ID dari CardView yang telah dibuat
        KameraCard = (CardView) findViewById(R.id.kamera);
        GaleriCard = (CardView) findViewById(R.id.galeri);
        MetodeCard = (CardView) findViewById(R.id.metode);
        TentangCard = (CardView) findViewById(R.id.tentang);

        //menambahkan ClickListener ke CardView menu yang 4
        KameraCard.setOnClickListener(this);
        GaleriCard.setOnClickListener(this);
        MetodeCard.setOnClickListener(this);
        TentangCard.setOnClickListener(this);
    }

    //perizinan untuk SDK yang lebih atau diatas API 15
    public static boolean hasPermission(Context context, String... permissions){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && context!=null && permissions!=null){
            for (String permission: permissions){
                if (ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    //switch case untuk berpindah ke menu yang 4 yang sudah dibuat
    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.kamera : i = new Intent(this,Activity_Kamera.class);startActivity(i);break;
            case R.id.galeri : i = new Intent(this,Activity_Galeri.class);startActivity(i);break;
            case R.id.metode : i = new Intent(this,Activity_Metode.class);startActivity(i);break;
            case R.id.tentang : i = new Intent(this,Activity_Tentang.class);startActivity(i);break;
            default:break;
        }
    }
}
