package net.hunme.discovery.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import net.hunme.baselibrary.util.G;
import net.hunme.discovery.R;

import org.apache.cordova.LOG;

import java.util.Timer;
import java.util.TimerTask;

public class PlayVideoActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {
    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private Uri uri;
    private RelativeLayout rl_media;
    private ImageView iv_srceen;
    private ImageView iv_pre;
    private ImageView iv_play;
    private ImageView iv_next;
    private ImageView iv_left;
    private RelativeLayout rl_toolbar;
    private SeekBar seekBar;
    private TextView tv_current;
    private TextView tv_duration;
    private  int vWidth;
    private    int vHeight;
    private    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        G.initDisplaySize(this);
        G.setTranslucent(this);
        initSurfView();
    }
    private void initSurfView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        rl_media = (RelativeLayout) findViewById(R.id.rl_media);
        rl_toolbar= (RelativeLayout) findViewById(R.id.rl_toolbar);
        iv_srceen = (ImageView) findViewById(R.id.iv_screen);
        iv_pre = (ImageView) findViewById(R.id.iv_pre);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_left= (ImageView) findViewById(R.id.iv_left);
        seekBar = (SeekBar)findViewById(R.id.sb_video);
        tv_current = (TextView)findViewById(R.id.tv_current);
        tv_duration = (TextView)findViewById(R.id.tv_duration);
        iv_srceen.setOnClickListener(this);
        iv_pre.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        seekBar.setMax(100);
        setSurfaceViewparam();
    }
    private void setSurfaceViewparam(){
        player = new MediaPlayer();
        holder = surfaceView.getHolder();
        uri = Uri.parse("http://zhu.hunme.net:8080/resource/resourceManage/Video/Muffin%20Songs%20X6/01.Animal%20Songs.mp4");
        holder.addCallback(this);
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(100);
        // 当prepare完成后，该方法触发，在这里我们播放视频    
        vWidth = G.size.W;
        vHeight =  ( G.size.W*9/16);
        //设置surfaceView的布局参数    
        rl_media.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(vWidth, vHeight));
        setCurrenTime();
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (player != null) {
            try {
                player.reset();
                player.setDataSource(this, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            player.setDisplay(holder);
            player.prepareAsync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        player.release();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //下一首
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            seekBar.setProgress(0);
            tv_duration.setText(G.toTime(mediaPlayer.getDuration()));
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
       if (viewId==R.id.iv_screen){
         //设置横屏模式
         setScreen(this.getResources().getConfiguration().orientation);
       }else if (viewId==R.id.iv_left){
           finish();
       }else if (viewId==R.id.iv_play){
           if (player!=null){
               if (player.isPlaying()){
                   iv_play.setImageResource(R.mipmap.ic_play);
                    player.pause();
               }else {
                   iv_play.setImageResource(R.mipmap.ic_pause);
                   player.start();
               }
           }
       }
    }
    private void setScreen(int orientation){
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        Window window = getWindow();
        //设置横屏模式
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            rl_toolbar.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            rl_media.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            attr.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(attr);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            // 横屏时
            rl_toolbar.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            rl_media.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
            surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(vWidth, vHeight));
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(attr);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            G.setTranslucent(this);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (event.getAction()==KeyEvent.ACTION_DOWN){
           if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
               setScreen(this.getResources().getConfiguration().orientation);
           }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (player!=null){
            float progress = i *((float)player.getDuration()/100);
            player.seekTo((int) progress);
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (player!=null){
          player.pause();
        }
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (player!=null){
           player.start();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0x01){
                float currentPosition = 100*(float)player.getCurrentPosition()/(float) player.getDuration();
                LOG.i("sssssssssssss",player.getCurrentPosition()+"=========");
                seekBar.setProgress((int) currentPosition);
                tv_current.setText(G.toTime(player.getCurrentPosition()));
            }
           // handler.sendEmptyMessageDelayed(0x01,1000);
        }
    };
    /**
     * 定时的获取当前的时间
     */
    private void setCurrenTime() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x01);
            }
        };
        timer.schedule(timerTask,0,10);
    }

    @Override
    protected void onDestroy() {
        if (handler!=null){
            handler.removeMessages(0x01);
        }
        super.onDestroy();

    }
    //    private void init() {
//        Uri uri = Uri.parse("http://zhu.hunme.net:8080/resource/resourceManage/Video/Muffin%20Songs%20X6/01.Animal%20Songs.mp4");
//        //调用系统自带的播放器    
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(uri, "video/mp4");
//        startActivity(intent);
//    }
}
