package com.example.test

// 날씨용 뷰 홀더

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val dayView = v.findViewById<TextView>(R.id.mission1_item_day)
    val minView = v.findViewById<TextView>(R.id.mission1_item_min)
    val maxView = v.findViewById<TextView>(R.id.mission1_item_max)
    val imgView = v.findViewById<ImageView>(R.id.mission1_item_image)
}

