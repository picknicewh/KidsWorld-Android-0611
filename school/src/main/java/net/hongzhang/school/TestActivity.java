package net.hongzhang.school;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.school.widget.CustomVideoView;

import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {

    CustomVideoView videoView ;
    private LoadingDialog dialog;
    private Timer timer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        videoView = (CustomVideoView)this.findViewById(R.id.rtsp_player);
        videoView.setVideoURI(Uri.parse("rtsp://admin:hengmei001@zhu.hunme.net:554/h264/ch1/sub/av_stream"));
        videoView.requestFocus();
        dialog=new LoadingDialog(this,R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setLoadingText("正在加载资源文件...");
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
        timer=new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(videoView.getCurrentPosition()>0){
                    dialog.dismiss();
                    timer.cancel();
                }
            }
        };
        timer.schedule(mTimerTask, 0, 100);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN ) {
            if(videoView.isPlaying()){
                videoView.stopPlayback();
                if(dialog!=null){
                    dialog.cancel();
                }
                timer.cancel();
                finish();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        timer=new Timer();
        dialog.show();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(videoView.getCurrentPosition()>0){
                    dialog.dismiss();
                    timer.cancel();
                }
            }
        };
        timer.schedule(mTimerTask, 0, 100);
    }
}
