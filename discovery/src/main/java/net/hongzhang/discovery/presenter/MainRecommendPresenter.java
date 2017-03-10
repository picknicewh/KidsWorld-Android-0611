package net.hongzhang.discovery.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.activity.ConsultActivity;
import net.hongzhang.discovery.activity.ConsultListActivity;
import net.hongzhang.discovery.activity.MainPlayMusicActivity;
import net.hongzhang.discovery.activity.ResourceAlubmListActivity;
import net.hongzhang.discovery.activity.PlayVideoListActivity;
import net.hongzhang.discovery.activity.SearchResourceActivity;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.modle.ConsultInfoVo;
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
    public void getRecommendConsult(String tsId, int pageSize, String account_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("pageNumber", 1);
        map.put("pageSize",pageSize);
        map.put("type", 1);
        map.put("account_id",account_id);
        Type mType = new TypeToken<Result<List<ConsultInfoVo>>>() {}.getType();
        OkHttps.sendPost(mType, Apiurl.GETRECONSULT, map, this);
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
    public void startVideoListActivity() {
        Intent intent = new Intent(context, ResourceAlubmListActivity.class);
        intent.putExtra("type",TYPE_VIDEO);
        context.startActivity(intent);
    }

    @Override
    public void startMusicListActivity() {
        Intent intent = new Intent(context, ResourceAlubmListActivity.class);
        intent.putExtra("type",TYPE_MUISC);
        context.startActivity(intent);
    }
    @Override
    public void startConsultListActivity() {
        Intent intent = new Intent(context, ConsultListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void startVideoActivity(String AlbumId) {
        Intent intent = new Intent(context, PlayVideoListActivity.class);
        intent.putExtra("themeId", AlbumId);
        context.startActivity(intent);
    }

    @Override
    public void startMusicActivity(String AlbumId, String resourceId) {
        Intent intent = new Intent(context, MainPlayMusicActivity.class);
        intent.putExtra("themeId", AlbumId);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }

    @Override
    public void startConsultActivity(String resourceId) {
        Intent intent = new Intent(context, ConsultActivity.class);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }



    @Override
    public void startSearchActivity() {
        Intent intent = new Intent(context, SearchResourceActivity.class);
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
                   getRecommendConsult(tsid,6,UserMessage.getInstance(context).getAccount_id());
               }
           }
        } else if (uri.equals(Apiurl.GETBANNERLIST)) {
            if (date!=null){
                Result< List<BannerVo>> data = (Result< List<BannerVo>>) date;
                List<BannerVo> bannerVos = data.getData();
                view.setBannerList(bannerVos);
            }
        }else if (uri.equals(Apiurl.GETRECONSULT)){
            if (date!=null){
                Result<List<ConsultInfoVo>> result = (Result<List<ConsultInfoVo>>) date;
                List<ConsultInfoVo> consultInfoVos  = result.getData();
                view.setRecommendVoConsultList(consultInfoVos);
            }

        }
    }
    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
}
