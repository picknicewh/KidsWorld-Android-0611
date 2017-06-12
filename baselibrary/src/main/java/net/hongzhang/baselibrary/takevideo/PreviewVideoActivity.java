package net.hongzhang.baselibrary.takevideo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.LoadingDialog;


public class PreviewVideoActivity extends Activity implements View.OnClickListener {
    public static final int SOURCE_STATUS = 1;
    public static final int SOURCE_SCHOOL = 2;
    public static final int RESULT_DELETE = 3;
    private TextureView textureView;
    private String recorderPath;
    private ImageView iv_left;
    private ImageView iv_delete;
    private int source;
    private RelativeLayout rl_head;
    private ImageView iv_image;
    public LoadingDialog dialog;
    private MediaPlayer mPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);
        G.setTranslucent(this);
        init();
    }

    private void init() {
       // textureView = (TextureView) findViewById(texture_view);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        rl_head =  (RelativeLayout)findViewById(R.id.rl_head);
        recorderPath = getIntent().getStringExtra("path");
        source = getIntent().getIntExtra("source", -1);
        holder = surfaceView.getHolder();
        //不传递source时表示查看动态查看视频
        rl_head.setVisibility(source==-1?View.GONE:View.VISIBLE);
        ImageCache.imageLoader(VideoUtil.getFirstFrame(recorderPath),iv_image);
      //  setVideoParam();
        //textureView.setSurfaceTextureListener(listener);
        holder.addCallback(callback);
        iv_delete.setOnClickListener(this);
        iv_left.setOnClickListener(this);
    }

    private SurfaceHolder.Callback callback  = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (recorderPath != null) {
                playMedia(surfaceView.getHolder(), recorderPath);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            stopMedia();
        }
    };
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_left) {
            finish();
        } else if (viewId == R.id.iv_delete) {
            if (source == SOURCE_STATUS) {
               goActivity("net.hongzhang.status.activity.PublishStatusActivity");
            } else if (source == SOURCE_SCHOOL) {
                goActivity("net.hongzhang.school.activity.SubmitTaskActivityS");
            }
        }
    }

    /**
     * 前往删除页面
     * @param activityPath
     */
    public void goActivity(String activityPath) {
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow", activityPath);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(componetName);
        setResult(RESULT_DELETE);
        finish();
    }
    /**
     * 播放Media
     */
    public void playMedia(SurfaceHolder surface, String mediaPath) {
        if (source==-1){
            showLoadingDialog();
        }
        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(mediaPath);
            } else {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                mPlayer.reset();
                mPlayer.setDataSource(mediaPath);
            }
         //   mPlayer.setSurface(surface);
            mPlayer.setDisplay(surface);
            mPlayer.setLooping(true);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    stopLoadingDialog();
                    mp.start();
                }
            });
        } catch (Exception e) {
            G.log(e);
        }
    }

    /**
     * 停止播放Media
     */
    public void stopMedia() {
        try {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                mPlayer.release();
                mPlayer = null;
            }
        } catch (Exception e) {
            G.log(e);
        }
    }
    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new LoadingDialog(this, R.style.LoadingDialogTheme);
        try {
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }

    public void stopLoadingDialog() {
        if (dialog != null) {
            try {
                dialog.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
