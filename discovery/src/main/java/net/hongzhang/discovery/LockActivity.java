package net.hongzhang.discovery;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.discovery.service.LockScreenService;
import net.hongzhang.discovery.util.Constants;
import net.hongzhang.discovery.util.ImagerComber;
import net.hongzhang.discovery.util.LockLayer;
import net.hongzhang.discovery.util.PlayMusicPresenter;
import net.hongzhang.discovery.util.TextUtil;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class LockActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
    /**
     * 当前时间
     */
    private TextView tv_time;
    /**
     * 日期
     */
    private TextView tv_date;
    /**
     * 星期
     */
    private TextView tv_week;
    /**
     * 歌曲名
     */
    private TextView tv_song;
    /**
     * 歌手
     */
    private TextView tv_artist;
    /**
     * 解锁
     */
    private LinearLayout ll_unlock;
    /**
     * 上一曲
     */
    private ImageView iv_pre;
    /**
     * 播放
     */
    private ImageView iv_play;
    /**
     * 下一曲
     */
    private ImageView iv_next;
    /**
     * 解锁动画
     */
    private ImageView iv_slider_image;
    /**
     * 获取歌手和歌曲名广播
     */
    private MyMusicChangeReceiver myMusicChangeReceiver;
    private RelativeLayout rl_lock_bg;
    private boolean isPlaying = false;
    /**
     * 专辑图片
     */
    private CircleImageView iv_album;
    private AnimationDrawable animationDrawable;
    // 锁屏所需的全部权限
    private final static String[] PERMISSIONS = new String[]{
            Manifest.permission.DISABLE_KEYGUARD
    };
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private LockLayer lockLayer;
    private View lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lock = getLayoutInflater().inflate(R.layout.activity_lockview, null);
            initView(lock);
        } else {
            getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
            setContentView(R.layout.activity_lockview);
            initView(null);
        }
        PermissionsChecker checker = PermissionsChecker.getInstance(this);
        if (checker.lacksPermissions(PERMISSIONS)) {
            checker.getPerMissions(PERMISSIONS);
            return;
        }
        initSetting();
        Log.i("yyyyy", "onCreate==========================");
    }

    private void initSetting() {
        BaseLibrary.addLockActivity(this);
        G.setTranslucent(this);
        G.initDisplaySize(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    /**
     * 初始化
     */
    private void initView(View view) {
        if (view == null) {
            tv_time = (TextView) findViewById(R.id.time);
            tv_date = (TextView) findViewById(R.id.date);
            tv_week = (TextView) findViewById(R.id.tv_week);
            tv_song = (TextView) findViewById(R.id.music);
            ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
            tv_artist = (TextView) findViewById(R.id.artist);
            iv_play = (ImageView) findViewById(R.id.iv_play);
            iv_pre = (ImageView) findViewById(R.id.iv_pre);
            iv_next = (ImageView) findViewById(R.id.iv_next);
            iv_album = (CircleImageView) findViewById(R.id.iv_bg);
            rl_lock_bg = (RelativeLayout) findViewById(R.id.rl_lock_bg);
            iv_slider_image = (ImageView) findViewById(R.id.iv_slider_image);
        } else {
            lockLayer = LockLayer.getInstance(this);
            tv_time = (TextView) view.findViewById(R.id.time);
            tv_date = (TextView) view.findViewById(R.id.date);
            tv_week = (TextView) view.findViewById(R.id.tv_week);
            tv_song = (TextView) view.findViewById(R.id.music);
            ll_unlock = (LinearLayout) view.findViewById(R.id.ll_unlock);
            tv_artist = (TextView) view.findViewById(R.id.artist);
            iv_play = (ImageView) view.findViewById(R.id.iv_play);
            iv_pre = (ImageView) view.findViewById(R.id.iv_pre);
            iv_next = (ImageView) view.findViewById(R.id.iv_next);
            iv_album = (CircleImageView) view.findViewById(R.id.iv_bg);
            rl_lock_bg = (RelativeLayout) view.findViewById(R.id.rl_lock_bg);
            iv_slider_image = (ImageView) view.findViewById(R.id.iv_slider_image);
            lockLayer.setLockView(lock);
            lockLayer.lock();
            lock.setOnTouchListener(this);
        }
        iv_next.setOnClickListener(this);
        iv_pre.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        initData();

    }

    private void initData() {
        registerReceiver();
        setCurrenTime();
        setSlideImage();
        resourceVo = PlayMusicPresenter.resourceVos.get(PlayMusicPresenter.position);
        isPlaying = MainPlayActivity.isPlaying;
        setTextData(resourceVo, isPlaying);
        defaulData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onTouch(event,ll_unlock);
        return super.onTouchEvent(event);
    }
    private void setSlideImage() {
        iv_slider_image.setImageResource(R.drawable.ic_slide_image);
        animationDrawable = (AnimationDrawable) iv_slider_image.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected void onRestart() {
        Log.i("yyyyy", "onRestart==========================");
        setTextData(resourceVo, isPlaying);
        super.onRestart();
        animationDrawable.stop();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_pre) {
            playAction(Constants.LAST);
        } else if (viewId == R.id.iv_play) {
            if (isPlaying) {
                iv_play.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                iv_album.pauseRotateAnimation();
            } else {
                iv_play.setImageResource(R.drawable.ic_pause);
                isPlaying = true;
                iv_album.resumeRotateAnimation();
            }
            playAction(Constants.PLAYING);
        } else if (viewId == R.id.iv_next) {
            playAction(Constants.NEXT);
        } else if (viewId == R.id.ll_unlock) {
            startHome();
        }
    }

    /**
     * 播放操作
     *
     * @param op 选项
     */
    private void playAction(int op) {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_NOTIFICATION);
        intent.putExtra("op", op);
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        Log.i("yyyyy", "onDestroy==========================");
        super.onDestroy();
        unregisterReceiver(myMusicChangeReceiver);
        iv_album.cancelRotateAnimation();
        if (lockLayer != null) {
            lockLayer.unlock();
        }
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        myMusicChangeReceiver = new MyMusicChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_CHANGETEXT);
        registerReceiver(myMusicChangeReceiver, filter);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Calendar calendar = Calendar.getInstance();
                int mouth = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                String mt, h, d, mo;
                if (minute < 10) mt = "0" + minute;
                else mt = String.valueOf(minute);
                if (hour < 10) h = "0" + hour;
                else h = String.valueOf(hour);
                if (day < 10) d = "0" + day;
                else d = String.valueOf(day);
                if (mouth < 10) mo = "0" + mouth;
                else mo = String.valueOf(mouth);
                String time = h + ":" + mt;
                String date = mo + "月" + d + "日";
                String weekday = DateUtil.getWeekOfday(week);
                tv_time.setText(time);
                tv_date.setText(date);
                tv_week.setText(weekday);
            }
        }
    };

    private void startHome() {
        LockScreenService.isUnLock = true;
        finish();
        BaseLibrary.removeLockActivity();
    }

    /**
     * 定时的获取当前的时间
     */
    private void setCurrenTime() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        onTouch(motionEvent,ll_unlock);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return true;
    }

    private class MyMusicChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ACTION_CHANGETEXT)) {
                resourceVo = intent.getParcelableExtra("resourceVo");
                isPlaying = intent.getBooleanExtra("isPlaying", false);
                setTextData(resourceVo, isPlaying);
            }
        }
    }

    private static ResourceVo resourceVo;

    private void setTextData(ResourceVo resourceVo, boolean isPlaying) {
        tv_song.setText(resourceVo.getResourceName());
        // tv_artist.setText(String.valueOf(resourceVo.getSource()));
        Bitmap bitmap = ImageCache.getBitmap(TextUtil.encodeChineseUrl(resourceVo.getImageUrl()));
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_default);
            iv_album.setImageBitmap(bitmap);
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_img_error);
            bitmap = ImagerComber.fastBlur(this, bitmap, 160);
        } else {
            iv_album.setImageBitmap(bitmap);
            bitmap = ImagerComber.fastBlur(this, bitmap, 160);
        }
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable.setAlpha(180);
        rl_lock_bg.setBackground(drawable);
        if (isPlaying) {
            iv_play.setImageResource(R.drawable.ic_pause);
            iv_album.startRotateAnimation();
        } else {
            iv_play.setImageResource(R.drawable.ic_play);
            iv_album.pauseRotateAnimation();
        }
    }

    private int marginLeft;
    private int height;
    private int marginBottom;
    private int lock_height;
    private int lock__width;
    private void defaulData() {
        ll_unlock.measure(0, 0);
        lock__width = ll_unlock.getMeasuredWidth();
        lock_height = ll_unlock.getMeasuredHeight();
        int screenWidth = G.size.W;
        int screenHeight = G.size.H;
        marginLeft = (screenWidth - lock__width) / 2;
        marginBottom = G.dp2px(this, 20);
        height = screenHeight - marginBottom;
    }

    private void setLockParam(int marginLeft) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.setMargins(marginLeft, 0, 0, marginBottom);
        ll_unlock.setLayoutParams(layoutParams);
    }

    private void onTouch(MotionEvent motionEvent,LinearLayout ll_unlock) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int move_x = (int) motionEvent.getX();
                int move_y = (int) motionEvent.getY();
                if (move_y < height && move_y > height - lock_height) {
                    marginLeft = move_x;
                    if (marginLeft >= G.size.W - lock__width || marginLeft <= 0) {
                        ll_unlock.setVisibility(View.GONE);
                        startHome();
                    } else {
                        setLockParam(marginLeft);
                        ll_unlock.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                marginLeft = (G.size.W - lock__width) / 2;
                setLockParam(marginLeft);
                break;
        }
    }
}
