package net.hongzhang.discovery.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.activity.MainPlayMusicActivity;
import net.hongzhang.discovery.activity.PlayVideoListActivity;
import net.hongzhang.discovery.modle.CompilationVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/3/7
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ThemeVoListPresenter implements ThemeVoListContract.Presenter, OkHttpListener {
    private Context context;
    private ThemeVoListContract.View view;
    private  List<CompilationVo> compilationVoList;
    public ThemeVoListPresenter(Context context, ThemeVoListContract.View view) {
        this.context = context;
        this.view = view;
        compilationVoList  =new ArrayList<>();

    }

    @Override
    public void starVedioActivity(String themeId,String resourceId) {
        Intent intent = new Intent(context, PlayVideoListActivity.class);
        intent.putExtra("themeId", themeId);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }

    @Override
    public void getThemVoList(String themeId, int pageSize, int pageNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("pageNumber", pageNumber);
        map.put("pageSize", pageSize);
        map.put("themeId", themeId);
        Type mType = new TypeToken<Result<List<CompilationVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.GETHEMELISTBYID, map, this);
        view.showLoadingDialog();
    }

    @Override
    public void startMusicActivity(String themeId, String resourceId) {
        Intent intent = new Intent(context, MainPlayMusicActivity.class);
        intent.putExtra("themeId", themeId);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (date != null) {
            Result<List<CompilationVo>> result = (Result<List<CompilationVo>>) date;
            List<CompilationVo> compilationVos = result.getData();
            compilationVoList.addAll(compilationVos);
            view.setThemeVoList(compilationVoList);
            view.setThemeVoSize(compilationVos.size());
        }
    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
}
