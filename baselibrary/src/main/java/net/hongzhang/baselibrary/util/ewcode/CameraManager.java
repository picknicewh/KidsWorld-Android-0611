package net.hongzhang.baselibrary.util.ewcode;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;

import net.hongzhang.baselibrary.util.G;

import java.io.IOException;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/11/4
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CameraManager implements Camera.PreviewCallback ,Camera.AutoFocusCallback {
    /**
     *相机的状态
     */
    private enum CameraState {
        CLOSED, OPEN, PREVIEW;
    }
    /**
     * 自动扫描间隔
     */
    private static final  int AUTO_SCAN_DURATION = 2000;
    /**
     * 自动扫描信息
     */
    private static final int MESSAGE_FOT_AUTO_ACAN = 0x01;
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
     * 相机
     */
    private Camera camera;
    /**
     * 相机状态
     */
    private CameraState mState;
    /**
     * 预览帧标准事件
     */
    private PreviewFrameShotListener mFrameShotListener;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==MESSAGE_FOT_AUTO_ACAN){
              if (mState==CameraState.PREVIEW&&camera!=null){
                  camera.autoFocus(CameraManager.this);
              }
            }
        }
    };
    public  CameraManager(Activity context){
        G.initDisplaySize(context);
        screenWidth = G.size.W;
        screenHeght =G.size.H;
        mState = CameraState.CLOSED;
    }
    public void initCamera(SurfaceHolder holder){
        camera = Camera.open();
        if (camera == null) {
            return ;
        }
        mState = CameraState.OPEN;
        camera.setDisplayOrientation(90);
        Camera.Parameters parameters = camera.getParameters();
        setCameraSize(parameters);

        parameters.setPreviewSize(cameraWidth,cameraHeight);
        parameters.setPreviewFormat(ImageFormat.NV21);
        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void   startPreview(){
        if (camera!=null){
            camera.startPreview();
            camera.autoFocus(CameraManager.this);
            mState = CameraState.PREVIEW;
        }
    }
    public void stopPreView(){
        if (camera!=null){
            camera.stopPreview();
            mState =CameraState.OPEN;
        }
    }
    public void release(){
        if (camera!=null){
            camera.setOneShotPreviewCallback(null);
            camera.release();
            mState = CameraState.CLOSED;
        }
    }
    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
      if (mFrameShotListener!=null){
          bytes = rotateYUVdata90(bytes);
          mFrameShotListener.onPreviewFrame(bytes,cameraWidth,cameraHeight);
      }
    }
    public boolean isCameraAvailable() {
        return camera == null ? false : true;
    }

    /**
     * 获取与屏幕大小最相近的预览图像大小
     * @param parameters 屏幕参数
     */
    private void setCameraSize(Camera.Parameters parameters){
        int diff = Integer.MAX_VALUE;
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size size :sizes){
            int previewHeight = size.height;
            int preViewWidth = size.width;
            int newDiff = Math.abs(preViewWidth - screenWidth) * Math.abs(preViewWidth - screenWidth)
                    + Math.abs(previewHeight - screenHeght) * Math.abs(previewHeight - screenHeght);
            if (newDiff==0){
                cameraHeight = previewHeight;
                cameraWidth =preViewWidth;
            } else if (newDiff<diff){
                diff = newDiff;
                cameraHeight = previewHeight;
                cameraWidth =preViewWidth;
            }
        }
    }
    /**
     * 因为摄像头旋转了90度，对应旋转90度之后的YUV的参数也发生了相应的变化
     */
    private byte[] rotateYUVdata90(byte[] srcData) {
        byte[] desData = new byte[srcData.length];
        int srcWidth = cameraWidth ;
        int srcHeight = cameraHeight;

        // Only copy Y
        int i = 0;
        for (int x = 0; x < srcWidth; x++) {
            for (int y = srcHeight - 1; y >= 0; y--) {
                desData[i++] = srcData[y * srcWidth + x];
            }
        }
        return desData;
    }

    /**
     * 因为预览图像和屏幕大小可能不一样，所以屏幕上的区域要根据比例转化为预览图像上对应的区域
     *  @param screenFrameRect xianji的矩形区域
     */
    public Rect getPreviewFrameRect(Rect screenFrameRect) {
        if (camera == null) {
            throw new IllegalStateException("Need call initCamera() before this.");
        }
        Rect previewRect = new Rect();
        previewRect.left = screenFrameRect.left * cameraWidth / screenWidth;
        previewRect.right = screenFrameRect.right *  cameraWidth / screenWidth;
        previewRect.top = screenFrameRect.top *cameraHeight/screenHeght;
        previewRect.bottom = screenFrameRect.bottom * cameraHeight/screenHeght;
        return previewRect;
    }
    public void setPreviewFrameShotListener(PreviewFrameShotListener l) {
        mFrameShotListener = l;
    }
    public void requestPreviewFrameShot() {
        camera.setOneShotPreviewCallback(CameraManager.this);
    }
    @Override
    public void onAutoFocus(boolean b, Camera camera) {
        if (mState==CameraState.PREVIEW){
            handler.sendEmptyMessageDelayed(MESSAGE_FOT_AUTO_ACAN,AUTO_SCAN_DURATION);
        }
    }
   public interface PreviewFrameShotListener {
        public void onPreviewFrame(byte[] data, int cameraWidth,int cameraHeight);
    }

}
