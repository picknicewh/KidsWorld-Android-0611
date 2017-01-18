package net.hunme.discovery.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import net.hunme.discovery.modle.CompilationVo;
import net.hunme.discovery.modle.RecommendVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghua on 2017/1/3.
 */
public class PlayVideoDetailPresenter implements PlayVideoDetailContract.Presenter, OkHttpListener, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    private static MediaPlayer player;
    private Activity context;
    private PlayVideoDetailContract.View view;
    private SurfaceHolder surfaceHolder;
    private List<ResourceVo> resourceVos;
    private int position=0;
    private String resourceId;
    private String tsId;
    private boolean isPrepared;
    public PlayVideoDetailPresenter(Activity context, SurfaceView surfaceView, PlayVideoDetailContract.View view, String resourceId){
        this.context = context;
        this.view = view;
        this.resourceId =resourceId;
        tsId = UserMessage.getInstance(context).getTsId();
        getRecommendList(tsId,4,1);
        surfaceHolder = surfaceView.getHolder();
        getVideoList(tsId, String.valueOf("121"));
        player = new MediaPlayer();
        surfaceHolder.addCallback(this);
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
    public void getRecommendList(String tsId, int pageSize, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("pageNumber", 1);
        map.put("pageSize",pageSize);
        map.put("type", type);
        Type mType = new TypeToken<Result<List<CompilationVo>>>() {}.getType();
        OkHttps.sendPost(mType, Apiurl.GETRECOMMENDLIST, map, this);
        view.showLoadingDialog();
    }
    @Override
    public void getCommentList(String resourceId, int pageSize, int pageNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("resourceid", resourceId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        Type mType = new TypeToken<Result<List<RecommendVo>>>() {}.getType();
        OkHttps.sendPost(mType, Apiurl.GETCOMMENTLIST, map, this);
        view.showLoadingDialog();
    }

    @Override
    public void subComment(String tsId, String resourceId, String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("resourceid", resourceId);
        map.put("tsId", tsId);
        map.put("content", content);
        Type mType = new TypeToken<Result<String>>() {}.getType();
        OkHttps.sendPost(mType, Apiurl.SUBMENTCOMMENT, map, this);
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
        if (player!=null){
            player.start();
            view.setIsPlay(true);
        }
    }
    @Override
    public void pause() {
        if (player!=null){
            player.pause();
            view.setIsPlay(false);
        }
    }
    @Override
    public boolean getPrepared() {
        return  this.isPrepared;
    }
    @Override
    public void setPosition(int position){
        this.position = position;
        view.setVideoInfo(resourceVos.get(position),position);
        setup();
    }
    @Override
    public void nextSong() {
        position++;
        if (position > resourceVos.size() - 1) {
            position = 0;
        }
        view.setVideoInfo(resourceVos.get(position),position);
        setup();
    }

    @Override
    public void preSong() {
        position--;
        if (position <0) {
            position = resourceVos.size() - 1;
        }
        view.setVideoInfo(resourceVos.get(position),position);
        setup();
    }
    @Override
    public void changeSeeBar(int progress) {
        if (player!=null){
            player.seekTo(progress);
        }
    }
    @Override
    public void setScreenDirection(int orientation, RelativeLayout rl_control,RelativeLayout rl_control_full,LinearLayout ll_detail, SurfaceView surfaceView) {
        int  vWidth = G.size.W;
        int vHeight =  ( G.size.W*9/16);
        WindowManager.LayoutParams attr = context.getWindow().getAttributes();
        Window window = context.getWindow();
        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        attr.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attr);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //设置横屏模式
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 横屏时
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            attr.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(attr);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            ll_detail.setVisibility(View.GONE);
            rl_control.setVisibility(View.GONE);
            rl_control_full.setVisibility(View.VISIBLE);
        } else {
            // 竖屏
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(vWidth, vHeight));
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(attr);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            G.setTranslucent(context);
            ll_detail.setVisibility(View.VISIBLE);
            rl_control.setVisibility(View.VISIBLE);
            rl_control_full.setVisibility(View.GONE);
        }
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
                    view.setVideoInfo(resourceVos.get(position),position);

                }
            }
        }else  if (uri.equals(Apiurl.GETRECOMMENDLIST)) {
            if (date!=null){
                Result<List<CompilationVo>> data = (Result<List<CompilationVo>>) date;
                List<CompilationVo> compilationVos = data.getData();
                view.setRecommendList(compilationVos);
            }
        }else if (uri.equals(Apiurl.SUBATTENTION)){
            if (date!=null) {
                Result<String> data = (Result<String>) date;
                String result = data.getData();
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }
        }else if (uri.equals(Apiurl.SUBMENTCOMMENT)){
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
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            if (player!=null){
                player.setDisplay(surfaceHolder);
            }
        }catch (Exception e){

        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (player!=null){
            player.release();
        }
    }
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (mediaPlayer!=null){
            isPrepared = true;
            view.stopLoadingDialog();
            player.start();
            view.setIsPlay(true);
            view.setDuration(player.getDuration());
        }
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        view.setVideoInfo(resourceVos.get(position),position);
        nextSong();
    }
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
      view.setCurrent(mediaPlayer.getCurrentPosition());
    }
}
