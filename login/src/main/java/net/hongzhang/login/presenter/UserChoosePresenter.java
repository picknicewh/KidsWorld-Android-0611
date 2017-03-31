package net.hongzhang.login.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.login.mode.CharacterSeleteVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/3/27
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class UserChoosePresenter implements UserChooseContract.Presenter, OkHttpListener {
    private Context context;
    private UserChooseContract.View view;

    public UserChoosePresenter(Context context, UserChooseContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getIsGologin(String accountId, String password, String sign) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("password", password);
        params.put("sign", sign);
        Type type = new TypeToken<Result<List<CharacterSeleteVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.APPLOGIN, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void selectUserSubmit(String tsId, String sign) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("sign", sign);
        params.put("requestSource", "android");
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SELECTUSER, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (Apiurl.APPLOGIN.equals(uri)) {
            Result<List<CharacterSeleteVo>> result = (Result<List<CharacterSeleteVo>>) date;
            List<CharacterSeleteVo> characterSeleteVoList = result.getData();
            view.setCharacterVoList(characterSeleteVoList);
        } else if (Apiurl.SELECTUSER.equals(uri)) {
            Result<String> data = (Result<String>) date;
            String result = data.getData();
            if (result.equals("成功")) {
                view.setIsChoose(true);
            } else {
                view.setIsChoose(false);
            }
            G.showToast(context,"身份选择失败，请重新选择");
        }
    }
    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        G.showToast(context, error);
    }
}
