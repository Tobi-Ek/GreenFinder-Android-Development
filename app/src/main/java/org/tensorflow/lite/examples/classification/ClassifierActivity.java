/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.classification;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Size;
import android.util.TypedValue;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.List;

import org.tensorflow.lite.examples.classification.Utilities.NutDescription;
import org.tensorflow.lite.examples.classification.Utilities.Nutrients;
import org.tensorflow.lite.examples.classification.env.BorderedText;
import org.tensorflow.lite.examples.classification.env.Logger;
import org.tensorflow.lite.examples.classification.tflite.Classifier;
import org.tensorflow.lite.examples.classification.tflite.Classifier.Device;
import org.tensorflow.lite.examples.classification.tflite.Classifier.Model;

public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener {
  private static final Logger LOGGER = new Logger();
  private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
  private static final float TEXT_SIZE_DIP = 10;
  private Bitmap rgbFrameBitmap = null;
  private long lastProcessingTimeMs;
  private Integer sensorOrientation;
  private Classifier classifier;
  private BorderedText borderedText;
  /** Input image size of the model along x axis. */
  private int imageSizeX;
  /** Input image size of the model along y axis. */
  private int imageSizeY;

  @Override
  protected int getLayoutId() {
    return R.layout.tfe_ic_camera_connection_fragment;
  }

  @Override
  protected Size getDesiredPreviewFrameSize() {
    return DESIRED_PREVIEW_SIZE;
  }

  @Override
  public void onPreviewSizeChosen(final Size size, final int rotation) {
    final float textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    borderedText = new BorderedText(textSizePx);
    borderedText.setTypeface(Typeface.MONOSPACE);

    recreateClassifier(getModel(), getDevice(), getNumThreads());
    if (classifier == null) {
      LOGGER.e("No classifier on preview!");
      return;
    }

    previewWidth = size.getWidth();
    previewHeight = size.getHeight();

    sensorOrientation = rotation - getScreenOrientation();
    LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

    LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
    rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
  }

  @Override
  protected void processImage() {
    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
    final int cropSize = Math.min(previewWidth, previewHeight);

    runInBackground(
        new Runnable() {
          @Override
          public void run() {
            if (classifier != null) {
              final long startTime = SystemClock.uptimeMillis();
              final List<Classifier.Recognition> results =
                  classifier.recognizeImage(rgbFrameBitmap, sensorOrientation);
              lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
              LOGGER.v("Detect: %s", results);

              runOnUiThread(
                  new Runnable() {
                    @Override
                    public void run() {
                    Dictionary topRecognitionDic = showResultsInBottomSheet(results);//---------------------------------------------------------
                     // if( topRecognition.toLowerCase()=="orange" || topRecognition.toLowerCase()=="apple"){
                      try{
                        InputStream is= getApplicationContext().getAssets().open("nutrient_list.json");
                        InputStream isDes=getApplicationContext().getAssets().open("nutrient_desc.json");
                        Nutrients nutrients=Nutrients.getNutrients(topRecognitionDic,is);
                        NutDescription nutDescription=NutDescription.getNutDesc(topRecognitionDic,isDes);
                        nutDesTextView.setText(nutDescription.getDesc());
                        nutTextView.setText(nutrients.getNut());
                        nut1TextView.setText(nutrients.getNut1());
                        nut2TextView.setText(nutrients.getNut2());
                        nut3TextView.setText(nutrients.getNut3());
                        nut4TextView.setText(nutrients.getNut4());
                        nut5TextView.setText(nutrients.getNut5());
                        nut6TextView.setText(nutrients.getNut6());
                        nut7TextView.setText(nutrients.getNut7());
                        nut8TextView.setText(nutrients.getNut8());
                        nut9TextView.setText(nutrients.getNut9());
                        nut10TextView.setText(nutrients.getNut10());

                        nutValueTextView.setText(nutrients.getNut_val());
                        nut1ValueTextView.setText(nutrients.getNut1_val());
                        nut2ValueTextView.setText(nutrients.getNut2_val());
                        nut3ValueTextView.setText(nutrients.getNut3_val());
                        nut4ValueTextView.setText(nutrients.getNut4_val());
                        nut5ValueTextView.setText(nutrients.getNut5_val());
                        nut6ValueTextView.setText(nutrients.getNut6_val());
                        nut7ValueTextView.setText(nutrients.getNut7_val());
                        nut8ValueTextView.setText(nutrients.getNut8_val());
                        nut9ValueTextView.setText(nutrients.getNut9_val());
                        nut10ValueTextView.setText(nutrients.getNut10_val());
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                      showInference(lastProcessingTimeMs + "ms");
                    }
                  });
            }
            readyForNextImage();
          }
        });
  }

  @Override
  protected void onInferenceConfigurationChanged() {
    if (rgbFrameBitmap == null) {
      // Defer creation until we're getting camera frames.
      return;
    }
    final Device device = getDevice();
    final Model model = getModel();
    final int numThreads = getNumThreads();
    runInBackground(() -> recreateClassifier(model, device, numThreads));
  }

  private void recreateClassifier(Model model, Device device, int numThreads) {
    if (classifier != null) {
      LOGGER.d("Closing classifier.");
      classifier.close();
      classifier = null;
    }
    if (device == Device.GPU
        && (model == Model.QUANTIZED_MOBILENET || model == Model.QUANTIZED_EFFICIENTNET)) {
      LOGGER.d("Not creating classifier: GPU doesn't support quantized models.");
      runOnUiThread(
          () -> {
            Toast.makeText(this, R.string.tfe_ic_gpu_quant_error, Toast.LENGTH_LONG).show();
          });
      return;
    }
    try {
      LOGGER.d(
          "Creating classifier (model=%s, device=%s, numThreads=%d)", model, device, numThreads);
      classifier = Classifier.create(this, model, device, numThreads);
    } catch (IOException e) {
      LOGGER.e(e, "Failed to create classifier.");
    }

    // Updates the input image size.
    imageSizeX = classifier.getImageSizeX();
    imageSizeY = classifier.getImageSizeY();
  }
}
