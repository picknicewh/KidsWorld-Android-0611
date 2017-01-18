package net.hunme.discovery.activity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import net.hunme.baselibrary.mode.ResourceVo;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.LoadingDialog;
import net.hunme.discovery.R;
import net.hunme.discovery.adapter.VideoAlbumAdapter;
import net.hunme.discovery.util.PlayVideoContract;
import net.hunme.discovery.util.PlayVideoPresenter;
import net.hunme.discovery.widget.ChooseAlbumPopWindow;
import net.hunme.discovery.widget.VerticalSeekBar;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FullPlayVideoActivity extends Activity implements View.OnClickListener, PlayVideoContract.View, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {
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
    private VerticalSeekBar vsb_light;
    /**
     * 声音调节进度条
     */
    private VerticalSeekBar vsb_volume;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_full_play_video);
        initView();
    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.sv_preview);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_name = (TextView) findViewById(R.id.tv_video_name);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        vsb_light = (VerticalSeekBar) findViewById(R.id.vsb_light);
        vsb_volume = (VerticalSeekBar) findViewById(R.id.vsb_volume);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        sb_video = (SeekBar) findViewById(R.id.sb_video);
        rv_video_album = (RecyclerView) findViewById(R.id.rv_video_album);
        rl_toolbar = (RelativeLayout) findViewById(R.id.rl_toolbar);
        ll_contorl = (LinearLayout) findViewById(R.id.ll_contorl);
        iv_theme = (ImageView) findViewById(R.id.iv_theme);
        iv_collect.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_theme.setOnClickListener(this);
        initData();

    }

    public void initData() {
        G.initDisplaySize(this);
        mDate = new Date();
        alubumId = getIntent().getStringExtra("albumId");
        resourceId = getIntent().getStringExtra("resourceId");
        presenter = new PlayVideoPresenter(this, surfaceView, this, alubumId, resourceId);
        presenter.setScreenDirection(this.getResources().getConfiguration().orientation, surfaceView);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        vsb_volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        vsb_volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        int normal = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
        vsb_light.setMax(255);
        vsb_light.setProgress(normal);
        vsb_light.setOnSeekBarChangeListener(this);
        vsb_volume.setOnSeekBarChangeListener(this);
        sb_video.setOnSeekBarChangeListener(this);
        surfaceView.setOnTouchListener(this);
        timerStart();
    }

    @Override
    protected void onResume() {
        if (resourceVos != null) {
            presenter = new PlayVideoPresenter(this, surfaceView, this, alubumId, resourceId);
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_play) {
            if (isPlaying) {
                presenter.pause();
            } else {
                presenter.play();
            }

        } else if (viewId == R.id.iv_collect) {
            if (cancel == 1) {
                cancel = 2;
                iv_collect.setImageResource(R.mipmap.ic_video_full_collect);
            } else {
                cancel = 1;
                iv_collect.setImageResource(R.mipmap.ic_video_full_collect_full);
            }
            presenter.subFavorate(resourceVos.get(position).getAlbumId(), cancel);
        } else if (viewId == R.id.iv_left) {
            presenter.savePlayTheRecord(UserMessage.getInstance(this).getTsId(), resourceId, currentPosition, 2);
            finish();
        } else if (viewId == R.id.iv_theme) {
            ChooseAlbumPopWindow albumPopWindow = new ChooseAlbumPopWindow(this);
            albumPopWindow.showAtLocation(view, Gravity.NO_GRAVITY, G.dp2px(this, 50), G.dp2px(this, 45));
        }
    }
   private  void  timerStart(){
       timer = new Timer();
       TimerTask timerTask = new TimerTask() {
           @Override
           public void run() {
               handler.sendEmptyMessage(0x01);
           }
       };
       timer.schedule(timerTask, mDate, 10000);
   }

    private void setInfoIsVisible(int isVisible) {
        rl_toolbar.setVisibility(isVisible);
        ll_contorl.setVisibility(isVisible);
        vsb_light.setVisibility(isVisible);
        vsb_volume.setVisibility(isVisible);
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
            dialog = new LoadingDialog(this, net.hunme.baselibrary.R.style.LoadingDialogTheme);
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
                Log.i("qq", "=======================222222=========================");
            }
        }
        if (seekBar.getId() == R.id.vsb_volume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//获取当前值  
            seekBar.setProgress(currentVolume);
            Log.i("qq", currentVolume + "==============currentVolume");
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
            Log.i("qq", tempFloat + "==============tempFloat");
        }
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

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mDate = new Date();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int y = (int) event.getY();
            int top = G.dp2px(this, 50);
            ll_contorl.measure(0,0);
            int bottom = G.size.H -  ll_contorl.getMeasuredHeight();
            if (y > top && y < bottom) {
                Log.i("sssss",top+"========================="+bottom);
                if (isclick) {
                    setInfoIsVisible(View.VISIBLE);
                    isclick = false;
                } else {
                    setInfoIsVisible(View.GONE);
                    isclick = true;
                }
            }
        }
        return false;
    }
}
