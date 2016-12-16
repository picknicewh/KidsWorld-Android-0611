package net.hunme.discovery.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.discovery.MainPlayActivity;
import net.hunme.discovery.modle.RecommendVo;
import net.hunme.user.activity.CollectActivity;
import net.hunme.user.activity.UserActivity;
import net.hunme.user.mode.BannerVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/12/12
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MainRecommendPresenter implements MainRecommendContract.Presenter, OkHttpListener {
    private Context context;
    public static final int TYPE_MUISC = 1;
    public static final int  TYPE_VIDEO= 0;
    public static final int  TYPE_CONSULT = 2;
    private int type;
    private String tsid;
    private MainRecommendContract.View view;
    public MainRecommendPresenter(Context context,MainRecommendContract.View view) {
        this.context = context;
        this.view =view ;
        tsid  = UserMessage.getInstance(context).getTsId();
        type = TYPE_MUISC;
        getRecommendResource(tsid,4,TYPE_MUISC);

    }

    @Override
    public void getRecommendResource(String tsId, int size, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("size", size);
        map.put("type", type);
        Type mType = new TypeToken<Result<List<RecommendVo>>>() {}.getType();
        OkHttps.sendPost(mType, Apiurl.GETRECOMMENDLIST, map, this);
        view.showLoadingDialog();
    }

    @Override
    public void getBanner(String tsId) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        Type mType = new TypeToken<Result<List<BannerVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.GETBANNERLIST, map, this);
    }

    @Override
    public void startVideoListActivity(String AlbumId) {
        Intent intent = new Intent();
        intent.putExtra("AlbumId", AlbumId);
        context.startActivity(intent);
    }

    @Override
    public void startMusicListActivity(String AlbumId) {
        Intent intent = new Intent();
        intent.putExtra("AlbumId", AlbumId);
        context.startActivity(intent);
    }

    @Override
    public void startConsultListActivity(String AlbumId) {
        Intent intent = new Intent();
        intent.putExtra("AlbumId", AlbumId);
        context.startActivity(intent);
    }

    @Override
    public void startVideoActivity(String AlbumId) {
        Intent intent = new Intent();
        intent.putExtra("AlbumId", AlbumId);
        context.startActivity(intent);
    }

    @Override
    public void startMusicActivity(String AlbumId, String resourceId) {
        Intent intent = new Intent(context, MainPlayActivity.class);
        intent.putExtra("themeId", AlbumId);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }

    @Override
    public void startConsultActivity(int resourceId) {
        Intent intent = new Intent();
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }

    @Override
    public void startSearchActivity() {
        Intent intent = new Intent();
        context.startActivity(intent);
    }

    @Override
    public void startHistoryActivity() {
        Intent intent = new Intent(context, CollectActivity.class);
        intent.putExtra("source", 1);
        context.startActivity(intent);
    }

    @Override
    public void startUserActivity() {
        Intent intent = new Intent(context, UserActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.GETRECOMMENDLIST)) {
           if (date!=null){
               Result<List<RecommendVo>> data = (Result<List<RecommendVo>>) date;
               List<RecommendVo> recommendVos = data.getData();
               if (type==TYPE_MUISC){
                   view.setRecommendVoMusicList(recommendVos);
                   type = TYPE_VIDEO;
                  getRecommendResource(tsid,4,type);
               }else if (type==TYPE_VIDEO){
                  view.setRecommendVoClassList(recommendVos);
                   type = TYPE_CONSULT;
                   getRecommendResource(tsid,6,type);
               }else if (type==TYPE_CONSULT){
                   view.setRecommendVoConsultList(recommendVos);
               }
           }
        } else if (uri.equals(Apiurl.GETBANNERLIST)) {
            if (date!=null){
                Result< List<BannerVo>> data = (Result< List<BannerVo>>) date;
                List<BannerVo> bannerVos = data.getData();
                view.setBannerList(bannerVos);
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
}
