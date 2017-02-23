package net.hongzhang.discovery.util;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.modle.RecommendVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghua on 2017/1/3.
 */
public class PlayVideoDetailPresenter implements PlayVideoDetailContract.Presenter, OkHttpListener {
    private Activity context;
    private PlayVideoDetailContract.View view;
    private List<ResourceVo> resourceVos;
    private int position=0;
    private String resourceId;
    private String tsId;
    public PlayVideoDetailPresenter(Activity context,  PlayVideoDetailContract.View view, String resourceId){
        this.context = context;
        this.view = view;
        this.resourceId =resourceId;
        tsId = UserMessage.getInstance(context).getTsId();
        getRecommendList(tsId,4,1);

        getVideoList(tsId, String.valueOf("121"));

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

}
