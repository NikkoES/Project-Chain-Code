package com.ivan.skripsi.skripsi_ivan;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    //list of images
    public int[] list_images = {
            R.drawable.slide1,
            R.drawable.slide2,
            R.drawable.slide3,
            R.drawable.slide4
    };
    //list of titles
    public String[] list_title = {
            "IVAN S P",
            "TEKNIK INFORMATIKA",
            "SAINS DAN TEKNOLOGI",
            "UIN SGD BANDUNG"
    };
    //list of description
    public String[] list_description ={
            "adalah Seorang Mahasiswa yang sedang menyelesaikan tugas akhir yakni pembuatan aplikasi Android tentang deteksi citra 2 dimensi",
            "adalah jurusan perkuliahan yang ditempuh untuk menguasai berbagai ilmu mengenai dunia komputer dan teknologi informasi",
            "adalah sebuah fakultas Perguruan Tinggi yang didalamnya banyak mempelajari ilmu tentang Sains",
            "adalah sebuah Universitas Islam Negeri di Bandung yang sangat populer dan memiliki nilai plus yang tinggi dimata masyarakat karena memiliki bobot agama"
    };
    //list of background colors
    public int[] list_backgroundcolor ={
            Color.rgb(55,55,55),
            Color.rgb(239,85, 85),
            Color.rgb(110,49,89),
            Color.rgb(1,188,212)
    };

    public SlideAdapter(Context context){
        this.context = context;
    }


    @Override
    public int getCount() {
        return list_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide,container,false);
        LinearLayout linearLayout = view.findViewById(R.id.slidelinearlayout);
        ImageView slideimg =(ImageView)view.findViewById(R.id.slideimg);
        TextView txttitle = (TextView)view.findViewById(R.id.txttitle);
        TextView txtdescription = (TextView)view.findViewById(R.id.txtdescription);
        linearLayout.setBackgroundColor(list_backgroundcolor[position]);
        slideimg.setImageResource(list_images[position]);
        txttitle.setText(list_title[position]);
        txtdescription.setText(list_description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
