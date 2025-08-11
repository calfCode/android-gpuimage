package jp.co.cyberagent.android.gpuimage.sample.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.ImageReader;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.hardware.camera2.CaptureRequest;
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class Camera2Loader1 extends CameraLoaderInterface {

    private static final String TAG = "Camera2Loader";
    private static final int PREVIEW_WIDTH = 480;
    private static final int PREVIEW_HEIGHT = 640;

    private final Activity activity;
    private CameraDevice cameraInstance;
    private CameraCaptureSession captureSession;
    private ImageReader imageReader;
    private int cameraFacing = CameraCharacteristics.LENS_FACING_BACK;
    private int viewWidth = 0;
    private int viewHeight = 0;

    private CameraManager cameraManager;

    public Camera2Loader1(Activity activity) {
        this.activity = activity;
        this.cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
    }

    @Override
    public void onResume(int width, int height) {
        viewWidth = width;
        viewHeight = height;
        setUpCamera();
    }

    @Override
    public void onPause() {
        releaseCamera();
    }

    @Override
    public void switchCamera() {
        if (cameraFacing == CameraCharacteristics.LENS_FACING_BACK) {
            cameraFacing = CameraCharacteristics.LENS_FACING_FRONT;
        } else if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
            cameraFacing = CameraCharacteristics.LENS_FACING_BACK;
        } else {
            return;
        }
        releaseCamera();
        setUpCamera();
    }

    @Override
    public int getCameraOrientation() {
        int degrees = 0;
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
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

        String cameraId = getCameraId(cameraFacing);
        if (cameraId == null) return 0;

        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            Integer orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (orientation == null) return 0;

            if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                return (orientation + degrees) % 360;
            } else { // back-facing
                return (orientation - degrees) % 360;
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to get camera orientation", e);
            return 0;
        }
    }

    @Override
    public boolean hasMultipleCamera() {
        try {
            return cameraManager.getCameraIdList().length > 1;
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to get camera id list", e);
            return false;
        }
    }

    @SuppressLint("MissingPermission")
    private void setUpCamera() {
        String cameraId = getCameraId(cameraFacing);
        if (cameraId == null) return;

        try {
            cameraManager.openCamera(cameraId, new CameraDeviceCallback(), null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Opening camera (ID: " + cameraId + ") failed.", e);
        }
    }

    private void releaseCamera() {
        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }

        if (cameraInstance != null) {
            cameraInstance.close();
            cameraInstance = null;
        }

        if (captureSession != null) {
            captureSession.close();
            captureSession = null;
        }
    }

    private String getCameraId(int facing) {
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            for (String id : cameraIdList) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                Integer cameraFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraFacing != null && cameraFacing == facing) {
                    return id;
                }
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to get camera id", e);
        }
        return null;
    }

    private void startCaptureSession() {
        Size size = chooseOptimalSize();
        imageReader = ImageReader.newInstance(size.getWidth(), size.getHeight(), ImageFormat.YUV_420_888, 2);
        imageReader.setOnImageAvailableListener(reader -> {
            android.media.Image image = reader.acquireNextImage();
            if (image == null) return;

            if (onPreviewFrame != null) {
                byte[] nv21Data = ImageUtils.generateNV21Data(image);
                onPreviewFrame.onPreviewFrame(nv21Data, image.getWidth(), image.getHeight());
            }
            image.close();
        }, null);

        try {
            if (cameraInstance != null) {
                cameraInstance.createCaptureSession(
                        Collections.singletonList(imageReader.getSurface()),
                        new CaptureStateCallback(),
                        null
                );
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to start camera session", e);
        }
    }

    private Size chooseOptimalSize() {
        if (viewWidth == 0 || viewHeight == 0) {
            return new Size(0, 0);
        }

        String cameraId = getCameraId(cameraFacing);
        if (cameraId == null) return new Size(0, 0);

        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            android.hardware.camera2.params.StreamConfigurationMap map =
                    characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            if (map == null) return new Size(PREVIEW_WIDTH, PREVIEW_HEIGHT);

            Size[] outputSizes = map.getOutputSizes(ImageFormat.YUV_420_888);
            if (outputSizes == null || outputSizes.length == 0) {
                return new Size(PREVIEW_WIDTH, PREVIEW_HEIGHT);
            }

            int orientation = getCameraOrientation();
            int maxPreviewWidth = (orientation == 90 || orientation == 270) ? viewHeight : viewWidth;
            int maxPreviewHeight = (orientation == 90 || orientation == 270) ? viewWidth : viewHeight;

            // Filter sizes and find the largest one that fits within our constraints
            Size optimalSize = null;
            int maxArea = 0;

            for (Size size : outputSizes) {
                if (size.getWidth() < maxPreviewWidth / 2 && size.getHeight() < maxPreviewHeight / 2) {
                    int area = size.getWidth() * size.getHeight();
                    if (area > maxArea) {
                        optimalSize = size;
                        maxArea = area;
                    }
                }
            }

            return optimalSize != null ? optimalSize : new Size(PREVIEW_WIDTH, PREVIEW_HEIGHT);

        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to get camera characteristics", e);
            return new Size(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        }
    }

    private class CameraDeviceCallback extends CameraDevice.StateCallback {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraInstance = camera;
            startCaptureSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            cameraInstance = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            cameraInstance = null;
        }
    }

    private class CaptureStateCallback extends CameraCaptureSession.StateCallback {
        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e(TAG, "Failed to configure capture session.");
        }

        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            if (cameraInstance == null) return;

            captureSession = session;
            try {
                CaptureRequest.Builder builder =
                        cameraInstance.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                builder.addTarget(imageReader.getSurface());
                session.setRepeatingRequest(builder.build(), null, null);
            } catch (CameraAccessException e) {
                Log.e(TAG, "Failed to start camera preview because it couldn't access camera", e);
            } catch (IllegalStateException e) {
                Log.e(TAG, "Failed to start camera preview.", e);
            }
        }
    }
}
