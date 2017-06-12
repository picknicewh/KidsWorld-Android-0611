package net.hongzhang.baselibrary.takevideo;

import android.app.Application;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.widget.Toast;

import net.hongzhang.baselibrary.util.G;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 相机管理类
 */

public final class CameraManager {

    private Application context;
    /**
     * camera
     */
    private Camera mCamera;
    private Camera.Parameters mParameters;
    /**
     * 视频录制
     */
    private MediaRecorder mMediaRecorder;
    /**
     * 相机闪光状态
     */
    private int cameraFlash;
    /**
     * 前后置状态
     */
    private int cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
    /**
     * 是否支持前置摄像,是否支持闪光
     */
    private boolean isSupportFrontCamera, isSupportFlashCamera;
    /**
     * 录制视频的相关参数
     */
    private CamcorderProfile mProfile;
    /**
     * 0为拍照, 1为录像
     */
    private int cameraType;

    private CameraManager(Application context) {
        this.context = context;
        isSupportFrontCamera = CameraUtils.isSupportFrontCamera();
        isSupportFlashCamera = CameraUtils.isSupportFlashCamera(context);
        if (isSupportFrontCamera) {
            cameraFacing = CameraUtils.getCameraFacing(context, Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        if (isSupportFlashCamera) {
            cameraFlash = CameraUtils.getCameraFlash(context);
        }
    }

    private static CameraManager INSTANCE;

    public static CameraManager getInstance(Application context) {
        if (INSTANCE == null) {
            synchronized (CameraManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CameraManager(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 打开camera
     */
    public void openCamera(SurfaceTexture surfaceTexture, int width, int height) {
        if (mCamera == null) {
            mCamera = Camera.open(cameraFacing);//打开当前选中的摄像头
            mProfile = CamcorderProfile.get(cameraFacing, CamcorderProfile.QUALITY_HIGH);
            try {
                mCamera.setDisplayOrientation(90);//默认竖直拍照
                mCamera.setPreviewTexture(surfaceTexture);
                initCameraParameters(cameraFacing, width, height);
                mCamera.startPreview();
            } catch (Exception e) {
                G.log(e);
                if (mCamera != null) {
                    mCamera.release();
                    mCamera = null;
                }
            }
        }
    }

    /**
     * 开启预览,前提是camera初始化了
     */
    public void restartPreview() {
        if (mCamera == null) return;
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            int zoom = parameters.getZoom();
            if (zoom > 0) {
                parameters.setZoom(0);
                mCamera.setParameters(parameters);
            }
            mCamera.startPreview();
        } catch (Exception e) {
            G.log(e);
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
    }


    private void initCameraParameters(int cameraId, int width, int height) {
        final Camera.Parameters parameters = mCamera.getParameters();

        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes != null) {
                if (cameraType == 0) {
                    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    }
                } else {
                    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    }
                }
            }
        }
        parameters.setRotation(90);//设置旋转代码,
       /* setOrientationChanged(new OrientationChanged() {
            @Override
            public void onChanged(int orientation) {
                G.log("xcxcxcxcx"+orientation);
                parameters.setRotation(orientation + 90);//设置旋转代码,
            }
        });*/
        switch (cameraFlash) {
            case 0:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case 1:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                break;
            case 2:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
        }
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        if (!isEmpty(pictureSizes) && !isEmpty(previewSizes)) {
            Camera.Size optimalPicSize = getOptimalCameraSize(previewSizes, width, height);
            Camera.Size optimalPreSize = getOptimalCameraSize(previewSizes, width, height);
            G.log("TextureSize " + width + " " + height + " optimalSize pic " + optimalPicSize.width + " " + optimalPicSize.height + " pre " + optimalPreSize.width + " " + optimalPreSize.height);
            parameters.setPictureSize(optimalPicSize.width, optimalPicSize.height);

            parameters.setPreviewSize(optimalPreSize.width, optimalPreSize.height);
            mProfile.videoFrameWidth = optimalPreSize.width;
            mProfile.videoFrameHeight = optimalPreSize.height;
            mProfile.videoBitRate = 5000000;//此参数主要决定视频拍出大小
        }

        mCamera.setParameters(parameters);
    }

    /**
     * 释放摄像头
     */
    public void closeCamera() {
        this.cameraType = 0;
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                G.log(e);
                if (mCamera != null) {
                    mCamera.release();
                    mCamera = null;
                }
            }
        }
    }

    public void setOrientaion(int orientaion) {
        if (orientationChanged != null) {
            G.log("---xxxx-----" + orientaion);
            orientationChanged.onChanged(orientaion % 180);
        }
    }

    private OrientationChanged orientationChanged;

    public void setOrientationChanged(OrientationChanged orientationChanged) {
        this.orientationChanged = orientationChanged;
    }

    public interface OrientationChanged {
        void onChanged(int orientation);
    }

    /**
     * 集合不为空
     *
     * @param list
     * @param <E>
     * @return
     */
    private <E> boolean isEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }

    /**
     * @param sizes 相机support参数
     * @param w
     * @param h
     * @return 最佳Camera size
     */
    private Camera.Size getOptimalCameraSize(List<Camera.Size> sizes, int w, int h) {
        sortCameraSize(sizes);
        int position = binarySearch(sizes, w * h);
        return sizes.get(position);
    }

    /**
     * @param sizes
     * @param targetNum 要比较的数
     * @return
     */
    private int binarySearch(List<Camera.Size> sizes, int targetNum) {
        int targetIndex;
        int left = 0, right;
        int length = sizes.size();
        for (right = length - 1; left != right; ) {
            int midIndex = (right + left) / 2;
            int mid = right - left;
            Camera.Size size = sizes.get(midIndex);
            int midValue = size.width * size.height;
            if (targetNum == midValue) {
                return midIndex;
            }
            if (targetNum > midValue) {
                left = midIndex;
            } else {
                right = midIndex;
            }

            if (mid <= 1) {
                break;
            }
        }
        Camera.Size rightSize = sizes.get(right);
        Camera.Size leftSize = sizes.get(left);
        int rightNum = rightSize.width * rightSize.height;
        int leftNum = leftSize.width * leftSize.height;
        targetIndex = Math.abs((rightNum - leftNum) / 2) > Math.abs(rightNum - targetNum) ? right : left;
        return targetIndex;
    }

    /**
     * 排序
     *
     * @param previewSizes
     */
    private void sortCameraSize(List<Camera.Size> previewSizes) {
        Collections.sort(previewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size size1, Camera.Size size2) {
                int compareHeight = size1.height - size2.height;
                if (compareHeight == 0) {
                    return (size1.width == size2.width ? 0 : (size1.width > size2.width ? 1 : -1));
                }
                return compareHeight;
            }
        });
    }


    /**
     * 获取最佳预览相机Size参数
     *
     * @return
     */
    private Camera.Size getOptimalSize(List<Camera.Size> sizes, int w, int h) {
        Camera.Size optimalSize = null;
        float targetRadio = h / (float) w;
        float optimalDif = Float.MAX_VALUE; //最匹配的比例
        int optimalMaxDif = Integer.MAX_VALUE;//最优的最大值差距
        for (Camera.Size size : sizes) {
            float newOptimal = size.width / (float) size.height;
            float newDiff = Math.abs(newOptimal - targetRadio);
            if (newDiff < optimalDif) { //更好的尺寸
                optimalDif = newDiff;
                optimalSize = size;
                optimalMaxDif = Math.abs(h - size.width);
            } else if (newDiff == optimalDif) {//更好的尺寸
                int newOptimalMaxDif = Math.abs(h - size.width);
                if (newOptimalMaxDif < optimalMaxDif) {
                    optimalDif = newDiff;
                    optimalSize = size;
                    optimalMaxDif = newOptimalMaxDif;
                }
            }
        }
        return optimalSize;
    }

    /**
     * 缩放
     *
     * @param isZoomIn
     */
    public void handleZoom(boolean isZoomIn) {
        if (mCamera == null) return;
        Camera.Parameters params = mCamera.getParameters();
        if (params == null) return;
        if (params.isZoomSupported()) {
            int maxZoom = params.getMaxZoom();
            int zoom = params.getZoom();
            if (isZoomIn && zoom < maxZoom) {
                zoom++;
            } else if (zoom > 0) {
                zoom--;
            }
            params.setZoom(zoom);
            mCamera.setParameters(params);
        } else {
            G.log("zoom not supported");
        }
    }

    /**
     * 更换前后置摄像
     */
    public void changeCameraFacing(SurfaceTexture surfaceTexture, int width, int height) {
        if (isSupportFrontCamera) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
            for (int i = 0; i < cameraCount; i++) {
                Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
                if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) { //现在是后置，变更为前置
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位为前置
                        closeCamera();
                        cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
                        CameraUtils.setCameraFacing(context, cameraFacing);
                        openCamera(surfaceTexture, width, height);
                        break;
                    }
                } else {//现在是前置， 变更为后置
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位
                        closeCamera();
                        cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        CameraUtils.setCameraFacing(context, cameraFacing);
                        openCamera(surfaceTexture, width, height);
                        break;
                    }
                }
            }
        } else { //不支持摄像机
            Toast.makeText(context, "您的手机不支持前置摄像", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 改变闪光状态
     */
    public void changeCameraFlash(SurfaceTexture surfaceTexture, int width, int height) {
        if (!isSupportFlashCamera) {
            Toast.makeText(context, "您的手机不支闪光", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters != null) {
                int newState = cameraFlash;
                switch (cameraFlash) {
                    case 0: //自动
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        newState = 1;
                        break;
                    case 1://open
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        newState = 2;
                        break;
                    case 2: //close
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                        newState = 0;
                        break;
                }
                cameraFlash = newState;
                CameraUtils.setCameraFlash(context, newState);
                mCamera.setParameters(parameters);
            }
        }
    }

    /**
     * 拍照
     */
    public void takePhoto(Camera.PictureCallback callback) {
        if (mCamera != null) {
            try {
                mCamera.takePicture(null, null, callback);
            } catch (Exception e) {
                Toast.makeText(context, "拍摄失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 开始录制视频
     */
    public void startMediaRecord(String savePath) {
        if (mCamera == null) {
            return;
        }
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        } else {
            mMediaRecorder.reset();
        }


        if (isCameraFrontFacing()) {
            mMediaRecorder.setOrientationHint(270);

        } else {
            mMediaRecorder.setOrientationHint(90);
        }
        mParameters = mCamera.getParameters();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置录像参数
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
        try {
            mMediaRecorder.setOutputFile(savePath);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {

        }
    }

    /**
     * 停止录制
     */
    public void stopMediaRecord() {
        this.cameraType = 0;
        stopRecorder();
        releaseMediaRecorder();
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                mCamera.lock();
            } catch (Exception e) {
                e.printStackTrace();
                G.log(e);
            }
        }
    }

    private void stopRecorder() {
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
                G.log(e);
            }

        }
    }

    public boolean isSupportFrontCamera() {
        return isSupportFrontCamera;
    }

    public boolean isSupportFlashCamera() {
        return isSupportFlashCamera;
    }

    public boolean isCameraFrontFacing() {
        return cameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    /**
     * 设置对焦类型
     *
     * @param cameraType
     */
    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
        if (mCamera != null) {//拍摄视频时
            if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Camera.Parameters parameters = mCamera.getParameters();
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    if (cameraType == 0) {
                        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        }
                    } else {
                        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                        }
                    }
                }
            }
        }
    }

    public int getCameraFlash() {
        return cameraFlash;
    }

    /**
     * 对焦
     *
     * @param x
     * @param y
     */
    public void handleFocusMetering(float x, float y) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            Camera.Size previewSize = params.getPreviewSize();
            Rect focusRect = calculateTapArea(x, y, 1f, previewSize);
            Rect meteringRect = calculateTapArea(x, y, 1.5f, previewSize);
            mCamera.cancelAutoFocus();

            if (params.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> focusAreas = new ArrayList<>();
                focusAreas.add(new Camera.Area(focusRect, 1000));
                params.setFocusAreas(focusAreas);
            } else {
                G.log("focus areas not supported");
            }
            if (params.getMaxNumMeteringAreas() > 0) {
                List<Camera.Area> meteringAreas = new ArrayList<>();
                meteringAreas.add(new Camera.Area(meteringRect, 1000));
                params.setMeteringAreas(meteringAreas);
            } else {
                G.log("metering areas not supported");
            }
            final String currentFocusMode = params.getFocusMode();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(params);
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    Camera.Parameters params = camera.getParameters();
                    params.setFocusMode(currentFocusMode);
                    camera.setParameters(params);
                }
            });
        }

    }

    private Rect calculateTapArea(float x, float y, float coefficient, Camera.Size previewSize) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / previewSize.width - 1000);
        int centerY = (int) (y / previewSize.height - 1000);
        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }


}