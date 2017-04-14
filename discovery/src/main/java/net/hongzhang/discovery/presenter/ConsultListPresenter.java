package net.hongzhang.discovery.presenter;

import android.content.Context;
import android.content.Intent;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.activity.ConsultActivity;
import net.hongzhang.discovery.modle.ThemeVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/3/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ConsultListPresenter implements ConsultListContract.Presenter, OkHttpListener {
    private Context context;
    private ConsultListContract.View view;
    public ConsultListPresenter(Context context, ConsultListContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getThemeList(int type, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("pageNumber", pageNumber);
        map.put("pageSize", pageSize);
        map.put("type", type);
        Type mType = new TypeToken<Result<List<ThemeVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.GETHEMELIST, map, this);
      //  view.showLoadingDialog();
    }


    @Override
    public void starConsultActivity(String resourceId) {
        Intent intent = new Intent(context ,ConsultActivity.class);
        intent.putExtra("resourceId",resourceId);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(String uri, Object date) {
       // view.stopLoadingDialog();
         if (uri.equals(Apiurl.GETHEMELIST)) {
            Result<List<ThemeVo>> result = (Result<List<ThemeVo>>) date;
            List<ThemeVo> themeVos = result.getData();
            view.setThemeList(themeVos);
        }

    }

    @Override
    public void onError(String uri, Result error) {
        // view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(error,context);
    }
}
