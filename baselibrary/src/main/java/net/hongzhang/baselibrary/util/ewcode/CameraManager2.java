package net.hongzhang.baselibrary.util.ewcode;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

public class CameraManager2 implements Camera.AutoFocusCallback, Camera.PreviewCallback {
    /**
     * 相机的状态
     */
    private enum CameraState {
        CLOSED, OPEN, PREVIEW;
    }

    /**
     * 相机
     */
    private Camera mCamera;
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 扫描宽度
     */
    private int cameraWidth;
    /**
     * 屏幕高度
     */
    private int screenHeght;
    /**
     * 扫描高度
     */
    private int cameraHeight;
    /**
     * 相机状态
     */
    private CameraState mState;
    /**
     * 预览帧标准事件
     */
    private PreviewFrameShotListener mFrameShotListener;
    /**
     * 自动请求聚焦间隔时间
     */
    private static final int REQUEST_AUTO_FOCUS_INTERVAL_MS = 1500;
    /**
     * 信息请求自动聚焦
     */
    private static final int MESSAGE_REQUEST_AUTO_FOCUS = 0;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_REQUEST_AUTO_FOCUS:
                    if (mState == CameraState.PREVIEW && mCamera != null) {
                        mCamera.autoFocus(CameraManager2.this);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @SuppressWarnings("deprecation")
    public CameraManager2(Context context) {
        //获取屏幕的长宽
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeght = display.getHeight();
        mState = CameraState.CLOSED;
    }

    /**
     * 初始摄像头
     *
     * @param holder 显示摄像头的holder
     */
    public boolean initCamera(SurfaceHolder holder) {
        try{
            mCamera = Camera.open();
        }catch (RuntimeException e){
            return false;
        }
        if (mCamera == null) {
            return false;
        }
        //设置当前的摄像头为打开
        mState = CameraState.OPEN;
        //设置显示
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();
        getBestPreviewSize(parameters, screenWidth, screenHeght);
        parameters.setPreviewSize(cameraHeight, cameraWidth);
        parameters.setPreviewFormat(ImageFormat.NV21);//Default
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean isCameraAvailable() {
        return mCamera == null ? false : true;
    }

    public boolean isFlashlightAvailable() {
        if (mCamera == null) {
            return false;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> flashModes = parameters.getSupportedFlashModes();
        boolean isFlashOnAvailable = false;
        boolean isFlashOffAvailable = false;
        for (String flashMode : flashModes) {
            if (Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
                isFlashOnAvailable = true;
            }
            if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
                isFlashOffAvailable = true;
            }
            if (isFlashOnAvailable && isFlashOffAvailable) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打开闪光灯
     */
    public void enableFlashlight() {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(parameters);
    }

    /**
     * 关闭闪光灯
     */
    public void disableFlashlight() {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);
    }

    /**
     * 开始预览
     */
    public void startPreview() {
        if (mCamera != null) {
            mState = CameraState.PREVIEW;
            mCamera.startPreview();
            mCamera.autoFocus(CameraManager2.this);
        }
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mState = CameraState.OPEN;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mCamera != null) {
            mCamera.setOneShotPreviewCallback(null);
            mCamera.release();
            mState = CameraState.CLOSED;
        }
    }

    public void requestPreviewFrameShot() {
        mCamera.setOneShotPreviewCallback(CameraManager2.this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mFrameShotListener != null) {
            data = rotateYUVdata90(data);
            mFrameShotListener.onPreviewFrame(data, cameraWidth, cameraHeight);
        }
    }

    /**
     * 获取与屏幕大小最相近的预览图像大小
     */
    private void getBestPreviewSize(Camera.Parameters parameters, int screenWidth, int screenHeght) {

        int diff = Integer.MAX_VALUE;
        List<Camera.Size> previewList = parameters.getSupportedPreviewSizes();
        for (Camera.Size previewSize : previewList) {
            // Rotate 90 degrees
            int previewWidth = previewSize.height;
            int previewHeight = previewSize.width;
            int newDiff = Math.abs(previewWidth - screenWidth) * Math.abs(previewWidth - screenWidth)
                    + Math.abs(previewHeight - screenHeght) * Math.abs(previewHeight - screenHeght);
            if (newDiff == 0) {
                cameraWidth = previewWidth;
                cameraHeight = previewHeight;

            } else if (newDiff < diff) {
                diff = newDiff;
                cameraWidth = previewWidth;
                cameraHeight = previewHeight;
            }
        }

    }

    /**
     * 因为预览图像和屏幕大小可能不一样，所以屏幕上的区域要根据比例转化为预览图像上对应的区域
     */
    public Rect getPreviewFrameRect(Rect screenFrameRect) {
        if (mCamera == null) {
            throw new IllegalStateException("Need call initCamera() before this.");
        }
        Rect previewRect = new Rect();
        previewRect.left = screenFrameRect.left * cameraWidth / screenWidth;
        previewRect.right = screenFrameRect.right * cameraWidth / screenWidth;
        previewRect.top = screenFrameRect.top * cameraHeight / screenHeght;
        previewRect.bottom = screenFrameRect.bottom * cameraHeight / screenHeght;
        return previewRect;
    }

    /**
     * 因为摄像头旋转了90度，对应旋转90度之后的YUV的参数也发生了相应的变化
     */
    private byte[] rotateYUVdata90(byte[] srcData) {
        byte[] desData = new byte[srcData.length];
        int srcWidth = cameraHeight;
        int srcHeight = cameraWidth;

        // Only copy Y
        int i = 0;
        for (int x = 0; x < srcWidth; x++) {
            for (int y = srcHeight - 1; y >= 0; y--) {
                desData[i++] = srcData[y * srcWidth + x];
            }
        }

        return desData;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (mState == CameraState.PREVIEW) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_REQUEST_AUTO_FOCUS, REQUEST_AUTO_FOCUS_INTERVAL_MS);
        }
    }

    public void setPreviewFrameShotListener(PreviewFrameShotListener l) {
        mFrameShotListener = l;
    }

    public interface PreviewFrameShotListener {
        public void onPreviewFrame(byte[] data, int cameraWidth, int cameraHeight);
    }

}
