package net.hongzhang.discovery.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.activity.MainPlayMusicActivity;
import net.hongzhang.discovery.activity.PlayVideoListActivity;
import net.hongzhang.discovery.modle.CompilationVo;
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
public class SearchResourcePresenter implements SearchResourceContract.Presenter, OkHttpListener {
    private Context context;
    private SearchResourceContract.View view;
    private List<CompilationVo> compilationVoList;
    private List<ResourceVo> resourceVoList;
    private SearchHistoryDb db;
    private SearchHistoryDbHelper helper;
    /**
     * flag=1表示重新获取新的搜索词列表，flag=2表示同一搜索词加载更多
     */
    private int flag;
    /**
     * 搜索资源所在资源的位置
     */
   private String resourceId;
    private String albumId;
    public SearchResourcePresenter(Context context, SearchResourceContract.View view) {
        this.context = context;
        this.view = view;
        compilationVoList = new ArrayList<>();
        resourceVoList = new ArrayList<>();
        db = new SearchHistoryDb(context);
        helper = SearchHistoryDbHelper.getinstance();
    }

    @Override
    public void getSearchResourceList(String tsId, int type, int pageSize, int pageNumber, String account_id, String tag, int flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        map.put("type", type);
        map.put("tag", tag);
        map.put("account_id", account_id);
        this.flag = flag;
        Type mType = new TypeToken<Result<ResourceVos>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.SERACHRESOURCE, map, this, 2, "search" + type);
        view.showLoadingDialog();
        view.setloadMoreVis(false);
    }
    /**
     * 根据专辑id获取专辑中的资源列表
     *
     * @param tsId    角色id
     * @param themeId 专辑id
     */
    @Override
    public void getSongList(String tsId, String themeId,String resourceId) {
        this.albumId = themeId;
        this.resourceId = resourceId;
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("pageSize", 999);
        map.put("pageNumber", 1);
        map.put("albumId", themeId);
        map.put("account_id", UserMessage.getInstance(context).getAccount_id());
        Type mType = new TypeToken<Result<List<ResourceVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.USER_GETTHENELIST, map, this);
    }
    @Override
    public void getSearchHistoryList(int type) {
        List<SearchKeyVo> searchKeyVos = new ArrayList<>();
        if (type == MainRecommendPresenter.TYPE_MUISC) {
            searchKeyVos = helper.getKeyList(db.getReadableDatabase(), SearchHistoryDb.TABLENAME1);
        } else if (type == MainRecommendPresenter.TYPE_VIDEO) {
            searchKeyVos = helper.getKeyList(db.getReadableDatabase(), SearchHistoryDb.TABLENAME2);
        }
        view.setSearchHistoryList(searchKeyVos);
    }
    @Override
    public void saveSearchKey(String key,int type,String tsId,String targetName,String targetId) {
        Map<String,Object> params = new HashMap<>();
        params.put("tsId",tsId);
        params.put("tag",key);
        params.put("targetName",targetName);
        params.put("type",type);//type=1资源 type=2专辑
        params.put("targetId",targetId);
        Type mType =  new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mType,Apiurl.SAVESERACHRE,params,this);
    }


    @Override
    public void insertKey(int type, String tag) {
       // Log.i("RRRRRRRR","tag========="+tag+"type========"+type);
        if (type == MainRecommendPresenter.TYPE_MUISC) {
            helper.insert(db.getWritableDatabase(), tag, SearchHistoryDb.TABLENAME1);
        } else if (type == MainRecommendPresenter.TYPE_VIDEO) {
            helper.insert(db.getWritableDatabase(), tag, SearchHistoryDb.TABLENAME2);
        }
    }


    @Override
    public void startVideoActivity(String AlbumId, String resourceId) {
        Intent intent = new Intent(context, PlayVideoListActivity.class);
        intent.putExtra("themeId", AlbumId);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);

    }

    @Override
    public void startMusicActivity(List<ResourceVo> resourceVos,String resourceId) {
        Intent intent = new Intent(context, MainPlayMusicActivity.class);
     //  intent.putExtra("themeId", AlbumId);
        intent.putExtra("resourceId", resourceId);
        intent.putParcelableArrayListExtra("resourceVos", (ArrayList<? extends Parcelable>) resourceVos);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        view.setloadMoreVis(true);
        if (Apiurl.SERACHRESOURCE.equals(uri)) {
            if (date != null) {
                Result<ResourceVos> result = (Result<ResourceVos>) date;
                ResourceVos resourceVos = result.getData();
                List<CompilationVo> compilationVos = resourceVos.getAlbumManageList();
                List<ResourceVo> resourceVos1 = resourceVos.getResourceManageList();
                if (compilationVos != null && compilationVos.size() > 0) {
                    if (flag == 1) {
                        compilationVoList.clear();
                    }
                    compilationVoList.addAll(compilationVos);
                    view.setCompilationVoList(compilationVoList);
                }

                if (resourceVos1 != null && resourceVos1.size() > 0) {
                    if (flag == 1) {
                        resourceVoList.clear();
                    }
                    resourceVoList.addAll(resourceVos1);
                    view.setResourceList(resourceVoList);
                   /* if (compilationVos != null && compilationVos.size() > 0) {
                        if (flag == 1) {
                            compilationVoList.clear();
                        }
                        compilationVoList.addAll(compilationVos);
                        view.setCompilationVoList(compilationVoList);
                    }*/
                }
              //  view.setResourceList(resourceVos1);
                view.setResourceSize((resourceVos1.size()));
            }
        }else if (uri.equals(Apiurl.USER_GETTHENELIST)) {
            if (date!=null){
                Result<List<ResourceVo>> result = (Result<List<ResourceVo>>) date;
                List<ResourceVo> resourceVoList = result.getData();
                G.log("zzzzzzzzzzzzzz---++---"+resourceId);
                startMusicActivity(resourceVoList,resourceId);
            }
        }else if (uri.equals(Apiurl.SAVESERACHRE)){
            Result<String> result = (Result<String>) date;
            String resultData = result.getData();
            G.log(resultData);
            //G.showToast(context,resultData);
        }
    }

    @Override
    public void onError(String uri, Result error) {
        view.stopLoadingDialog();
        view.setloadMoreVis(true);
        DetaiCodeUtil.errorDetail(error,context);
    }
}
