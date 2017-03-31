package net.hongzhang.login.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.activity.UpdateMessageActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.FormValidation;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.PromptPopWindow;
import net.hongzhang.login.mode.CharacterSeleteVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/3/22
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LoginPresenter implements LoginContact.Presenter, OkHttpListener {
    private Activity context;
    private LoginContact.View view;
    public LoginPresenter(Activity context, LoginContact.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getIsGologin(CheckBox checkBox, String accountId, String password, String sign) {
        if (!checkBox.isChecked()) {
            G.initDisplaySize(context);
            PromptPopWindow promptPopWindow = new PromptPopWindow(context, "请同意服务条款");
            promptPopWindow.showAtLocation(checkBox, Gravity.NO_GRAVITY, (G.size.W - promptPopWindow.getWidth()) / 2, (int) (G.size.H * 0.2));
            return;
        }
        if (G.isEmteny(accountId) || G.isEmteny(password)) {
            G.showToast(context, "账号密码不能为空");
            return;
        }
        if (!FormValidation.isMobileNO(accountId)) {
            G.showToast(context, "您输入的账号不规范");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("password", password);
        params.put("sign", sign);
        params.put("requestSource","android");
        Type type = new TypeToken<Result<List<CharacterSeleteVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.APPLOGIN, params, this);
        view.showLoadingDialog();
    }
    @Override
    public void selectUserSubmit(String tsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SELECTUSER, params, this);
        view.showLoadingDialog();
    }
    @Override
    public void goUserAgreementActivity(String url, int source) {
        Intent intent = new Intent();
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow",
                "net.hongzhang.user.activity.WebViewActivity");
        intent.setComponent(componetName);
        intent.putExtra("source", source);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public void goUpdateMessageActivity(String phoneNumber) {
        if (!FormValidation.isMobileNO(phoneNumber)) {
            G.showToast(context, "请先输入正确的手机号码");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("type", "pw");
        intent.putExtra("phoneNumber", phoneNumber);
        intent.setClass(context, UpdateMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (Apiurl.APPLOGIN.equals(uri)) {
            Result<List<CharacterSeleteVo>> result = (Result<List<CharacterSeleteVo>>) date;
            List<CharacterSeleteVo> characterSeleteVoList = result.getData();
            view.setCharacterVoList(characterSeleteVoList);
            view.setIsOneRole(characterSeleteVoList.size()==1);
        }
    }
    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
}
