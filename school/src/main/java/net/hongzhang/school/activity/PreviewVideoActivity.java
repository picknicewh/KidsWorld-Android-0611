package net.hongzhang.school.activity;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

import net.hongzhang.school.R;
import net.hongzhang.school.util.MediaPlayerManager;

public class PreviewVideoActivity extends Activity {
    private TextureView textureView;
    private MediaPlayerManager playerManager;
   private  String recorderPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);
        init();
    }
    private void init() {
        textureView = (TextureView) findViewById(R.id.texture_view);
        playerManager = MediaPlayerManager.getInstance(getApplication());
        recorderPath= getIntent().getStringExtra("path");
        textureView.setSurfaceTextureListener(listener);
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
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {}
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {}
    };

}
