package net.hongzhang.discovery.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.discovery.util.Constants;
import net.hongzhang.discovery.util.PlayMode;
import net.hongzhang.discovery.util.TextUtil;

import java.util.ArrayList;

public class MusicService extends Service implements OnPreparedListener,
        OnCompletionListener {
    private String TAG = "MusicService";
    public static MediaPlayer player;
    private MyBroadcastReceiver receiver;
    private static ArrayList<ResourceVo> resourceVos;
    private int position;
    public  boolean isFirst = true;
    public static int current;
    private static int duration = 0;
    public  boolean isPlaying;
    public boolean isPrepare = false;
    private int songSize;
    private boolean isCompleted=false;
    private Handler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        regFilter();
    }
    /**
     * 返回当前的current
     *
     * @return
     */
    public static int getCurrent() {
        current = player.getCurrentPosition();
        return current;
    }

    /**
     * 返回总共的长度
     *
     * @return
     */
    public static int getDuration() {
        duration = player.getDuration();
        return duration;
    }

    /*
     * 注册广播
     */
    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CLEAN_NOTIFICATION);
        filter.addAction(Constants.ACTION_PAUSE);
        filter.addAction(Constants.ACTION_PLAY);
        filter.addAction(Constants.ACTION_NEXT);
        filter.addAction(Constants.ACTION_PRV);
        filter.addAction(Constants.ACTION_POSITION);
        filter.addAction(Constants.ACTION_CALLING);
        filter.addAction(Constants.ACTION_PROCRESSBAR);
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        filter.setPriority(1000);
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, filter); // 注册接收
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver); // 服务终止时解绑
        }
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        if (handler != null) {
            handler.removeMessages(1);
            handler = null;
        }
        Log.i("TTTTTTTTTT","================onDestroy==========");

        super.onDestroy();
    }
    // api2.0以后采用onStartCommand
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player = new MediaPlayer();
        if (intent!=null){
            resourceVos = intent.getParcelableArrayListExtra("musicInfoVos");
            songSize = resourceVos.size();
            if (!player.isPlaying()) {
                if (isFirst) {
                    position = intent.getIntExtra("position", 0);
                    Log.i("position", "position:============service" + position);
                    prepareMusic(position);
                } else {
                    player.seekTo(current);
                    player.start();
                }
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }
    /*
     * 创建自定义的广播接收器
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            isCompleted = false;
            if (action.equals(Constants.ACTION_PAUSE)) {
                isPlaying = false;
                if (player.isPlaying()) {
                    pauseMusic();
                }
            }
            else if (action.equals(Constants.ACTION_PLAY)) {
                Log.i("TTTTTTTTTT",isPrepare+"================isPrepare==========startMusic");
                if (!player.isPlaying() && isPrepare) {
                    isPlaying = true;
                    startMusic();
                }
            } else if (action.equals(Constants.ACTION_NEXT)) {
                stopMusic();
                isPlaying = true;
                position++;
                if (position > songSize - 1) {
                    position = 0;
                }
                sendPosition(position,isCompleted);
                prepareMusic(position);
            } else if (action.equals(Constants.ACTION_PRV)) {
                stopMusic();
                isPlaying = true;
                position--;
                if (position < 0) {
                    position = songSize - 1;
                }
                prepareMusic(position);
                sendPosition(position,isCompleted);
            } else if (action.equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                isPlaying = false;
                //如果耳机拨出时暂停播放
                if (intent.getIntExtra("state", 0) == 0) {
                    pauseMusic();
                }
            } else if (intent.getAction().equals(Constants.ACTION_POSITION)) {
                isFirst = false;
                if (isPlaying){
                    stopMusic();
                }
                isPlaying=true;
                position = intent.getIntExtra("position", 0);
                prepareMusic(position);
                sendPosition(position,isCompleted);
            } else if (action.equals(Constants.ACTION_CALLING)) {
                TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                switch (tManager.getCallState()) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        isPlaying = false;
                        pauseMusic();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        isPlaying = true;
                        startMusic();
                        break;
                }
            }else if (action.equals(Constants.ACTION_PROCRESSBAR)){
                int progress = intent.getIntExtra("progress",current);
                 player.seekTo(progress);
            }

        }
    }

    /**
     * 改变页面上播放的位置
     * @param position
     *@param  isCompleted 记录是否自然播放当下一首，如果通过切换上一首下一首，那么不是自然的切换
     */
    private void sendPosition(int position,boolean isCompleted) {
        Intent intent = new Intent(Constants.ACTION_UPDATE);
        intent.putExtra("position", position);
        intent.putExtra("isPlaying", isPlaying);
        intent.putExtra("isCompleted",isCompleted);
        sendBroadcast(intent);
    }

    /**
     * 发送当前播放时间广播
     */
    private void sendCurrentTime(){
        final Intent intent = new Intent();
        intent.setAction(Constants.MUSIC_CURRENT);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    intent.putExtra("currentTime", player.getCurrentPosition());
                    sendBroadcast(intent);
                }
                // 发送空消息持续时间
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        };
        sendBroadcast(intent);


    }
    public int setNextPosition(PlayMode playMode, int position) {
        switch (playMode) {
            case LOOP:
                position++;
                if (position > songSize - 1) {
                    position = 0;
                }
                break;
            case SHUFFLE:
                position = (int) (Math.random() * songSize);
                break;
            case SINGLE:
                return position;

        }
        return position;
    }

    /*
     * 准备播放音乐并添加播放与完结的事件监听
     */
    private void prepareMusic(final int position) {
        player.reset();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC); // 设置播放类型
        String url = resourceVos.get(position).getResourceUrl();
        Log.i("position", "position:============prepareMusic" + url);
        Log.i("position", "position:============prepareMusic" + TextUtil.encodeChineseUrl(url));
        try {
            player.setDataSource(TextUtil.encodeChineseUrl(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.setOnPreparedListener(MusicService.this);
        player.setOnCompletionListener(MusicService.this);
        player.prepareAsync();
        isPrepare = false;
        sendisPrepared(-1);
        sendCurrentTime();

    }

    /**
     * 音乐播放完成的回调函数
     */
    @Override
    public void onCompletion(MediaPlayer arg0) {
        isCompleted = true;
        Log.i("TTTTTTTTTT",isPrepare+"====================isPrepare======onCompletion");
        if (isPrepare) {
            isPlaying = true;
            isFirst = false;
            position = setNextPosition(Constants.playMode, position);
            prepareMusic(position);
            sendPosition(position,isCompleted);
        }
    }

    /**
     * @param state 0,准备完成，开始播放，1暂停，2停止
     */
    private void sendisPrepared(int state) {
        Intent intent = new Intent();
        intent.setAction(Constants.MUSIC_ANIAMATION);
        intent.putExtra("state", state);
        intent.putExtra("duration",getDuration());
        sendBroadcast(intent);
    }
    /**
     * 音乐准备完成的回调函数
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i("TTTTTTTTTT",isFirst+"================isFirst==========");
        isPrepare = true;
        if (mediaPlayer != null && !isFirst) {
            mediaPlayer.start();
            sendisPrepared(0);
        }else {
            sendisPrepared(1);
        }
        handler.sendEmptyMessage(1);
    }
    public void startMusic() {
        Log.i("TTTTTTTTTT",isFirst+"================isFirst==========");
        if (player != null) {
            if (isFirst){
                isFirst = false;
            }
            Log.i("TTTTTTTTTT",isFirst+"================isFirst==========");
            player.start();
        }
    }

    public void pauseMusic() {
        current = player.getCurrentPosition();
        if (player != null) {
            player.pause();
            //sendisPrepared(2);
        }
    }

    public void stopMusic() {
        current = player.getCurrentPosition();
        if (player != null) {
            player.stop();
            player.seekTo(0);
            sendisPrepared(3);
        }
    }

}