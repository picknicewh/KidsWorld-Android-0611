package net.hongzhang.discovery.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.MainPlayActivity;
import net.hongzhang.discovery.activity.PlayVideoActivity;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.user.activity.CollectActivity;
import net.hongzhang.user.activity.UserActivity;
import net.hongzhang.user.mode.BannerVo;

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
    public static final int TYPE_MUISC = 2;
    public static final int  TYPE_VIDEO= 1;
    public static final int  TYPE_CONSULT = 3;
    private int type;
    private String tsid;
    private MainRecommendContract.View view;
    public MainRecommendPresenter(Context context,MainRecommendContract.View view) {
        this.context = context;
        this.view =view ;
        tsid  = UserMessage.getInstance(context).getTsId();
        type = TYPE_MUISC;
        getBanner(tsid);
        getRecommendResource(tsid,4,TYPE_MUISC);
    }

    @Override
    public void getRecommendResource(String tsId, int pageSize, int type) {
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
    public void getBanner(String tsId) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        Type mType = new TypeToken<Result<List<BannerVo>>>() {}.getType();
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
        Intent intent = new Intent(context, PlayVideoActivity.class);
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
               Result<List<CompilationVo>> data = (Result<List<CompilationVo>>) date;
               List<CompilationVo> compilationVos = data.getData();
               if (type==TYPE_MUISC){
                   view.setRecommendVoMusicList(compilationVos);
                   type = TYPE_VIDEO;
                  getRecommendResource(tsid,4,type);
               }else if (type==TYPE_VIDEO){
                  view.setRecommendVoClassList(compilationVos);
                   type = TYPE_CONSULT;
                   getRecommendResource(tsid,6,type);
               }else if (type==TYPE_CONSULT){
                   view.setRecommendVoConsultList(compilationVos);
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
