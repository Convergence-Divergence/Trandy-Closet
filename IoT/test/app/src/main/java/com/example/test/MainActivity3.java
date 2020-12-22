package com.example.test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity3 extends AppCompatActivity {
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        progressDoalog = new ProgressDialog(MainActivity3.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RetroPhoto>> call = service.getAllCloths();
        call.enqueue(new Callback<List<RetroPhoto>>() {
            @Override
            public void onResponse(Call<List<RetroPhoto>> call, Response<List<RetroPhoto>> response) {
                progressDoalog.dismiss();
                generateDataList(response.body());
                generateDataList2(response.body());
                generateDataList3(response.body());
                generateDataList4(response.body());
                Log.d("뜨나", String.valueOf(response));
            }

            @Override
            public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(MainActivity3.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("뜨나", String.valueOf(t));
            }
        });
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<RetroPhoto> photoList) {
        List<RetroPhoto> newPhotoList = new ArrayList();

        for (RetroPhoto i :photoList) {
            if(String.valueOf(i.getcategory()).equals("outer")){
                newPhotoList.add(i);
            }
        }

        recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(MainActivity3.this, newPhotoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity3.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void generateDataList2(List<RetroPhoto> photoList) {
        List<RetroPhoto> newPhotoList = new ArrayList();

        for (RetroPhoto i :photoList) {
            if(String.valueOf(i.getcategory()).equals("top")){
                newPhotoList.add(i);
            }
        }

        recyclerView = findViewById(R.id.custom2RecyclerView);
        adapter = new CustomAdapter(MainActivity3.this, newPhotoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity3.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void generateDataList3(List<RetroPhoto> photoList) {
        List<RetroPhoto> newPhotoList = new ArrayList();

        for (RetroPhoto i :photoList) {
            if(String.valueOf(i.getcategory()).equals("bottom")){
                newPhotoList.add(i);
            }
        }

        recyclerView = findViewById(R.id.custom3RecyclerView);
        adapter = new CustomAdapter(MainActivity3.this, newPhotoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity3.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void generateDataList4(List<RetroPhoto> photoList) {
        List<RetroPhoto> newPhotoList = new ArrayList();

        for (RetroPhoto i :photoList) {
            if(String.valueOf(i.getcategory()).equals("shoes")){
                newPhotoList.add(i);
            }
        }

        recyclerView = findViewById(R.id.custom4RecyclerView);
        adapter = new CustomAdapter(MainActivity3.this, newPhotoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity3.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}