package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecomActivity extends AppCompatActivity {
    private RecomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recom);

        progressDoalog = new ProgressDialog(RecomActivity.this);
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
            }

            @Override
            public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(RecomActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<RetroPhoto> photoList) {
        String [][] newPhotoList = new String[3][99];


        int j = 0;
        for (RetroPhoto i :photoList) {
            if(String.valueOf(i.getcategory()).equals("top")){
                newPhotoList[0][j] = String.valueOf(i.getUrl());
                j = j+1;
            }
        }

        j=0;
        for (RetroPhoto i :photoList) {
            if(String.valueOf(i.getcategory()).equals("bottom")){
                newPhotoList[1][j] = String.valueOf(i.getUrl());
                j = j+1;
            }
        }

        j = 0;
        for (RetroPhoto i :photoList) {
            if(String.valueOf(i.getcategory()).equals("shoes")){
                newPhotoList[2][j] = String.valueOf(i.getUrl());
                j = j+1;
            }
        }

        recyclerView = findViewById(R.id.rv_recom);
        adapter = new RecomAdapter(RecomActivity.this, newPhotoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecomActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}