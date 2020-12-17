/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.example.test.tflite;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import android.widget.TextView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;

import com.example.test.ClassifierActivity;
import com.example.test.R;
import com.example.test.env.Logger;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import kotlin.jvm.internal.Intrinsics;

/** A classifier specialized to label images using TensorFlow Lite. */
public abstract class Classifier {
  private static final Logger LOGGER = new Logger();

  /** The runtime device type used for executing classification. */
  public enum Device {
    CPU,
    GPU
  }

  /** Number of results to show in the UI. */
  private static final int MAX_RESULTS = 3;

  /** The loaded TensorFlow Lite model. */
  private MappedByteBuffer tfliteModel;

  /** Image size along the x axis. */
  private final int imageSizeX;

  /** Image size along the y axis. */
  private final int imageSizeY;

  private Activity context;
  private Context context2;
  /** Optional GPU delegate for acceleration. */
  // TODO: Declare a GPU delegate


  /** An instance of the driver class to run model inference with Tensorflow Lite. */
  // TODO: Declare a TFLite interpreter
  protected Interpreter tflite;


  /** Options for configuring the Interpreter. */
  private final Interpreter.Options tfliteOptions = new Interpreter.Options();

  /** Labels corresponding to the output of the vision model. */
  private List<String> labels;

  /** Input image TensorBuffer. */
  private TensorImage inputImageBuffer;

  /** Output probability TensorBuffer. */
  private final TensorBuffer outputProbabilityBuffer;

  /** Processer to apply post processing of the output probability. */
  private final TensorProcessor probabilityProcessor;

  /**
   * Creates a classifier with the provided configuration.
   *
   * @param activity The current Activity.
   * @param device The device to use for classification.
   * @param numThreads The number of threads to use for classification.
   * @return A classifier with the desired configuration.
   */
  public static Classifier create(Activity activity, Device device, int numThreads)
      throws IOException {

    return new ClassifierFloatMobileNet(activity, device, numThreads);
  }

  /** An immutable result returned by a Classifier describing what was recognized. */
  public static class Recognition {
    /**
     * A unique identifier for what has been recognized. Specific to the class, not the instance of
     * the object.
     */
    private final String id;

    /** Display name for the recognition. */
    private final String title;

    /**
     * A sortable score for how good the recognition is relative to others. Higher should be better.
     */
    private final Float confidence;

    /** Optional location within the source image for the location of the recognized object. */
    private RectF location;

    public Recognition(
            final String id, final String title, final Float confidence, final RectF location) {
      this.id = id;
      this.title = title;
      this.confidence = confidence;
      this.location = location;
    }

    public String getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    public Float getConfidence() {
      return confidence;
    }

    public RectF getLocation() {
      return new RectF(location);
    }

    public void setLocation(RectF location) {
      this.location = location;
    }

    @Override
    public String toString() {
      String resultString = "";
      if (id != null) {
        resultString += "[" + id + "] ";
      }

      if (title != null) {
        resultString += title + " ";
      }

      if (confidence != null) {
        resultString += String.format("(%.1f%%) ", confidence * 100.0f);
      }

      if (location != null) {
        resultString += location + " ";
      }

      return resultString.trim();
    }
  }

  /** Initializes a {@code Classifier}. */
  protected Classifier(Activity context, Device device, int numThreads) throws IOException {
    this.context = context;
    tfliteModel = FileUtil.loadMappedFile(context, getModelPath());

    context2 = context;


    switch (device) {
      case GPU:
        // TODO: Create a GPU delegate instance and add it to the interpreter options

        break;
      case CPU:
        break;
    }
    tfliteOptions.setNumThreads(numThreads);

    // TODO: Create a TFLite interpreter instance
    tflite = new Interpreter(tfliteModel, tfliteOptions);

    // Loads labels out from the label file.
    labels = FileUtil.loadLabels(context, getLabelPath());

    // Reads type and shape of input and output tensors, respectively.
    int imageTensorIndex = 0;
    int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
    imageSizeY = imageShape[1];
    imageSizeX = imageShape[2];
    DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();
    int probabilityTensorIndex = 0;
    int[] probabilityShape =
        tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
    DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

    // Creates the input tensor.
    inputImageBuffer = new TensorImage(imageDataType);

    // Creates the output tensor and its processor.
    outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);

    // Creates the post processor for the output probability.
    probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

    LOGGER.d("Created a Tensorflow Lite Image Classifier.");
  }

  /** Runs inference and returns the classification results. */
  public List<Recognition> recognizeImage(final Bitmap bitmap, int sensorOrientation) {
    // Logs this method so that it can be analyzed with systrace.
    Trace.beginSection("recognizeImage");

    Trace.beginSection("loadImage");
    long startTimeForLoadImage = SystemClock.uptimeMillis();
    inputImageBuffer = loadImage(bitmap, sensorOrientation);
    long endTimeForLoadImage = SystemClock.uptimeMillis();
    Trace.endSection();
    LOGGER.v("Timecost to load the image: " + (endTimeForLoadImage - startTimeForLoadImage));

    // 레츠고!!
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

    Interpreter tflite2 = getTfliteInterpreter("color.tflite");

    if (tflite2 == null) {
      Intrinsics.throwNpe();
    }

    tflite2.run(input, output);


    HashMap<Integer,String> map = new HashMap<>();
    map.put(0,"빨간색");
    map.put(1,"초록색");
    map.put(2,"파란색");
    map.put(3,"하늘색");
    map.put(4,"갈색");
    map.put(5,"핑크색");
    map.put(6,"네이비색");
    map.put(7,"노란색");
    map.put(8,"오렌지색");
    map.put(9,"흰색");
    map.put(10,"회색");
    map.put(11,"검정색");


    Log.d("해석", String.valueOf(output));
    for(a=0; a<12; ++a) {
      Log.d("해석", String.valueOf(map.get(a) + " " + output[0][a]*100 + "%"));
    }

//    Arrays.sort(output[0]);
    float max = output[0][0];
    int k = 0 ;
    for(int i=1 ; i<12 ; i++){ if(output[0][i] >= max){ max = output[0][i]; k=i; } }

    Log.d("최대값은", String.valueOf(map.get(k) + " " + max*100 + "%"));


    TextView text1 = (TextView) ((ClassifierActivity) context2).findViewById(R.id.tv_ai_color);
    text1.setText(String.valueOf(map.get(k) + " " + max*100 + "%"));

    // 레츠고 끝!!

    // Runs the inference call.
    Trace.beginSection("runInference");
    long startTimeForReference = SystemClock.uptimeMillis();
    // TODO: Run TFLite inference
    tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());

    long endTimeForReference = SystemClock.uptimeMillis();
    Trace.endSection();
    LOGGER.v("Timecost to run model inference: " + (endTimeForReference - startTimeForReference));

    // Gets the map of label and probability.
    // TODO: Use TensorLabel from TFLite Support Library to associate the probabilities
    //       with category labels
    Map<String, Float> labeledProbability =
            new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                    .getMapWithFloatValue();

    Trace.endSection();

    // Gets top-k results.
    return getTopKProbability(labeledProbability);
  }

  /** Closes the interpreter and model to release resources. */
  public void close() {
    if (tflite != null) {
      // TODO: Close the interpreter

      tflite.close();
      tflite = null;
    }
    // TODO: Close the GPU delegate


    tfliteModel = null;
  }

  /** Get the image size along the x axis. */
  public int getImageSizeX() {
    return imageSizeX;
  }

  /** Get the image size along the y axis. */
  public int getImageSizeY() {
    return imageSizeY;
  }

  /** Loads input image, and applies preprocessing. */
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
                    .add(new ResizeOp(imageSizeX, imageSizeY, ResizeMethod.NEAREST_NEIGHBOR))
                    .add(new Rot90Op(numRoration))
                    .add(getPreprocessNormalizeOp())
                    .build();
    return imageProcessor.process(inputImageBuffer);
  }

  /** Gets the top-k results. */
  private static List<Recognition> getTopKProbability(Map<String, Float> labelProb) {
    // Find the best classifications.
    PriorityQueue<Recognition> pq =
        new PriorityQueue<>(
            MAX_RESULTS,
            new Comparator<Recognition>() {
              @Override
              public int compare(Recognition lhs, Recognition rhs) {
                // Intentionally reversed to put high confidence at the head of the queue.
                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
              }
            });

    for (Map.Entry<String, Float> entry : labelProb.entrySet()) {
      pq.add(new Recognition("" + entry.getKey(), entry.getKey(), entry.getValue(), null));
    }

    final ArrayList<Recognition> recognitions = new ArrayList<>();
    int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
    for (int i = 0; i < recognitionsSize; ++i) {
      recognitions.add(pq.poll());
    }
    return recognitions;
  }

  /** Gets the name of the model file stored in Assets. */
  protected abstract String getModelPath();

  /** Gets the name of the label file stored in Assets. */
  protected abstract String getLabelPath();

  /** Gets the TensorOperator to nomalize the input image in preprocessing. */
  protected abstract TensorOperator getPreprocessNormalizeOp();

  /**
   * Gets the TensorOperator to dequantize the output probability in post processing.
   *
   * <p>For quantized model, we need de-quantize the prediction with NormalizeOp (as they are all
   * essentially linear transformation). For float model, de-quantize is not required. But to
   * uniform the API, de-quantize is added to float model too. Mean and std are set to 0.0f and
   * 1.0f, respectively.
   */
  protected abstract TensorOperator getPostprocessNormalizeOp();


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
}
