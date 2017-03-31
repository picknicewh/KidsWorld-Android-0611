package net.hongzhang.bbhow.share;

import android.content.Intent;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.status.mode.DynamicVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/3/28
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SharePresenter implements ShareContract.Presenter, OkHttpListener {
    private ShareActivity activity;
    private ShareContract.View view;
    private Intent intent;

    public SharePresenter(ShareActivity activity, ShareContract.View view) {
        this.activity = activity;
         this.view = view;
         intent = activity.getIntent();
    }

    @Override
    public void getClassList(String tsId) {
        if (G.isEmteny(tsId)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        Type type = new TypeToken<Result<List<DynamicVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.DYNAMICHEAD, map, this);
        view.showLoadingDialog();
    }



    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (Apiurl.DYNAMICHEAD.equals(uri)) {
            Result<List<DynamicVo>> data = (Result<List<DynamicVo>>) date;
            List<DynamicVo> dynamicList = data.getData();
            if (dynamicList.size() > 0 && dynamicList != null) {
                view.setClassList(dynamicList);
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        G.showToast(activity, error);
    }
}
