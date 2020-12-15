package com.example.test

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_crud.*

class CrudActivity : AppCompatActivity() {

    private val TAG = "MyTag"
    private val PERMISSIONS_REQUEST_CODE = 100
    private val REQUIRED_PERMISSIONS =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private var CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_FRONT
    private var myCameraPreview: MyCameraPreview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 상태바를 안보이도록 합니다.
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // 화면 켜진 상태를 유지합니다.
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_crud)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // OS가 Marshmallow 이상일 경우 권한체크를 해야 합니다.

            val permissionCheckCamera
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            val permissionCheckStorage
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (permissionCheckCamera == PackageManager.PERMISSION_GRANTED

                && permissionCheckStorage == PackageManager.PERMISSION_GRANTED) {

                // 권한 있음
                Log.d(TAG, "권한 이미 있음")
                startCamera()


            } else {

                // 권한 없음
                Log.d(TAG, "권한 없음")
                ActivityCompat.requestPermissions(this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE)
            }


        } else {
            // OS가 Marshmallow 이전일 경우 권한체크를 하지 않는다.
            Log.d("MyTag", "마시멜로 버전 이하로 권한 이미 있음")
            startCamera()

        }

        btnCapture.setOnClickListener {
            myCameraPreview?.takePicture()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // grantResults[0] 거부 -> -1
        // grantResults[0] 허용 -> 0 (PackageManager.PERMISSION_GRANTED)

        Log.d(TAG, "requestCode : $requestCode, grantResults size : ${grantResults.size}")

        if(requestCode == PERMISSIONS_REQUEST_CODE) {

            var check_result = true

            for(result in grantResults) {
                if(result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }

            if(check_result) {

                startCamera()

            } else {

                Log.e(TAG, "권한 거부")
            }

        }

    }
    private fun startCamera() {

        Log.e(TAG, "startCamera")

        // Create our Preview view and set it as the content of our activity.
        myCameraPreview = MyCameraPreview(this, CAMERA_FACING)

        cameraPreview.addView(myCameraPreview)

    }
}