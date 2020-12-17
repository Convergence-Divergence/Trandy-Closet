package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.common.ops.NormalizeOp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import kotlin.jvm.internal.Intrinsics;



public class MyCameraPreview extends SurfaceView implements SurfaceHolder.Callback {


    private final String TAG = "MyTag";

    private SurfaceHolder mHolder;

    private int mCameraID;

    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;

    private TensorImage inputImageBuffer;

    /** Image size along the x axis. */
    private final int imageSizeX = 0;
    /** Image size along the y axis. */
    private final int imageSizeY = 0;

    private final float PROBABILITY_MEAN = 0.0f;
    private final float PROBABILITY_STD = 1.0f;

    private int mDisplayOrientation;
    private Activity context;

    public MyCameraPreview(Activity context, int cameraId) {
        super(context);
        this.context = context;
        Log.d(TAG, "MyCameraPreview cameraId : " + cameraId);

        // 0    ->     CAMERA_FACING_BACK
        // 1    ->     CAMERA_FACING_FRONT
        mCameraID = cameraId;

        try {
            // attempt to get a Camera instance
            mCamera = Camera.open(mCameraID);
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera is not available");
        }


        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // get display orientation
        mDisplayOrientation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();



    }



    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");

        // retrieve camera's info.
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, cameraInfo);

        mCameraInfo = cameraInfo;

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.d(TAG, "surfaceChanged");
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            Log.e(TAG, "preview surface does not exist");
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
            Log.d(TAG, "Preview stopped.");
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }


        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
        mCamera.setDisplayOrientation(orientation);

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            Log.d(TAG, "Camera preview started.");
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

    }

    /**
     * 안드로이드 디바이스 방향에 맞는 카메라 프리뷰를 화면에 보여주기 위해 계산합니다.
     */
    public int calculatePreviewOrientation(Camera.CameraInfo info, int rotation) {
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    public void takePicture(){

        mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    /**
     * 이미지 저장을 위한 콜백 함수
     */
    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {

        }
    };

    private Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };


    private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            //이미지의 너비와 높이 결정
            int w = camera.getParameters().getPictureSize().width;
            int h = camera.getParameters().getPictureSize().height;
            int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);

            //Log.d("MyTag","이미지 캡처 시 -> orientation : " + orientation);

            //byte array 를 bitmap 으로 변환
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray( data, 0, data.length, options);


            // 여기서 색 텐서 모델 사용!!!!
            int redColors = 0;
            int greenColors = 0;
            int blueColors = 0;
            int pixelCount = 0;

            for (int y = 0; y < bitmap.getHeight(); y++)
            {
                for (int x = 0; x < bitmap.getWidth(); x++)
                {
                    int c = bitmap.getPixel(x, y);
                    pixelCount++;
                    redColors += Color.red(c);
                    greenColors += Color.green(c);
                    blueColors += Color.blue(c);
                }
            }
            // calculate average of bitmap r,g,b values
            float red = (redColors/pixelCount);
            float green = (greenColors/pixelCount);
            float blue = (blueColors/pixelCount);

            byte var3 = 1;
            int a;
            float[] var11;
            float[][] var4 = new float[var3][];
            for(a = 0; a < var3; ++a) {
                var11 = new float[3];
                var4[a] = var11;
            }

            float[][] input = (float[][])var4;


            input[0][0] = red;
            input[0][1] = green;
            input[0][2] = blue;


            byte var13 = 1;
            float[][] var15 = new float[var13][];
            int var6;
            for(var6 = 0; var6 < var13; ++var6) {
                var11 = new float[12];
                var15[var6] = var11;
            }

            float[][] output = (float[][])var15;

            // 색모델 모델을 해석할 인터프리터 생성

            // 모델 구동.
            // 정확하게는 from_session 함수의 output_tensors 매개변수에 전달된 연산 호출


            Interpreter tflite = getTfliteInterpreter("color.tflite");

            if (tflite == null) {
                Intrinsics.throwNpe();
            }

            tflite.run(input, output);

            Log.d("해석", String.valueOf(output));
            for(a=0; a<12; ++a) {
                Log.d("해석", String.valueOf(a + output[0][a]));
            }




            // 색 텐서 모델 사용 끝!!!!!

            //이미지를 디바이스 방향으로 회전
            Matrix matrix = new Matrix();

            /**
             * 셀카 모드에는 저장 시 좌우 반전을 해줘야 한다.
             */
            if(mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                //Log.d("MyTag","180도 추가 회전 후 좌우반전을 해줍니다.");
                //orientation += 180;
                matrix.setScale(-1,1);
            }

            matrix.postRotate(orientation);
            bitmap =  Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

            //bitmap 을  byte array 로 변환
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] currentData = stream.toByteArray();

            //파일로 저장
            Log.d("데이타!", String.valueOf(currentData));
            new MyCameraPreview.SaveImageTask().execute(currentData);




        }
    };


    /**
     * 이미지 저장을 위한 콜백 클레스
     */
    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            try {

                File path = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest");
                if (!path.exists()) {
                    path.mkdirs();
                }

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outputFile = new File(path, fileName);

                outStream = new FileOutputStream(outputFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to "
                        + outputFile.getAbsolutePath());


                mCamera.startPreview();


                // 갤러리에 반영
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(outputFile));
                Log.d("데이타!22", String.valueOf(outputFile));
                getContext().sendBroadcast(mediaScanIntent);


                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                    Log.d(TAG, "Camera preview started.");
                } catch (Exception e) {
                    Log.d(TAG, "Error starting camera preview: " + e.getMessage());
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }


    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(context, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private TensorImage loadImage(final Bitmap bitmap, int sensorOrientation) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int numRoration = sensorOrientation / 90;
        // TODO: Define an ImageProcessor from TFLite Support Library to do preprocessing
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(new Rot90Op(numRoration))
                        .add(new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD))
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

}


