package com.example.test

import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MediaImage(val ctx: AppCompatActivity) {
    // List는 LinkedList, ArrayList 두 종류가 있다.
    fun getAllPhotos(): ArrayList<String> { // java
        // 모든 사진 정보 가져오기
        val cursor = ctx.contentResolver.query( // select
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, // where절
            null,  // groupby절
            null, // having절
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC" // orderby절
        )

        val imageUris = ArrayList<String>()

        cursor?.use {
            while (it.moveToNext()) {
                // 사진 경로 Uri 가지고 오기
                val uri = it.getString(it.getColumnIndexOrThrow(
                    MediaStore.Images.Media.DATA))
                Log.d("MainActivity", uri)
                imageUris.add(uri)
            }
        }
        return imageUris
    }
}