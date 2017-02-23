package net.hongzhang.discovery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.VideoAlbumAdapter;
import net.hongzhang.discovery.util.MyGestureListener;
import net.hongzhang.discovery.util.PlayVideoContract;
import net.hongzhang.discovery.util.PlayVideoPresenter;
import net.hongzhang.discovery.widget.ChooseAlbumPopWindow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FullPlayVideoActivity extends Activity implements View.OnClickListener, PlayVideoContract.View, SeekBar.OnSeekBarChangeListener {
    /**
     * 导航栏
     */
    private RelativeLayout rl_toolbar;
    /**
     * 音频装在控件
     */
    private SurfaceView surfaceView;
    /**
     * 左边结束
     */
    private ImageView iv_left;
    /**
     * 当前播放歌曲的名字
     */
    private TextView tv_name;
    /**
     * 收藏按钮
     */
    private ImageView iv_collect;
    /**
     * 亮度调节进度条
     */
    // private VerticalSeekBar vsb_light;
    /**
     * 声音调节进度条
     */
    //  private VerticalSeekBar vsb_volume;
    /**
     * 播放
     */
    private ImageView iv_play;
    /**
     * 进度条
     */
    private SeekBar sb_video;
    /**
     * 专辑列表控件
     */
    private RecyclerView rv_video_album;
    /**
     * 播放控制台
     */
    private LinearLayout ll_contorl;

    /**
     * 特定专辑
     */
    private ImageView iv_theme;
    /**
     * 是否播放
     */
    private boolean isPlaying;
    /**
     * 加载dialog
     */
    public LoadingDialog dialog;
    /**
     * 播放的位置
     */
    private int position;
    /**
     * 数据处理
     */
    private PlayVideoPresenter presenter;
    /**
     * 适配器
     */
    private VideoAlbumAdapter albumAdapter;
    /**
     * 计时器
     */
    private Timer timer;
    private TimerTask timerTask;
    /**
     * 日期
     */
    private Date mDate;
    /**
     * 声音管理
     */
    private AudioManager audioManager;
    /**
     * 资源列表
     */
    private List<ResourceVo> resourceVos;
    /**
     * 是否点击
     */
    boolean isclick = true;
    /**
     * 是否收藏
     */
    private int cancel;
    /**
     * 专辑id
     */
    private String alubumId;
    /**
     * 当前播放资源id
     */
    private String resourceId;
    /**
     * 当前时间
     */
    private int currentPosition;
    private MyEventListener listener;
    private RelativeLayout rl_full_srceen;
    private GestureDetector mGestureDetector;
    private MyGestureListener myGestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_full_play_video);
        Log.i("fffff", "===========onCreate=============");
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_full_srceen = (RelativeLayout) findViewById(R.id.rl_full_srceen);
        surfaceView = (SurfaceView) findViewById(R.id.sv_preview);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_name = (TextView) findViewById(R.id.tv_video_name);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        rl_full_srceen = (RelativeLayout) findViewById(R.id.rl_full_srceen);
        // vsb_light = (VerticalSeekBar) findViewById(R.id.vsb_light);
        // vsb_volume = (VerticalSeekBar) findViewById(R.id.vsb_volume);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        sb_video = (SeekBar) findViewById(R.id.sb_video);
        rv_video_album = (RecyclerView) findViewById(R.id.rv_video_album);
        rl_toolbar = (RelativeLayout) findViewById(R.id.rl_toolbar);
        ll_contorl = (LinearLayout) findViewById(R.id.ll_contorl);
        iv_theme = (ImageView) findViewById(R.id.iv_theme);
        iv_collect.setOnClickListener(this);
       // rl_full_srceen.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_theme.setOnClickListener(this);
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        G.initDisplaySize(this);
        mDate = new Date();
        alubumId = getIntent().getStringExtra("albumId");
        resourceId = getIntent().getStringExtra("resourceId");
        resourceVos = new ArrayList<>();
        listener = new MyEventListener(this);
        presenter = new PlayVideoPresenter(this, surfaceView, this, alubumId, resourceId);
        presenter.setScreenDirection(this.getResources().getConfiguration().orientation, surfaceView);
        //audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        myGestureListener = new MyGestureListener(this);
        mGestureDetector = new GestureDetector(this, myGestureListener);
        //  vsb_volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        // vsb_volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
       // int normal = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
        //  vsb_light.setMax(255);
        //  vsb_light.setProgress(normal);
        //  vsb_light.setOnSeekBarChangeListener(this);
        //vsb_volume.setOnSeekBarChangeListener(this);
        sb_video.setOnSeekBarChangeListener(this);
     //   surfaceView.setOnTouchListener(this);
    //    surfaceView.setOnClickListener(this);
        timerStart();


    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_play) {
            //播放/暂停
            if (isPlaying) {
                presenter.pause();
            } else {
                presenter.play();
            }
        } else if (viewId == R.id.iv_collect) {
            //收藏数据
            if (cancel == 1) {
                cancel = 2;
                iv_collect.setImageResource(R.mipmap.ic_video_full_collect);
            } else {
                cancel = 1;
                iv_collect.setImageResource(R.mipmap.ic_video_full_collect_full);
            }
            //取消或者点赞
            presenter.subFavorate(resourceVos.get(position).getAlbumId(), cancel);
        } else if (viewId == R.id.iv_left) {
            //保持播放记录
            presenter.savePlayTheRecord(UserMessage.getInstance(this).getTsId(), resourceId, currentPosition, 2);
            finish();
        } else if (viewId == R.id.iv_theme) {
            ChooseAlbumPopWindow albumPopWindow = new ChooseAlbumPopWindow(this);
            albumPopWindow.showAtLocation(view, Gravity.NO_GRAVITY, G.dp2px(this, 50), G.dp2px(this, 45));
        } else if (viewId == R.id.sv_preview) {
            mDate = new Date();
            if (isclick) {
                setInfoIsVisible(View.VISIBLE);
                isclick = false;
            } else {
                setInfoIsVisible(View.GONE);
                isclick = true;
            }
        }
    }

    /**
     * 启动计时器
     */
    private void timerStart() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x01);
            }
        };
        timer.schedule(timerTask, mDate, 10000);
    }

    /**
     * 设置视频播放信息是否可见
     *
     * @param isVisible 是否可见
     */
    private void setInfoIsVisible(int isVisible) {
        rl_toolbar.setVisibility(isVisible);
        ll_contorl.setVisibility(isVisible);
        //vsb_light.setVisibility(isVisible);
        //
        //
        //  vsb_volume.setVisibility(isVisible);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x01) {
                setInfoIsVisible(View.GONE);
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 设置视频列表
     *
     * @param resourceVos 视频列表
     */
    @Override
    public void setVideoList(List<ResourceVo> resourceVos) {
        this.resourceVos = resourceVos;
        cancel = resourceVos.get(position).getIsFavorites();
        albumAdapter = new VideoAlbumAdapter(this, resourceVos);
        rv_video_album.setAdapter(albumAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_video_album.setLayoutManager(manager);
        albumAdapter.setOnItemClickListener(new VideoAlbumAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (presenter.getPrepared()) {
                    presenter.setPosition(position);
                }
                if (albumAdapter != null) {
                    albumAdapter.setCurrentPosition(position);
                }
            }
        });
    }

    /**
     * 设置视频信息
     *
     * @param resourceVo 当前播放视频信息
     * @param position   当前播放的列表播放的位置
     */
    @Override
    public void setvideoInfo(ResourceVo resourceVo, int position) {
        resourceId = resourceVo.getResourceId();
        this.position = position;
        if (albumAdapter != null) {
            albumAdapter.setCurrentPosition(position);
        }
        tv_name.setText(resourceVo.getAlbumName() + "  第" + (position + 1) + "集");
        if (resourceVo.getIsFavorites() == 1) {
            iv_collect.setImageResource(R.mipmap.ic_video_full_collect_full);
        } else {
            iv_collect.setImageResource(R.mipmap.ic_video_full_collect);
        }
    }

    /**
     * 设置播放状态
     *
     * @param isPlay 是否播放
     */
    @Override
    public void setIsPlay(boolean isPlay) {
        isPlaying = isPlay;
        if (isPlay) {
            iv_play.setImageResource(R.mipmap.ic_video_full_play);
        } else {
            iv_play.setImageResource(R.mipmap.ic_video_full_pause);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开启屏幕旋转监听
        listener.enable();
        Log.i("fffff", "===========onResume=============");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("fffff", "===========onStart=============");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //结束屏幕旋转监听
        listener.disable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.pause();
        Log.i("fffff", "===========onStop=============");
    }

    @Override
    protected void onRestart() {
        Log.i("fffff", "===========onRestart=============");
        super.onRestart();
        presenter.play();
        presenter.setActivityRestart(true);
    }

    @Override
    public void setCurrent(int progress) {
        currentPosition = progress;
        sb_video.setProgress(progress);
    }

    @Override
    public void setDuration(int max) {
        sb_video.setMax(max);
    }

    @Override
    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new LoadingDialog(this, net.hongzhang.baselibrary.R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }

    @Override
    public void stopLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("fffff", "===========onDestroy=============");
        presenter.destroy();
        presenter.savePlayTheRecord(UserMessage.getInstance(this).getTsId(), resourceId, currentPosition, 2);
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if (b) {
            if (seekBar.getId() == R.id.sb_video) {
                presenter.changeSeeBar(progress);
                //   Log.i("qq", "=======================222222=========================");
            }
        }
    /*    if (seekBar.getId() == R.id.vsb_volume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//获取当前值  
            seekBar.setProgress(currentVolume);
            // Log.i("qq", currentVolume + "==============currentVolume");
        } else if (seekBar.getId() == R.id.vsb_light) {
            int tempInt = progress;
            // 当进度小于80时，设置成80，防止太黑看不见的后果
            if (tempInt < 80) {
                tempInt = 80;
            }
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, tempInt);
            tempInt = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, -1);
            WindowManager.LayoutParams wl = getWindow().getAttributes();
            float tempFloat = (float) tempInt / 255;
            if (tempFloat > 0 && tempFloat <= 1) {
                wl.screenBrightness = tempFloat;
            }
            getWindow().setAttributes(wl);
            //  Log.i("qq", tempFloat + "==============tempFloat");
        }*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                myGestureListener.endGesture();
                break;
        }
        mDate = new Date();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int y = (int) event.getY();
            int top = G.dp2px(this, 50);
            ll_contorl.measure(0, 0);
            int bottom = G.size.H - ll_contorl.getMeasuredHeight();
            if (y > top && y < bottom) {
                if (isclick) {
                    setInfoIsVisible(View.VISIBLE);
                    isclick = false;
                } else {
                    setInfoIsVisible(View.GONE);
                    isclick = true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar.equals(sb_video)) {
            presenter.pause();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.equals(sb_video)) {
            presenter.play();
        }
    }

    private class MyEventListener extends OrientationEventListener {

        public MyEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            //屏幕旋转角度，如果0-180度正面如果大于180度就方面
            if (orientation >= 0 && orientation <= 180) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            } else if (orientation > 180 && orientation < 360) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }
}
