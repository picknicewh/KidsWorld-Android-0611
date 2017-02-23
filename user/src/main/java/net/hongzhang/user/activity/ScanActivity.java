package net.hongzhang.user.activity;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.MD5Utils;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.util.ewcode.CameraManager;
import net.hongzhang.baselibrary.util.ewcode.CameraManager2;
import net.hongzhang.baselibrary.util.ewcode.DecodeThread;
import net.hongzhang.baselibrary.util.ewcode.LuminanceSource;
import net.hongzhang.baselibrary.util.ewcode.PlanarYUVLuminanceSource;
import net.hongzhang.baselibrary.widget.ScanView;
import net.hongzhang.user.R;
import net.hongzhang.user.mode.ScanVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ScanActivity extends Activity implements  CameraManager.PreviewFrameShotListener, SurfaceHolder.Callback, DecodeThread.DecodeListener, OkHttpListener, CameraManager2.PreviewFrameShotListener ,View.OnClickListener{
    private static final long VIBRATE_DURATION = 200L;
    /**
     * 预览view
     */
   private SurfaceView preView;
    /**
     * 扫描view
     */
    private ScanView scanView;
    /**
     * 相机管理类
     */
    private CameraManager2 mCameraManager;
    /**
     * 是否将相机获取的图片解码成二进制
     */
    private boolean isDecoding = false;
    /**
     * 预览区域
     */
    private Rect previewFrameRect = null;
    /**
     * 解析
     */
    private DecodeThread mDecodeThread;
    // 访问相册所需的全部权限
    private final static String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA
    };
    private   String sign;
    private String AND_KEY = "9FHGghjGHJgjy57JBKnjklmkllkjnHKLM";
    /**
     * 扫描
     */
    private RelativeLayout rl_scan;
    /**
     * 确认登录
     */
    private RelativeLayout rl_conform_login;
    /**
     * 确认的图片
     */
    private ImageView iv_conform_image;
    /**
     * 确认登录
     */
    private TextView tv_conform_login;
    /**
     * 取消登录
     */
    private TextView tv_concel_login;
    /**
     * 关闭
     */
    private TextView tv_close1;
    /**
     * 关闭
     */
    private TextView tv_close2;
    private  String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        G.setTranslucent(this);
        PermissionsChecker checker=PermissionsChecker.getInstance(this);
        if(checker.lacksPermissions(PERMISSIONS)){
            checker.getPerMissions(PERMISSIONS);
            return;
        }
        initview();
    }


    private void initview(){
        preView = (SurfaceView) findViewById(R.id.sv_preview);
        scanView = (ScanView) findViewById(R.id.cv_capture);
        rl_scan = (RelativeLayout) findViewById(R.id.rl_scan);
        rl_conform_login = (RelativeLayout) findViewById(R.id.rl_conform_login);
        iv_conform_image = (ImageView) findViewById(R.id.iv_conform_image);
        tv_conform_login = (TextView) findViewById(R.id.tv_conform_login);
        tv_concel_login = (TextView) findViewById(R.id.tv_concel_login);
        tv_close1 = (TextView) findViewById(R.id.tv_close1);
        tv_close2 = (TextView) findViewById(R.id.tv_close2);
        rl_scan = (RelativeLayout) findViewById(R.id.rl_scan);
        tv_close1.setOnClickListener(this);
        tv_close2.setOnClickListener(this);
        tv_concel_login.setOnClickListener(this);
        tv_conform_login.setOnClickListener(this);
        preView.getHolder().addCallback(this);
        mCameraManager = new CameraManager2(this);
        mCameraManager.setPreviewFrameShotListener(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, int cameraWidth, int cameraHeight) {
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        if (previewFrameRect == null) {
            previewFrameRect = mCameraManager.getPreviewFrameRect(scanView.getFrameRect());
        }
        //解析中
        PlanarYUVLuminanceSource luminanceSource = new PlanarYUVLuminanceSource(data, cameraWidth, cameraHeight, previewFrameRect);
        mDecodeThread = new DecodeThread(this, luminanceSource);
        isDecoding = true;
        mDecodeThread.execute();
        //解析完成
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!  mCameraManager.initCamera(holder)){
            G.getAppDetailSettingIntent(this);
        }
        if (!mCameraManager.isCameraAvailable()) {
            Toast.makeText(this,"相机不存在!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mCameraManager.startPreview();
        if (!isDecoding) {
            mCameraManager.requestPreviewFrameShot();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCameraManager.stopPreview();
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        mCameraManager.release();
    }

    @Override
    public void onDecodeSuccess(LuminanceSource luminanceSource, Result result, Bitmap bitmap) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION);
        isDecoding = false;
        long time  = System.currentTimeMillis();
        String path = result.getText().toString();
        if (path.contains("sign=")&&path.contains("&pushId=")&&path.contains("&create_time=")){
            String hashValue="/app/qRCodeLogin"+time+AND_KEY;
            String sign = time+"-"+MD5Utils.encode(hashValue);
            String md5 = getmd5Code(path);
            scanLogin(md5,sign);
        }else {
            Toast.makeText(this,"二维码错误！",Toast.LENGTH_SHORT).show();
        }


    }
    /**@param  path
     */
    private String  getmd5Code(String path){
        Uri uri = Uri.parse(path);
        String sign = uri.getQueryParameter("sign");
        String pushId = uri.getQueryParameter("pushId");
        String create_time = uri.getQueryParameter("create_time");
        sign = MD5Utils.encode(sign);
        pushId = MD5Utils.encode(pushId);
        create_time = MD5Utils.encode(create_time);
        String md5 = create_time+sign + pushId;
        return  md5;
    }
    private void qRCodeConformLogin(String token,String sign){
        Map<String,Object> param = new HashMap<>();
        param.put("token",token);
        param.put("sign",sign );
        param.put("requestSource","android");
        Type type = new TypeToken<net.hongzhang.baselibrary.mode.Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.CONFIRMCODELOGIN,param,this);
    }

    private void scanLogin(String md5,String sign){
        Map<String,Object> param = new HashMap<>();
        param.put("md5",md5);
        param.put("tsId", UserMessage.getInstance(this).getTsId());
        param.put("requestSource","android");
        param.put("sign",sign);
        Type type = new TypeToken<net.hongzhang.baselibrary.mode.Result<ScanVo>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCANLOGIN,param,this);
    }
    @Override
    public void onDecodeFailed(LuminanceSource luminanceSource) {
        if ( !(luminanceSource instanceof PlanarYUVLuminanceSource)) {
            Toast.makeText(this, "没有找到二维码", Toast.LENGTH_SHORT).show();
        }
        isDecoding = false;
        //继续扫描
        mCameraManager.requestPreviewFrameShot();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint resultPoint) {
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.equals(Apiurl.SCANLOGIN)){
            if (date!=null){
                net.hongzhang.baselibrary.mode.Result<ScanVo> data = (net.hongzhang.baselibrary.mode.Result<ScanVo>) date;
                ScanVo  scanVo = data.getData();
                token = scanVo.getToken();
                rl_conform_login.setVisibility(View.VISIBLE);
                rl_scan.setVisibility(View.GONE);
            }
        }else if (uri.equals(Apiurl.CONFIRMCODELOGIN)){
            if (date!=null){
                net.hongzhang.baselibrary.mode.Result<String> data = (net.hongzhang.baselibrary.mode.Result<String>) date;
                String  result = data.getData();
                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
         int   viewId = view.getId();
        if (viewId==R.id.tv_conform_login){
            long time  = System.currentTimeMillis();
            String hasvalue ="/app/qRCodeConfirmLogin"+time+AND_KEY;
            String sign = time+"-"+MD5Utils.encode(hasvalue);
            String mToken = MD5Utils.encode(token);
            qRCodeConformLogin(mToken,sign);
        }else if (viewId==R.id.tv_concel_login){
            rl_scan.setVisibility(View.VISIBLE);
            rl_conform_login.setVisibility(View.GONE);
        }else if (viewId==R.id.tv_close1){
             finish();
        }else if (viewId==R.id.tv_close2){
              finish();
        }
    }
}
