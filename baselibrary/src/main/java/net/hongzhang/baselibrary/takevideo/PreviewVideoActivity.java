package net.hongzhang.baselibrary.takevideo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import net.hongzhang.baselibrary.R;


public class PreviewVideoActivity extends Activity implements View.OnClickListener {
    public static final int SOURCE_STATUS = 1;
    public static final int SOURCE_SCHOOL = 2;
    public static final int RESULT_DELETE = 3;
    private TextureView textureView;
    private MediaPlayerManager playerManager;
    private String recorderPath;
    private ImageView iv_left;
    private ImageView iv_delete;
    private int source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);
     //   G.setTranslucent(this);
        init();
    }

    private void init() {
        textureView = (TextureView) findViewById(R.id.texture_view);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        playerManager = MediaPlayerManager.getInstance(getApplication());
        recorderPath = getIntent().getStringExtra("path");
        source = getIntent().getIntExtra("source", SOURCE_STATUS);
        textureView.setSurfaceTextureListener(listener);
        iv_delete.setOnClickListener(this);
        iv_left.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerManager.stopMedia();
    }

    /**
     * camera回调监听
     */
    private TextureView.SurfaceTextureListener listener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            if (recorderPath != null) {
                playerManager.playMedia(new Surface(texture), recorderPath);
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
}
