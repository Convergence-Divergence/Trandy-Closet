package com.example.test;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<RetroPhoto> dataList = new ArrayList();
    private Context context;


    int mPreviousIndex = -1;

    public CustomAdapter(Context context,List<RetroPhoto> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtTitle;
        TextView txtCategory;
        TextView txtDetail;
        TextView txtColor;
        TextView txtSeason;

        private ImageView coverImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.title);
            coverImage = mView.findViewById(R.id.coverImage);

            txtCategory = mView.findViewById(R.id.tv_category);
            txtDetail = mView.findViewById(R.id.tv_detail);
            txtColor = mView.findViewById(R.id.tv_color);
            txtSeason = mView.findViewById(R.id.tv_season);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(Color.GRAY);
                }
            });

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.txtTitle.setText(dataList.get(position).getname());
        holder.txtCategory.setText(dataList.get(position).getcategory());
        holder.txtDetail.setText(dataList.get(position).getdetail());
        holder.txtColor.setText(dataList.get(position).getcolor());
        holder.txtSeason.setText(dataList.get(position).getseason());

        Glide.with(context).load(dataList.get(position).getUrl()).into(holder.coverImage);
//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.downloader(new OkHttp3Downloader(context));
//        builder.build().load(dataList.get(position).getUrl())
//                .placeholder((R.drawable.ic_launcher_background))
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.coverImage);



    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
}