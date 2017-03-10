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
import net.hongzhang.discovery.activity.MainPlayMusicActivity;
import net.hongzhang.discovery.activity.PlayVideoListActivity;
import net.hongzhang.discovery.modle.CompilationVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghua on 2016/12/20.
 */
public class AlbumSelectPresenter implements AlbumSelectContract.Presenter, OkHttpListener {
    private Context context;
    private AlbumSelectContract.View view;
    private List<CompilationVo> compilationVoList;

    public AlbumSelectPresenter(Context context, AlbumSelectContract.View view){
        compilationVoList = new ArrayList<>();
        this.context = context;
        this.view = view;
    }
    @Override
    public void getAlbumList( int type,int pageSize,int pageNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("pageNumber", pageNumber);
        map.put("pageSize",pageSize);
        map.put("type", type);
        Type mType = new TypeToken<Result<List<CompilationVo>>>() {}.getType();
        OkHttps.sendPost(mType, Apiurl.GETRECOMMENDLIST, map, this);
        view.showLoadingDialog();
    }
    @Override
    public void starVedioActivity(String themeId) {
        Intent intent = new Intent(context, PlayVideoListActivity.class);
        intent.putExtra("themeId", themeId);
        context.startActivity(intent);
    }
    @Override
    public void startMusicActivity(String themeId, String resourceId) {
        Intent intent = new Intent(context, MainPlayMusicActivity.class);
        intent.putExtra("themeId", themeId);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }


    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.GETRECOMMENDLIST)){
            if (date!=null){
                Result<List<CompilationVo>> data  = (Result<List<CompilationVo>>) date;
                List<CompilationVo> compilationVos = data.getData();
                if (compilationVos.size()>0&&compilationVos!=null){
                    compilationVoList.addAll(compilationVos);
                    view.setAlbum(compilationVoList);

                }
                view.setResourceSize(compilationVos.size());
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
}
