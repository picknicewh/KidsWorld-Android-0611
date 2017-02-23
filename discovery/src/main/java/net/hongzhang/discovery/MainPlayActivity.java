package net.hongzhang.discovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.baselibrary.widget.PromptPopWindow;
import net.hongzhang.discovery.service.LockScreenService;
import net.hongzhang.discovery.util.AnimationUtil;
import net.hongzhang.discovery.util.BaseMusicActivity;
import net.hongzhang.discovery.util.Constants;
import net.hongzhang.discovery.util.PlayListPopuWindow;
import net.hongzhang.discovery.util.PlayMode;
import net.hongzhang.discovery.util.PlayMusicContract;
import net.hongzhang.discovery.util.PlayMusicPresenter;
import net.hongzhang.discovery.util.TextUtil;

import java.util.List;

public class MainPlayActivity extends BaseMusicActivity implements View.OnClickListener, PlayMusicContract.View {
    /**
     * 导航栏左边的按钮
     */
    private ImageView iv_left;
    /**
     * 导航栏的标题----歌曲名
     */
    private TextView tv_content;
    private TextView tv_song_name;
    /**
     * 上一曲按钮
     */
    private ImageView iv_preSong;
    /**
     * 下一曲按钮
     */
    private ImageView iv_nextSong;
    /**
     * 播放/暂停 按钮
     */
    private ImageView iv_play;
    /**
     * 专辑封面布局控件
     */
    private FrameLayout fl_album;
    /**
     * 专辑封面
     */
    private CircleImageView iv_album;
    /**
     * 当前播放的时间显示
     */
    private TextView tv_currentTime;
    /**
     * 当前播放音乐总时长显示
     */
    private TextView tv_durationTime;
    /**
     * 音乐播放拖动进度条
     */
    private SeekBar seekBar;
    /**
     * 音乐播放模式
     */
    private ImageView iv_play_mode;
    /**
     * 圆形加载控件
     */
    private ImageView iv_circle;
    /**
     * 创建人播放音乐的姓名
     */
    private TextView tv_create_name;
    /**
     * 专辑列表展示按钮
     */
    private ImageView iv_more;
    /**
     * 上传者布局
     */
    private RelativeLayout rl_from;
    private RelativeLayout rl_play;
    /**
     * 界面布局
     */
    private LinearLayout ll_main;
    /**
     * 加载音乐动画
     */
    private ImageView iv_loading;
    private boolean isVisible = true;
    /**
     * 音乐播放数据处理
     */
    private PlayMusicPresenter presenter;
    /**
     * 当前音乐播放位置
     */
    private int position = 0;
    /**
     * 音乐是否正在播放
     */
    public static boolean isPlaying = false;
    /**
     * 资源列表
     */
    private List<ResourceVo> resourceVos;
    /**
     * 音乐监听广播
     */
    private MyMusicReceiver myMusicReceiver;
    /**
     * 当前播放专辑id
     */
    private String themeId;
    /**
     * 当前播放资源id
     */
    private String resourceId;
    /**
     * 播放模式
     */
    private PlayMode playMode;
    /**
     * 启动锁屏意图
     */
    private Intent mLockScreenIntent;
    /**
     * 封面旋转动画
     */
    private Animation operatingAnim;
    /**
     * 是否第一次播放专辑的第一首歌曲
     */
    private boolean isFirst = true;
    /**
     * 已经播放时长
     */
    public int hasplayTime = 0;
    /**
     * 是否已经播放
     */
    public boolean ishasPlay = false;
    /**
     * 用户角色id
     */
    private String tsId;
    /**
     * 是否免费资源-----如果普通用户非免费资源不能播放。如果会员用户所有资源都可光看
     */
    private int isResourceFree;
    /**
     * 旋转动画工具类
     */
    private AnimationUtil animationUtil;
    /**
     * 用户信息类
     */
    private UserMessage userMessage;
    private LinearLayout ll_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        G.initDisplaySize(this);
        initView();
        getData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_content = $(R.id.tv_title);
        iv_left = $(R.id.iv_left);
        tv_song_name = $(R.id.tv_song_name);
        iv_nextSong = $(R.id.iv_nextSong);
        iv_preSong = $(R.id.iv_preSong);
        iv_play = $(R.id.iv_play);
        iv_album = $(R.id.image_view_album);
        iv_play_mode = $(R.id.iv_play_mode);
        ll_main = $(R.id.ll_main);
        rl_from = $(R.id.rl_from);
        iv_more = $(R.id.iv_more);
        rl_play = $(R.id.rl_play);
        iv_loading = $(R.id.iv_loading);
        tv_currentTime = $(R.id.tv_current);
        tv_durationTime = $(R.id.tv_duration);
        iv_circle = $(R.id.iv_circle);
        seekBar = $(R.id.sb_music);
        fl_album = $(R.id.fl_album);
        ll_play = $(R.id.ll_play);
        tv_create_name = $(R.id.tv_create_name);
        iv_more.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_nextSong.setOnClickListener(this);
        iv_preSong.setOnClickListener(this);
        iv_play_mode.setOnClickListener(this);
        ll_main.setOnClickListener(this);
        ll_play.setOnClickListener(this);
        animationUtil = new AnimationUtil(fl_album);
        iv_left.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (b) {
                    presenter.changeSeeBar(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                presenter.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                presenter.play();
            }
        });
    }

    /**
     * 是否vivoX5
     */
    private boolean isVivoX5() {
        if (android.os.Build.MODEL.equals("vivo X5Max+")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 数据获取
     */
    private void getData() {
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.loading);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        userMessage = UserMessage.getInstance(this);
        tsId = userMessage.getTsId();
        playMode = PlayMode.getDefault();
        Intent intent = getIntent();
        themeId = intent.getStringExtra("themeId");
        resourceId = intent.getStringExtra("resourceId");
        startLockScreenService();
        myMusicReceiver = new MyMusicReceiver();
        presenter = new PlayMusicPresenter(this, this, position, myMusicReceiver, themeId, resourceId);
    }

    /**
     * 启动锁屏
     */
    private void startLockScreenService() {
        mLockScreenIntent = new Intent(this, LockScreenService.class);
        startService(mLockScreenIntent);
    }

    /**
     * 关闭锁屏
     */
    private void stopLockScreenService() {
        if (mLockScreenIntent != null) {
            stopService(mLockScreenIntent);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_more) {
            PlayListPopuWindow playListPopuWindow = new PlayListPopuWindow(this, resourceVos, themeId, position);
            playListPopuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        } else if (viewId == R.id.iv_play || viewId == R.id.ll_play) {
            //isResourceFree==2免费==1收费
            if (isResourceFree == 2) {
                play();
            } else {
                PromptPopWindow promptPopWindow = new PromptPopWindow(this, "小主人，您的套餐已到期暂不能收看了。别难受，尽快续订，和贝贝虎一起愉快的玩耍吧!");
                promptPopWindow.showAtLocation(view, Gravity.NO_GRAVITY, (int) (G.size.W * 0.1), (int) (G.size.H * 0.7));
              /*  if (isResourceFree==1){

                }else {
                    play();
                }*/
            }
        } else if (viewId == R.id.iv_nextSong) {
            boolean isFastClick = isFastClick(clickTime);
            if (!isFastClick) {
                clickTime = System.currentTimeMillis();
                presenter.nextSong();
            }
        } else if (viewId == R.id.iv_preSong) {
            boolean isFastClick = isFastClick(clickTime);
            if (!isFastClick) {
                clickTime = System.currentTimeMillis();
                presenter.preSong();
            }
        } else if (viewId == R.id.iv_play_mode) {
            PlayMode mPlayMode = PlayMode.switchNextMode(Constants.playMode);
            setPlayMode(mPlayMode);
            Constants.playMode = mPlayMode;
        } else if (viewId == R.id.iv_left) {
            close();
        }
    }

    /**
     * 播放
     */
    private void play() {
        //当第一次播放时点击按钮开始播放，记录当前播放记录
        if (position == 0 && isFirst) {
            ishasPlay = true;
            Log.i("ssssssssss", "tsId:" + tsId + "===========resourceId:" + resourceId + "==========hasplayTime:" + hasplayTime + "== //当第一次播放时点击按钮开始播放，记录当前播放记录===");
            presenter.savePlayTheRecord(tsId, resourceId, hasplayTime, 1);
            isFirst = false;
        }
        if (isPlaying) {
            presenter.pause();
            isPlaying = false;
            if (!isVivoX5()) {
                animationUtil.pauseRotateAnimation();
            }
        } else {
            ishasPlay = true;
            presenter.play();
            isPlaying = true;
            if (!isVivoX5()) {
                animationUtil.resumeRotateAnimation();
            }
        }
    }

    private long clickTime = 0;

    /**
     * 是否快速点击
     *
     * @param clickTime 第一次点击时间
     */
    private boolean isFastClick(long clickTime) {
        long currentTime = System.currentTimeMillis();
        long between = currentTime - clickTime;
        if (between < 500) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("sssss", "====================onResume=======================");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startLockScreenService();
        Log.i("sssss", "====================onRestart=======================");
    }

    /**
     * 根据播放资源列表
     *
     * @param resourceVos 资源
     */
    @Override
    public void setSongList(List<ResourceVo> resourceVos) {
        this.resourceVos = resourceVos;
        if (resourceVos.size() > 0 && resourceVos != null) {
            if (resourceId == null) {
                resourceId = String.valueOf(resourceVos.get(position).getResourceId());
            }
            setSongInfo(position);
        }
    }

    /**
     * 设置所有控件的数据
     *
     * @param position 当前播放音乐的位置
     */
    @Override
    public void setSongInfo(int position) {
        isResourceFree = resourceVos.get(position).getPay();
        tv_content.setText(resourceVos.get(position).getResourceName());
        String url = resourceVos.get(position).getImageUrl();
        ImageCache.imageLoaderPlay(TextUtil.encodeChineseUrl(url), iv_album);
        String createName = resourceVos.get(position).getCreateName();
        tv_create_name.setText(createName);
        ll_main.setBackgroundResource(TextUtil.getInforImage(createName, 1));
        rl_from.setBackgroundResource(TextUtil.getInforImage(createName, 2));
        rl_from.setAlpha((float) 0.8);
        iv_circle.setBackgroundResource(TextUtil.getInforImage(createName, 3));
    }

    /**
     * 根据不同的是否播放显示相对于的播放/暂停图标
     *
     * @param isPlay
     */
    @Override
    public void setIsPlay(boolean isPlay) {
        isPlaying = isPlay;
        if (isPlay) {
            iv_play.setImageResource(R.mipmap.ic_pause);
        } else {
            iv_play.setImageResource(R.mipmap.ic_play);
        }
    }

    /**
     * 设置当前播放位置
     *
     * @param position 当前播放位置
     */
    @Override
    public void setPosition(int position) {
        this.position = position;
        if (resourceVos.size() > 0 && resourceVos != null) {
            resourceId = String.valueOf(resourceVos.get(position).getResourceId());
            setSongInfo(position);
        }
    }

    /**
     * 设置当前播放模式
     *
     * @param playMode 当前播放模式
     */
    @Override
    public void setPlayMode(PlayMode playMode) {
        switch (playMode) {
            case LOOP:
                iv_play_mode.setImageResource(R.mipmap.ic_loop_circle);
                break;
            case SHUFFLE:
                iv_play_mode.setImageResource(R.mipmap.ic_loop_random);
                break;
            case SINGLE:
                iv_play_mode.setImageResource(R.mipmap.ic_loop_single);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unRisterReceiver();
        close();
        if (ishasPlay) {
            Log.i("ssssssssss", "tsId:" + tsId + "===========resourceId:" + resourceId + "==========hasplayTime:" + hasplayTime + "==========直接终止");
            presenter.savePlayTheRecord(tsId, resourceId, hasplayTime, 2);
        }
    }

    private int duration;

    public class MyMusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.MUSIC_CURRENT:
                    //获取当前暂停或停止播放时的播放时间
                    final int currentTime = intent.getIntExtra("currentTime", 0);
                    hasplayTime = currentTime / 1000;
                    tv_currentTime.setText(G.toTime(currentTime));
                    seekBar.setProgress(currentTime);
                    break;
                case Constants.MUSIC_UPDATE://播放下一首
                    isFirst = false;
                    //顺利播放下一首,记录总时间，不然，记录当前播放时间
                    boolean isCompleted = intent.getBooleanExtra("isCompleted", false);
                    if (isCompleted) {
                        presenter.savePlayTheRecord(tsId, resourceId, duration, 2);
                    } else {
                        presenter.savePlayTheRecord(tsId, resourceId, hasplayTime, 2);
                    }
                    //重新获取播放的状态
                    hasplayTime = 0;
                    isPlaying = true;
                    position = intent.getIntExtra("position", position);
                    setPosition(position);
                    break;
                case Constants.MUSIC_ANIAMATION://不同播放状态界面的不同显示
                    //state -1 没有准备成功 0第一次进入播放界面且准备成功 1播放歌曲准备成功 3 停止播放
                    int state = intent.getIntExtra("state", 0);
                    //当前播放音乐的总时间，当准备成功时候发送获取
                    duration = intent.getIntExtra("duration", 0);
                    seekBar.setMax(duration);
                    tv_durationTime.setText(G.toTime(duration));
                    if (state == -1) {
                        if (!isVivoX5()) {
                            iv_loading.startAnimation(operatingAnim);
                            iv_loading.setVisibility(View.VISIBLE);
                            iv_play.setClickable(false);
                        } else {
                            iv_play.setClickable(true);
                        }
                    } else if (state == 0) {
                        ishasPlay = true;
                        animationUtil.startRotateAnimation();
                        iv_loading.clearAnimation();
                        iv_play.setClickable(true);
                        iv_loading.setVisibility(View.GONE);
                        //非第一次播放,切换歌曲时，记录
                        presenter.savePlayTheRecord(tsId, resourceId, hasplayTime, 1);
                    } else if (state == 1) {
                        iv_loading.clearAnimation();
                        iv_loading.setVisibility(View.GONE);
                        iv_play.setClickable(true);
                    } else if (state == 3) {
                        if (!isVivoX5()) {
                            animationUtil.cancelRotateAnimation();
                        }
                    }
                    break;
                case Constants.ACTION_NOTIFICATION://通知栏状态
                    //op 不同的操作
                    int op = intent.getIntExtra("op", -1);
                    if (op == Constants.PLAYING) {
                        if (isPlaying) {
                            presenter.pause();
                            isPlaying = false;
                            if (!isVivoX5()) {
                                animationUtil.pauseRotateAnimation();
                            }
                        } else {
                            presenter.play();
                            isPlaying = true;
                            if (!isVivoX5()) {
                                animationUtil.resumeRotateAnimation();
                            }
                        }
                    } else if (op == Constants.NEXT) {
                        presenter.nextSong();
                    } else if (op == Constants.LAST) {
                        presenter.preSong();
                    } else if (op == Constants.CLOSE) {
                        close();
                    }
                    break;
            }
        }
    }

    /**
     * 关闭通知栏，音乐播放服务，锁屏服务
     */
    private void close() {
        if (resourceVos != null) {
            presenter.stopService();
            stopLockScreenService();
            presenter.cleanNotification();
            BaseLibrary.removeLockActivity();
            seekBar.setProgress(0);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
        }
        return super.onKeyDown(keyCode, event);
    }
}
