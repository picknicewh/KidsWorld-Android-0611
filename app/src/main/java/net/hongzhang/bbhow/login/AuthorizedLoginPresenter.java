package net.hongzhang.bbhow.login;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.login.util.SignUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/31
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class AuthorizedLoginPresenter implements AuthorizedLoginContract.Presenter, OkHttpListener {
    private Context context;
    private AuthorizedLoginContract.View view;
    public AuthorizedLoginPresenter(Context context,AuthorizedLoginContract.View view){
      this.context = context;
        this.view = view;
    }
    @Override
    public void authorizedLogin(String tsId, String keyNumber) {
        if (G.isEmteny(tsId)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("keyNumber",keyNumber);
        map.put("sign", SignUtil.getSign(Apiurl.AUTHORIZATION));
        map.put("requestSource","100003");
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.AUTHORIZATION, map, this);
        view.showLoadingDialog();
    }
    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (Apiurl.DYNAMICHEAD.equals(uri)) {
            Result<String> data = (Result<String>) date;
            view.setToken(data.getData());
        }
    }
    @Override
    public void onError(String uri, Result error) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(error,context);
    }
}
