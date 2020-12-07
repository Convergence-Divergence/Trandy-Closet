package com.example.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val dataList: MutableList<WeatherData>) : RecyclerView.Adapter<MyViewHolder>() {


    override fun getItemCount(): Int = dataList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.mission1_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val weatherData = dataList[position]

        holder?.let {
            it.dayView.text = weatherData.day
            it.maxView.text = weatherData.max
            it.minView.text = weatherData.min
            it.imgView.setImageBitmap(weatherData.img)
        }
    }
}