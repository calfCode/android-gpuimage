/*
 * Copyright (C) 2018 CyberAgent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage.sample;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster;
import jp.co.cyberagent.android.gpuimage.sample.utils.Camera1Loader1;
import jp.co.cyberagent.android.gpuimage.sample.utils.Camera2Loader1;
import jp.co.cyberagent.android.gpuimage.sample.utils.CameraLoaderInterface;
import jp.co.cyberagent.android.gpuimage.util.Rotation;


public class Camera2Activity extends AppCompatActivity {

    private GPUImageView gpuImageView;
    private SeekBar seekBar;
    private CameraLoaderInterface cameraLoader;
    private FilterAdjuster filterAdjuster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        gpuImageView = findViewById(R.id.surfaceView);
        seekBar      = findViewById(R.id.seekBar);

        // 初始化 CameraLoader
        if (Build.VERSION.SDK_INT < 21) {
            cameraLoader = new Camera1Loader1(this);
        } else {
            cameraLoader = new Camera2Loader1(this);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (filterAdjuster != null) {
                    filterAdjuster.adjust(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.button_choose_filter).setOnClickListener(v ->
                GPUImageFilterTools2.showDialog(this, filter -> {
                    switchFilterTo(filter);
                    gpuImageView.requestRender();
                }));

        findViewById(R.id.button_capture).setOnClickListener(v -> saveSnapshot());

        ImageView switchBtn = findViewById(R.id.img_switch_camera);
        if (!cameraLoader.hasMultipleCamera()) {
            switchBtn.setVisibility(View.GONE);
        }
        switchBtn.setOnClickListener(v -> {
            cameraLoader.switchCamera();
            gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()));
        });

        cameraLoader.setOnPreviewFrameListener(new CameraLoaderInterface.PreviewFrameCallback() {
            @Override
            public void onPreviewFrame(byte[] data, int width, int height) {
                gpuImageView.updatePreviewFrame(data, width, height);
            }
        });
        gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()));
        gpuImageView.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpuImageView.post(() -> cameraLoader.onResume(gpuImageView.getWidth(), gpuImageView.getHeight()));
    }

    @Override
    protected void onPause() {
        cameraLoader.onPause();
        super.onPause();
    }

    private void saveSnapshot() {
        String folderName = "GPUImage";
        String fileName   = System.currentTimeMillis() + ".jpg";
        gpuImageView.saveToPictures(folderName, fileName, uri ->
                Toast.makeText(this, folderName + "/" + fileName + " saved", Toast.LENGTH_SHORT).show());
    }

    private Rotation getRotation(int orientation) {
        switch (orientation) {
            case 90:  return Rotation.ROTATION_90;
            case 180: return Rotation.ROTATION_180;
            case 270: return Rotation.ROTATION_270;
            default:  return Rotation.NORMAL;
        }
    }

    private void switchFilterTo(GPUImageFilter filter) {
        if (gpuImageView.getFilter() == null ||
                !gpuImageView.getFilter().getClass().equals(filter.getClass())) {

            gpuImageView.setFilter(filter);
            filterAdjuster = new FilterAdjuster(filter);
            if (filterAdjuster.canAdjust()) {
                seekBar.setVisibility(View.VISIBLE);
                filterAdjuster.adjust(seekBar.getProgress());
            } else {
                seekBar.setVisibility(View.GONE);
            }
        }
    }
}