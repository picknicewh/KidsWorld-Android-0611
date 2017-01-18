package net.hunme.discovery.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.ResourceVo;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghua on 2017/1/3.
 */
public class PlayVideoPresenter implements PlayVideoContract.Presenter, OkHttpListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, SurfaceHolder.Callback, MediaPlayer.OnBufferingUpdateListener {
    private Activity context;
    private PlayVideoContract.View view;
    private List<ResourceVo> resourceVos;
    private int position=0;
    private String resourceId;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private boolean isPrepared;
    private  String tsId = UserMessage.getInstance(context).getTsId();
    public PlayVideoPresenter(Activity context,SurfaceView surfaceView,PlayVideoContract.View view,String alubmId,String resourceId ){
        this.context =context;
        this.view = view;
        this.resourceId =resourceId;
        holder = surfaceView.getHolder();
        tsId = UserMessage.getInstance(context).getTsId();
        getVideoList(tsId, alubmId);
        player = new MediaPlayer();
        holder.addCallback(this);
    }
    /**
     * 保存播放记录列表
     * @param  tsId 角色id
     * @param resourceId 资源id
     * @param  broadcastPace 当前播放音乐的秒数
     * @param  type 播放音乐开始/结束 type = 1 播放开始 type=2播放结束
     */
    @Override
    public void savePlayTheRecord(String tsId, String resourceId,int broadcastPace,int type) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("resourceid",resourceId);
        if (type==2){
            map.put("broadcastPace",broadcastPace);
        }
        map.put("type",type);
        Type mType=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mType, Apiurl.SAVEPLAYRECORDING,map,this);
    }
    @Override
    public void getVideoList(String tsId, String themeId) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("pageSize",999);
        map.put("pageNumber",1);
        map.put("albumId",themeId);
        map.put("account_id", UserMessage.getInstance(context).getAccount_id());
        Type mType=new TypeToken<Result<List<ResourceVo>>>(){}.getType();
        OkHttps.sendPost(mType, Apiurl.USER_GETTHENELIST,map,this);
        view.showLoadingDialog();
    }

    @Override
    public void subFavorate(String albumId, int cancel) {
        Map<String,Object> map=new HashMap<>();
        String tsId = UserMessage.getInstance(context).getTsId();
        if (G.isEmteny(tsId)){
            Toast.makeText(context,"请登录后在收藏！",Toast.LENGTH_SHORT).show();
            return;
        }
        map.put("tsId",tsId);
        map.put("albumId",albumId);
        map.put("type",cancel);
        Type mType=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mType, Apiurl.SUBATTENTION,map,this);
        view.showLoadingDialog();
    }

    @Override
    public void subComment(String resourceId, String tsId, String content) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("resourceid",resourceId);
        map.put("content",content);
        Type mType=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mType, Apiurl.SUBMENTCOMMENT,map,this);
        view.showLoadingDialog();
    }
    @Override
    public void setup() {
        try {
            player.reset();
            player.setDataSource(TextUtil.encodeChineseUrl(resourceVos.get(position).getResourceUrl()));
            player.prepareAsync();
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
            player.setOnBufferingUpdateListener(this);
            isPrepared = false;
            view.showLoadingDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        try {
            if (player!=null){
                player.start();
                view.setIsPlay(true);
                savePlayTheRecord(tsId, resourceId, player.getCurrentPosition(), 1);
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }
    @Override
    public void pause() {
        try {
            if (player!=null){
                player.pause();
                view.setIsPlay(false);
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

    }
    @Override
    public void changeSeeBar(int progress) {
        try {
            if (player!=null){
                player.seekTo(progress);
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }
    @Override
    public void setPosition(int position){
        this.position = position;
        view.setvideoInfo(resourceVos.get(position),position);
        setup();
    }
    @Override
    public void setScreenDirection(int orientation, SurfaceView surfaceView) {
        WindowManager.LayoutParams attr = context.getWindow().getAttributes();
        Window window = context.getWindow();
        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        attr.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attr);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.USER_GETTHENELIST)){
            if (date!=null){
                Result<ArrayList<ResourceVo>> data  = (Result<ArrayList<ResourceVo>>) date;
                ArrayList<ResourceVo>  resourceVos = data.getData();
                if (resourceVos.size()>0&&resourceVos!=null){
                    this.resourceVos = resourceVos;
                    view.setVideoList(resourceVos);
                    if (resourceId!=null){
                        for (int i = 0 ;i<resourceVos.size();i++){
                            ResourceVo resourceVo = resourceVos.get(i);
                            if (String.valueOf(resourceVo.getResourceId()).equals(resourceId)){
                                position =i;
                            }
                        }
                    }else {
                        position=0;
                    }
                    setup();
                    view.setvideoInfo(resourceVos.get(position),position);
                }
            }
        }else if (uri.equals(Apiurl.SUBATTENTION)){
            if (date!=null) {
                Result<String> data = (Result<String>) date;
                String result = data.getData();
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean getPrepared() {
        return  this.isPrepared;
    }
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        try {
            if (player!=null){
                Log.i("ssssssssssssss","==================================================onPrepared");
                isPrepared = true;
                view.stopLoadingDialog();
                player.start();
                view.setIsPlay(true);
                view.setDuration(player.getDuration());
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        position++;
        if (position > resourceVos.size() - 1) {
            position = 0;
        }
        view.setvideoInfo(resourceVos.get(position),position);
        setup();
        savePlayTheRecord(tsId, resourceId,mediaPlayer.getCurrentPosition(), 2);
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            if (player!=null ){
                player.setDisplay(holder);
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            if (player!=null){
                player.release();
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

    }
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        G.log("========视屏缓冲大小=========="+i);
       view.setCurrent(player.getCurrentPosition());

    }
}
