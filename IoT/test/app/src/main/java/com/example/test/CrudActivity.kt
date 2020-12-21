package com.example.test

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import kotlinx.android.synthetic.main.activity_crud.*
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class CrudActivity : AppCompatActivity() {

    private val TAG = "MyTag"
    private val PERMISSIONS_REQUEST_CODE = 100
    private val REQUIRED_PERMISSIONS =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private var CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_FRONT
    private var myCameraPreview: MyCameraPreview? = null

    private var ac1 = this

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

        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)

            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }


        btnCapture.setOnClickListener {
            myCameraPreview?.takePicture()



            uploadFile()
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

    // 모델 파일 인터프리터를 생성하는 공통 함수
    // loadModelFile 함수에 예외가 포함되어 있기 때문에 반드시 try, catch 블록이 필요하다.


    private fun uploadFile() {
        try {
                    // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
                    Amplify.addPlugin(AWSCognitoAuthPlugin())
                    Amplify.addPlugin(AWSS3StoragePlugin())
                    Amplify.configure(applicationContext)

                    Log.i("MyAmplifyApp", "Initialized Amplify")
                } catch (error: AmplifyException) {
                    Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
                }

        val exampleFile = File(applicationContext.filesDir, "ExampleKey")

        exampleFile.writeText("Example file contents")

        Amplify.Storage.uploadFile(
            "ExampleKey",
            exampleFile,
            StorageUploadFileOptions.defaultInstance(),
            { progress -> Log.i("MyAmplifyApp", "Fraction completed: ${progress.fractionCompleted}") },
            { result -> Log.i("MyAmplifyApp", "Successfully uploaded: ${result.getKey()}") },
            { error -> Log.e("MyAmplifyApp", "Upload failed", error) }
        )

        Amplify.Storage.downloadFile(
            "ExampleKey",
            File("${applicationContext.filesDir.toString()}/123123.jpg"),
            { result -> Log.i("MyAmplifyApp", "Successfully downloaded: ${result.getFile().name}") },
            { error -> Log.e("MyAmplifyApp", "Download Failure", error) }
        )
    }

}