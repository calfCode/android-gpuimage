package jp.co.cyberagent.android.gpuimage.sample.utils;

public abstract class CameraLoaderInterface {

    protected PreviewFrameCallback onPreviewFrame;

    public abstract void onResume(int width, int height);

    public abstract void onPause();

    public abstract void switchCamera();

    public abstract int getCameraOrientation();

    public abstract boolean hasMultipleCamera();

    public void setOnPreviewFrameListener(PreviewFrameCallback onPreviewFrame) {
        this.onPreviewFrame = onPreviewFrame;
    }

    // Interface to replace Kotlin's function type
    public interface PreviewFrameCallback {
        void onPreviewFrame(byte[] data, int width, int height);
    }
}
