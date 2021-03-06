package net.hongzhang.discovery.presenter;

import android.content.Context;
import android.content.Intent;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.discovery.activity.ConsultActivity;
import net.hongzhang.discovery.modle.ResourceVos;
import net.hongzhang.discovery.modle.SearchKeyVo;
import net.hongzhang.discovery.util.SearchHistoryDb;
import net.hongzhang.discovery.util.SearchHistoryDbHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/3/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SearchConsultPresenter implements SearchConsultContract.Presenter, OkHttpListener {
    private Context context;
    private SearchConsultContract.View view;
    private List<ResourceVo> resourceVoList;
    private SearchHistoryDb db;
    private SearchHistoryDbHelper helper;

    public SearchConsultPresenter(Context context, SearchConsultContract.View view) {
        this.context = context;
        this.view = view;
        resourceVoList = new ArrayList<>();
        db = new SearchHistoryDb(context);
        helper = SearchHistoryDbHelper.getinstance();
        getSearchHistoryList();
    }

    @Override
    public void getSearchResourceList(String tsId, int pageSize, int pageNumber, String account_id, String tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        map.put("type", 3);
        map.put("tag", tag);
        map.put("account_id", account_id);
        Type mType = new TypeToken<Result<ResourceVos>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.SERACHRESOURCE, map, this, 2, "search");
        view.showLoadingDialog();
    }

    @Override
    public void startConsultActivity(String resourceId) {
        Intent intent = new Intent(context, ConsultActivity.class);
        intent.putExtra("resourceId", resourceId);
        G.log("-----------------"+resourceId);
        context.startActivity(intent);
    }

    @Override
    public void getSearchHistoryList() {
        List<SearchKeyVo> searchKeyVos = helper.getKeyList(db.getReadableDatabase(), SearchHistoryDb.TABLENAME3);
        view.setSearchHistoryList(searchKeyVos);
    }

    @Override
    public void saveSearchKey(String key,String tsId,String targetName,String targetId) {
        Map<String,Object> params = new HashMap<>();
        params.put("tsId",tsId);
        params.put("tag",key);
        params.put("targetName",targetName);
        params.put("type",1);//type=1资源 type=2专辑
        params.put("targetId",targetId);
        Type type =  new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,Apiurl.SAVESERACHRE,params,this);
    }

    @Override
    public void insertKey(String tag) {
        helper.insert(db.getWritableDatabase(), tag, SearchHistoryDb.TABLENAME3);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (Apiurl.SERACHRESOURCE.equals(uri)) {
            if (date != null) {
                Result<ResourceVos> result = (Result<ResourceVos>) date;
                ResourceVos resourceVos = result.getData();
                List<ResourceVo> resourceVos1 = resourceVos.getResourceManageList();
               // resourceVoList.addAll(resourceVos1);
                view.setConsultList(resourceVos1);
                view.setConsultInfoSize(resourceVos1.size());
            }
        }else if (Apiurl.SAVESERACHRE.equals(uri)){
            Result<String> result = (Result<String>) date;
            G.showToast(context,result.getData());
        }
    }


    @Override
    public void onError(String uri, Result error) {
         view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(error,context);
    }
}
