package net.hongzhang.discovery.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.discovery.modle.ConsultInfoVo;
import net.hongzhang.discovery.modle.ResourceVos;

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
    private List<ConsultInfoVo> consultInfoVoList;

    public SearchConsultPresenter(Context context, SearchConsultContract.View view) {
        this.context = context;
        this.view = view;
        consultInfoVoList = new ArrayList<>();
    }

    @Override
    public void getSearchResourceList(String tsId, int type, int pageSize, int pageNumber, String account_id, String tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        map.put("type", type);
        map.put("tag", tag);
        map.put("account_id", account_id);
        Type mType = new TypeToken<Result<ResourceVos>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.SERACHRESOURCE, map, this);
        view.showLoadingDialog();
    }

    @Override
    public void startConsultActivity(String resourceId) {
        Intent intent = new Intent();
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
                List<ConsultInfoVo> consultInfoVos = resourceVos.getResourceManageList();
                consultInfoVoList.addAll(consultInfoVos);
                view.setConsultList(consultInfoVos);
                view.setConsultInfoSize(consultInfoVos.size());
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
}
