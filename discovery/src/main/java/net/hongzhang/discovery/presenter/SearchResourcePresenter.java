package net.hongzhang.discovery.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.discovery.activity.MainPlayMusicActivity;
import net.hongzhang.discovery.activity.PlayVideoListActivity;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.modle.ResourceVo;
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
    public void startMusicActivity(String AlbumId, String resourceId) {
        Intent intent = new Intent(context, MainPlayMusicActivity.class);
        intent.putExtra("themeId", AlbumId);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
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
                    if (resourceVos1 != null && resourceVos1.size() > 0) {
                        if (flag == 1) {
                            resourceVoList.clear();
                        }
                        resourceVoList.addAll(resourceVos1);
                        view.setResourceList(resourceVoList);
                    }
                } else if (resourceVos1 != null && resourceVos1.size() > 0) {
                    if (flag == 1) {
                        resourceVoList.clear();
                    }
                    resourceVoList.addAll(resourceVos1);
                    view.setResourceList(resourceVoList);
                  /*  if (compilationVos != null && compilationVos.size() > 0) {
                        if (flag == 1) {
                            compilationVoList.clear();
                        }
                        compilationVoList.addAll(compilationVos);
                        view.setCompilationVoList(compilationVoList);
                    }*/
                    view.setResourceSize((resourceVos1.size() + compilationVos.size()));
                }

            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
}
