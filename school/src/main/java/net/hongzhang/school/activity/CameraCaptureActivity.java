package net.hongzhang.school.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.school.R;
import net.hongzhang.school.util.CameraManager;
import net.hongzhang.school.util.CameraView;
import net.hongzhang.baselibrary.util.FileUtils;
import net.hongzhang.school.util.MediaPlayerManager;
import net.hongzhang.school.widget.CameraProgressBar;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 作者： wanghua
 * 时间： 2017/4/21
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CameraCaptureActivity extends Activity implements View.OnClickListener {
    /**
     * 获取相册
     */
    public static final int REQUEST_PHOTO = 1;
    /**
     * 获取视频
     */
    public static final int REQUEST_VIDEO = 2;
    private TextureView mTextureView;
    private ImageView ivCameraSwitch;
    private ImageView ivCancle;
    private CameraManager cameraManager;
    //  private ImageView iv_capture;
    private RelativeLayout rl_capture;
    private RelativeLayout rl_save;
    private ImageView iv_save;
    private ImageView iv_unsave;
    /**
     * true代表视频录制,否则拍照
     */
    private boolean isSupportRecord = false;
    private CameraProgressBar mProgressBar;
    private boolean isRecording;
    /**
     * 获取照片订阅, 进度订阅
     */
    private Subscription progressSubscription;
    /**
     * 最小录制时间
     */
    private static final int MIN_RECORD_TIME = 1 * 1000;
    /**
     * 最长录制时间
     */
    private static final int MAX_RECORD_TIME = 15 * 1000;
    /**
     * 刷新进度的间隔时间
     */
    private static final int PLUSH_PROGRESS = 100;
    /**
     * 视频录制地址
     */

    private String recorderPath;
    /**
     * 图片地址
     */
    private String photoPath;
    /**
     * 录制视频的时间,毫秒
     */
    private int recordSecond;
    /**
     * 是否为点了拍摄状态(没有拍照预览的状态)
     */
    private boolean isPhotoTakingState;
    /**
     * 带手势识别
     */
    private CameraView mCameraView;
    /**
     * 播放管理
     */
    private MediaPlayerManager playerManager;
    private byte[] photodata;
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,//麦克风权限
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.DISABLE_KEYGUARD,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);
        ButterKnife.bind(this);
        PermissionsChecker.getInstance(this).getPerMissions(PERMISSIONS);
        initView();
    }

    private void initView() {
        mTextureView = (TextureView) findViewById(R.id.tv_preview);
        ivCameraSwitch = (ImageView) findViewById(R.id.iv_camera_switch);
        ivCancle = (ImageView) findViewById(R.id.iv_cancle);
        rl_capture = (RelativeLayout) findViewById(R.id.rl_capture);
        rl_save = (RelativeLayout) findViewById(R.id.rl_save);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        iv_unsave = (ImageView) findViewById(R.id.iv_unsave);
        mProgressBar = (CameraProgressBar) findViewById(R.id.camera_progress_bar);
        mCameraView = (CameraView) findViewById(R.id.camera_view);
        ivCameraSwitch.setOnClickListener(this);
        ivCancle.setOnClickListener(this);
        iv_save.setOnClickListener(this);
        iv_unsave.setOnClickListener(this);
        initData();
    }


    private void initData() {
        G.initDisplaySize(this);
        cameraManager = CameraManager.getInstance(getApplication());
        playerManager = MediaPlayerManager.getInstance(getApplication());
        cameraManager.setCameraType(isSupportRecord ? 1 : 0);
        final int max = MAX_RECORD_TIME / PLUSH_PROGRESS;
        mProgressBar.setMaxProgress(max);
        mProgressBar.setLongScale(true);
        mProgressBar.setOnProgressTouchListener(new CameraProgressBar.OnProgressTouchListener() {
            @Override
            public void onClick(CameraProgressBar progressBar) {
                cameraManager.takePhoto(new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        setTakeButtonShow(true);
                        photodata = data;
                    }
                });
                isSupportRecord = false;
            }
            @Override
            public void onLongClick(CameraProgressBar progressBar) {
                isSupportRecord = true;
                cameraManager.setCameraType(1);
                recorderPath = FileUtils.getUploadVideoFile(getApplicationContext());
                cameraManager.startMediaRecord(recorderPath);
                isRecording = true;
                progressSubscription = Observable.interval(100, TimeUnit.MILLISECONDS,
                        AndroidSchedulers.mainThread()).take(max).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        stopRecorder(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mProgressBar.setProgress(mProgressBar.getProgress() + 1);
                    }
                });
            }

            @Override
            public void onZoom(boolean zoom) {
                cameraManager.handleZoom(zoom);
            }

            @Override
            public void onLongClickUp(CameraProgressBar progressBar) {
                isSupportRecord = false;
                cameraManager.setCameraType(0);
                stopRecorder(true);
                if (progressSubscription != null) {
                    progressSubscription.unsubscribe();
                }
            }

            @Override
            public void onPointerDown(float rawX, float rawY) {
                if (mTextureView != null) {
                    mCameraView.setFoucsPoint(new PointF(rawX, rawY));
                }
            }
        });
        //点击预览图聚焦
        mCameraView.setOnViewTouchListener(new CameraView.OnViewTouchListener() {
            @Override
            public void handleFocus(float x, float y) {
                cameraManager.handleFocusMetering(x, y);
            }

            @Override
            public void handleZoom(boolean zoom) {
                cameraManager.handleZoom(zoom);
            }
        });
    }

    /**
     * 停止拍摄
     *
     * @param play
     */
    private void stopRecorder(boolean play) {
        isRecording = false;
        cameraManager.stopMediaRecord();
        recordSecond = mProgressBar.getProgress() * PLUSH_PROGRESS;//录制多少毫秒
        mProgressBar.reset();
        if (recordSecond < MIN_RECORD_TIME) {//小于最小录制时间作废
            if (recorderPath != null) {
                FileUtils.delteFiles(new File(recorderPath));
                recorderPath = null;
                recordSecond = 0;
            }
        } else if (play && mTextureView != null && mTextureView.isAvailable()) {
            setTakeButtonShow(true);
            cameraManager.closeCamera();
            playerManager.playMedia(new Surface(mTextureView.getSurfaceTexture()), recorderPath);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()) {
            if (recorderPath != null) {//优先播放视频
                playerManager.playMedia(new Surface(mTextureView.getSurfaceTexture()), recorderPath);
            } else {
                cameraManager.openCamera(mTextureView.getSurfaceTexture(),
                        mTextureView.getWidth(), mTextureView.getHeight());
            }
        } else {
            mTextureView.setSurfaceTextureListener(listener);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_cancle) {
            finish();
        } else if (viewId == R.id.iv_camera_switch) {
            cameraManager.changeCameraFacing(mTextureView.getSurfaceTexture(),
                    mTextureView.getWidth(), mTextureView.getHeight());
        } else if (viewId == R.id.iv_save) {
            Intent intent = new Intent(this, PublishActiveSActivity.class);
            if (photodata != null) {
                //保存照片
                photoPath = FileUtils.getUploadPhotoFile(getApplicationContext());
                isPhotoTakingState = FileUtils.savePhoto(photoPath, photodata, cameraManager.isCameraFrontFacing());
                intent.putExtra("photoPath", photoPath);
                setResult(REQUEST_PHOTO, intent);
            } else if (recorderPath != null) {
                intent.putExtra("recorderPath", recorderPath);
                setResult(REQUEST_VIDEO, intent);

            }
            finish();
        } else if (viewId == R.id.iv_unsave) {
            if (recorderPath != null) {
                playerManager.stopMedia();
                FileUtils.delteFiles(new File(recorderPath));
                cameraManager.closeCamera();
                cameraManager.openCamera(mTextureView.getSurfaceTexture(),
                        mTextureView.getWidth(), mTextureView.getHeight());
            }
            setTakeButtonShow(false);
            cameraManager.restartPreview();
        }
    }

    private void setTakeButtonShow(boolean isStop) {
        rl_capture.setVisibility(isStop ? View.GONE : View.VISIBLE);
        rl_save.setVisibility(isStop ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        cameraManager.closeCamera();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//当前为横屏  
            cameraManager.openCameraH(mTextureView.getSurfaceTexture(),
                    mTextureView.getWidth(), mTextureView.getHeight());
            G.log("vvvvvvvvv"+"横向拍照");
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//当前为竖屏  
            cameraManager.openCamera(mTextureView.getSurfaceTexture(),
                    mTextureView.getWidth(), mTextureView.getHeight());
            G.log("vvvvvvvvv"+"竖向拍照");
        }

        super.onConfigurationChanged(newConfig);
    }


    /**
     * camera回调监听
     */
    private TextureView.SurfaceTextureListener listener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            if (recorderPath != null) {
                playerManager.playMedia(new Surface(texture), recorderPath);
            } else {
                cameraManager.openCamera(texture, width, height);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }
    };

    @Override
    protected void onPause() {
        G.log("---------------------" + "onPause");
        cameraManager.closeCamera();
        playerManager.stopMedia();
        if (progressSubscription != null) {
            progressSubscription.unsubscribe();
        }
        if (isRecording) {
            stopRecorder(false);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraView.removeOnZoomListener();
    }
}
