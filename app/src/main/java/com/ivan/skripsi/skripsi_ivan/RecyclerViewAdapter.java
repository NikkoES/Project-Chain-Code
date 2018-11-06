package com.ivan.skripsi.skripsi_ivan;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Galeri> mData;

    public RecyclerViewAdapter(Context mContext, List<Galeri> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_pict,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tv_galeri_judul.setText(mData.get(position).getJudul());
        holder.img_galeri_gambar.setImageResource(mData.get(position).getGambar());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,Galeri_Activity2.class);

                //passing data to the galeri_activity2
                intent.putExtra("Judul",mData.get(position).getJudul());
                intent.putExtra("Deskripsi",mData.get(position).getDeskripsi());
                intent.putExtra("Gambar",mData.get(position).getGambar());
                //memulai activity
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_galeri_judul;
        ImageView img_galeri_gambar;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_galeri_judul = (TextView) itemView.findViewById(R.id.judul_gambar_id);
            img_galeri_gambar = (ImageView) itemView.findViewById(R.id.galeri_gambar_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_galeri_id);
        }
    }
}
