package jp.co.cyberagent.android.gpuimage.sample.utils;

import android.app.Activity;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;

@SuppressWarnings("deprecation")
public class Camera1Loader1 extends CameraLoaderInterface {

    private static final String TAG = "Camera1Loader";

    private final Activity activity;
    private Camera cameraInstance;
    private int cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;

    public Camera1Loader1(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onResume(int width, int height) {
        setUpCamera();
    }

    @Override
    public void onPause() {
        releaseCamera();
    }

    @Override
    public void switchCamera() {
        if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
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

        if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (90 + degrees) % 360;
        } else { // back-facing
            return (90 - degrees) % 360;
        }
    }

    @Override
    public boolean hasMultipleCamera() {
        return Camera.getNumberOfCameras() > 1;
    }

    private void setUpCamera() {
        int id = getCurrentCameraId();
        try {
            cameraInstance = getCameraInstance(id);
        } catch (IllegalAccessError e) {
            Log.e(TAG, "Camera not found");
            return;
        }

        Camera.Parameters parameters = cameraInstance.getParameters();

        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        cameraInstance.setParameters(parameters);

        cameraInstance.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (data == null || camera == null) {
                    return;
                }
                Camera.Size size = camera.getParameters().getPreviewSize();
                if (Camera1Loader1.this.onPreviewFrame != null) {
                    Camera1Loader1.this.onPreviewFrame.onPreviewFrame(data, size.width, size.height);
                }
            }
        });
        cameraInstance.startPreview();
    }

    private int getCurrentCameraId() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int id = 0; id < Camera.getNumberOfCameras(); id++) {
            Camera.getCameraInfo(id, cameraInfo);
            if (cameraInfo.facing == cameraFacing) {
                return id;
            }
        }
        return 0;
    }

    private Camera getCameraInstance(int id) {
        try {
            return Camera.open(id);
        } catch (Exception e) {
            throw new IllegalAccessError("Camera not found");
        }
    }

    private void releaseCamera() {
        cameraInstance.setPreviewCallback(null);
        cameraInstance.release();
        cameraInstance = null;
    }
}
