package net.hongzhang.status.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.hongzhang.baselibrary.takevideo.CameraManager;
import net.hongzhang.baselibrary.takevideo.CameraProgressBar;
import net.hongzhang.baselibrary.takevideo.CameraView;
import net.hongzhang.baselibrary.takevideo.MediaPlayerManager;
import net.hongzhang.baselibrary.util.FileUtils;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.status.R;
import net.hongzhang.status.widget.StatusPublishPopWindow;

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
public class CaptureVideoActivity extends Activity implements View.OnClickListener {
    private TextureView mTextureView;
    private ImageView ivCameraSwitch;
    private ImageView ivCancle;
    private CameraManager cameraManager;
    private RelativeLayout rl_capture;
    private RelativeLayout rl_save;
    private ImageView iv_save;
    private ImageView iv_unsave;
    /**
     * true代表视频录制,否则拍照
     */
    private boolean isSupportRecord = true;
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
     * 录制视频的时间,毫秒
     */
    private int recordSecond;
    /**
     * 带手势识别
     */
    private CameraView mCameraView;
    /**
     * 播放管理
     */
    private MediaPlayerManager playerManager;
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,//麦克风权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private MyEventListener myEventListener;
    private boolean hasVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_video);
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

        //如果已经有视频，使得长按事件取消
        hasVideo = getIntent().getBooleanExtra("hasVideo",false);
        G.initDisplaySize(this);
        myEventListener = new MyEventListener(this);
        cameraManager = CameraManager.getInstance(getApplication());
        cameraManager.setOrientaion(0);
        playerManager = MediaPlayerManager.getInstance(getApplication());
        cameraManager.setCameraType(isSupportRecord ? 1 : 0);
        final int max = MAX_RECORD_TIME / PLUSH_PROGRESS;
        mProgressBar.setMaxProgress(max);
        // mProgressBar.setLongScale(true);
        mProgressBar.setOnProgressTouchListener(new CameraProgressBar.OnProgressTouchListener() {
            @Override
            public void onClick(final CameraProgressBar progressBar) {
                G.showToast(getApplicationContext(),"拍摄时间太短！");
            }
            @Override
            public void onLongClick(CameraProgressBar progressBar) {
                if (!hasVideo){
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
            }
            @Override
            public void onZoom(boolean zoom) {
              //  cameraManager.handleZoom(zoom);
            }

            @Override
            public void onLongClickUp(CameraProgressBar progressBar) {
                if (!hasVideo){
                    isSupportRecord = false;
                    cameraManager.setCameraType(0);
                    stopRecorder(true);
                    if (progressSubscription != null) {
                        progressSubscription.unsubscribe();
                    }
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
              //  cameraManager.handleZoom(zoom);
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
            rl_save.setVisibility(View.VISIBLE );
            rl_capture.setVisibility(View.GONE);
            cameraManager.closeCamera();
            playerManager.playMedia(new Surface(mTextureView.getSurfaceTexture()), recorderPath);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开启屏幕旋转监听
        myEventListener.enable();
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
             Intent intent = new Intent(this, PublishStatusActivity.class);
            if(recorderPath != null) {
                intent.putExtra("type", StatusPublishPopWindow.VEDIO);
                intent.putExtra("groupId",getIntent().getStringExtra("groupId"));
                intent.putExtra("recorderPath", recorderPath);
                startActivity(intent);
            }
            finish();
        } else if (viewId == R.id.iv_unsave) {
            if (recorderPath != null) {
                playerManager.stopMedia();
                FileUtils.delteFiles(new File(recorderPath));
            }
            cameraManager.closeCamera();
            cameraManager.openCamera(mTextureView.getSurfaceTexture(),
                    mTextureView.getWidth(), mTextureView.getHeight());
            setTakeButtonShow(false);

        }
    }

    private void setTakeButtonShow(boolean isStop) {
        rl_save.setVisibility(isStop ? View.VISIBLE : View.GONE);
        rl_capture.setVisibility(isStop ? View.GONE : View.VISIBLE);
        mTextureView.setVisibility(isStop ? View.GONE : View.VISIBLE);
        ivCameraSwitch.setVisibility(isStop ? View.GONE : View.VISIBLE);
        mCameraView.setVisibility(isStop ? View.GONE : View.VISIBLE);
        mCameraView.removeOnZoomListener();
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
        //结束屏幕旋转监听
        myEventListener.disable();
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

    private int mOrientation = 0;

    private class MyEventListener extends OrientationEventListener {

        public MyEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            //只检测是否有四个角度的改变  
            if (orientation > 350 || orientation < 10) {
                mOrientation = 0;
            } else if (orientation > 80 && orientation < 100) {
                mOrientation = 90;
            } else if (orientation > 170 && orientation < 190) {
                mOrientation = 180;
            } else if (orientation > 260 && orientation < 280) {
                mOrientation = 270;
            }
            ivCameraSwitch.setRotation(-mOrientation);
        }
    }

}
