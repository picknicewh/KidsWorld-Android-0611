package net.hongzhang.discovery.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.cordova.HMDroidGap;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.network.ServerConfigManager;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.modle.RecommendVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghua on 2016/12/20.
 */
public class AlbumPresenter implements AlbumContract.Presenter, OkHttpListener {
    private Context context;
    private AlbumContract.View view;
    private List<RecommendVo> recommendVoList;
    private static final String url = ServerConfigManager.WEB_IP + "/paradise/index.html";
    public AlbumPresenter(Context context, AlbumContract.View view){
        recommendVoList = new ArrayList<>();
        this.context = context;
        this.view = view;
    }
    @Override
    public void getAlbumList( int type,int pageSize,int pageNumber) {
       Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("pageSize",10);
        params.put("type",type);
        params.put("pageNumber",1);
        params.put("typeid",type);
        Type mtype = new TypeToken<Result<List<RecommendVo>>>(){}.getType();
        OkHttps.sendPost(mtype, Apiurl.GETALUBMLIST,params,this);
        view.showLoadingDialog();
    }
    @Override
    public void starVedioActivity(String themeId) {
        Intent intent = new Intent(context, HMDroidGap.class);
        intent.putExtra("loadUrl", url + "?tsId=" + UserMessage.getInstance(context).getTsId() + "#/videoPlay?themeid=" + themeId );
        context.startActivity(intent);
    }
    @Override
    public void startMusicActivity(String themeId, String resourceId) {
        Intent intent = new Intent();
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow", "net.hongzhang.discovery.MainPlayActivity");
        intent.setComponent(componetName);
        intent.putExtra("themeId", themeId);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }

    @Override
    public void starConsultActivity(String resourceId) {
        Intent intent = new Intent(context ,HMDroidGap.class);
        intent.putExtra("loadUrl", url + "?tsId=" + UserMessage.getInstance(context).getTsId() + "#/eduInformation_Detail?resourceid=" + resourceId);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.GETALUBMLIST)){
            if (date!=null){
                Result<List<RecommendVo>> data  = (Result<List<RecommendVo>>) date;
                List<RecommendVo> recommendVos = data.getData();
                if (recommendVos.size()>0&&recommendVos!=null){
                    recommendVoList.addAll(recommendVos);
                    view.setAlbum(recommendVoList);
                }
                view.setResourceSize(recommendVos.size());
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
}
