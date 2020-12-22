package com.example.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecomAdapter extends RecyclerView.Adapter<RecomAdapter.RecomViewHolder> {

    private String [][] dataList = new String[3][99];
    private Context context;

    public RecomAdapter(Context context,String [][] dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class RecomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;


        private ImageView iv_recom_top;
        private ImageView iv_recom_bottom;
        private ImageView iv_recom_shoes;

        RecomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            iv_recom_top = mView.findViewById(R.id.iv_recom_top);
            iv_recom_bottom = mView.findViewById(R.id.iv_recom_bottom);
            iv_recom_shoes = mView.findViewById(R.id.iv_recom_shoes);


        }
    }

    @Override
    public RecomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_recom_inrv, parent, false);
        return new RecomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecomViewHolder holder, int position) {
        Log.d("아이고", String.valueOf(dataList[0][0]));
        Log.d("아이고", String.valueOf(dataList[1][0]));
        Log.d("아이고", String.valueOf(dataList[2][0]));
        Glide.with(context).load(dataList[0][position]).into(holder.iv_recom_top);
        Glide.with(context).load(dataList[1][position]).into(holder.iv_recom_bottom);
        Glide.with(context).load(dataList[2][position]).into(holder.iv_recom_shoes);

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}