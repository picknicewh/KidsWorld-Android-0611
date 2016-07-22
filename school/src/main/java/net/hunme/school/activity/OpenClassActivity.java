package net.hunme.school.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import net.hunme.school.R;

/**
 * 作者： Administrator
 * 时间： 2016/7/15
 * 名称：开放课堂
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class OpenClassActivity extends Activity implements SurfaceHolder.Callback{
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean previewRunning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openclass);
        surfaceView = (SurfaceView)findViewById(R.id.surface_camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //打开摄像头
       camera = android.hardware.Camera.open();
        if (camera!=null){
            Camera.Parameters parameters = camera.getParameters();
        }else {
            finish();
            Toast.makeText(this,"没有摄像头！",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
      if (previewRunning){
          camera.startPreview();
          Camera.Parameters parameters = camera.getParameters();
      }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
     camera.startPreview();
    }
}
