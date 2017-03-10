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
import net.hongzhang.discovery.modle.ThemeVo;

import java.lang.reflect.Type;
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
public class AlbumAllPresenter implements AlbumAllContract.Presenter, OkHttpListener {
    private Context context;
    private AlbumAllContract.View view;

    public AlbumAllPresenter(Context context, AlbumAllContract.View view) {
        this.context = context;
        this.view = view;

    }

    @Override
    public void getAllAlbumList(int type, int pageSize, int pageNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("pageNumber", pageNumber);
        map.put("pageSize", pageSize);
        map.put("type", type);
        Type mType = new TypeToken<Result<List<ThemeVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.GETHEMELIST, map, this);
        view.showLoadingDialog();
    }

    @Override
    public void starVedioActivity(String themeId) {
        Intent intent = new Intent(context, PlayVideoListActivity.class);
        intent.putExtra("themeId", themeId);
        context.startActivity(intent);
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
        if (date!=null){
            Result<List<ThemeVo>> result = (Result<List<ThemeVo>>) date;
            List<ThemeVo> themeVos = result.getData();
            view.setAllAlbumList(themeVos);
        }
    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
}
