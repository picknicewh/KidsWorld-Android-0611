package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.bean.ActivityInfoVos;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/5
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class HomeInteractionSPresenter implements HomeInteractionSContract.Presenter, OkHttpListener {
    private Context context;
    private HomeInteractionSContract.View view;

    public HomeInteractionSPresenter(Context context, HomeInteractionSContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getPublishActivityList(String tsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        Type type = new TypeToken<Result<List<ActivityInfoVos>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_HOMEINTERACTIVELIST, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void getEndActivityList(String tsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        Type type = new TypeToken<Result<List<ActivityInfoVos>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_ENDHOMEINTERACTIVELIST, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_HOMEINTERACTIVELIST)) {
            Result<List<ActivityInfoVos>> result = (Result<List<ActivityInfoVos>>) date;
            List<ActivityInfoVos> data = result.getData();
            view.setPublishActivityList(data);
        } else if (uri.equals(Apiurl.SCHOOL_ENDHOMEINTERACTIVELIST)) {
            Result<List<ActivityInfoVos>> result = (Result<List<ActivityInfoVos>>) date;
            List<ActivityInfoVos> data = result.getData();
            view.setEndActivityList(data);
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
