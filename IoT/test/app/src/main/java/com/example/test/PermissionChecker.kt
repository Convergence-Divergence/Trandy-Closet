package com.example.test

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionChecker(val activity: AppCompatActivity,
                        val permissions: Array<String>,
                        val requestCode: Int = 1000) {
    fun check(): Boolean {
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) !=
                    PackageManager.PERMISSION_GRANTED
        }
        if(notGranted.isEmpty()) { // 권한 획득
            return true
        }

        // 미획득 권한이 있는 경우
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
        return false
    }

    fun checkGranted(requestCode: Int,
                     permissions: Array<out String>,
                     grantResults: IntArray) : Boolean {

        if( requestCode == this.requestCode) {
            val notGranted = permissions.filterIndexed { index, s ->
                grantResults[index]!=PackageManager.PERMISSION_GRANTED
            }
            if(notGranted.isEmpty()) {
                return true
            }
        }
        return false
    }
}