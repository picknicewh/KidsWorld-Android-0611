package net.hongzhang.discovery.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.activity.ConsultActivity;
import net.hongzhang.baselibrary.mode.ResourceVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
public class ConsultFragmentPresenter implements ConsultFragmentContract.Presenter, OkHttpListener {

    private Context context;
    private ConsultFragmentContract.View view;
    private List<ResourceVo> resourceVoList;

    public ConsultFragmentPresenter(Context context, ConsultFragmentContract.View view) {
        this.context = context;
        this.view = view;
        resourceVoList = new ArrayList<>();
    }

    @Override
    public void getConsultList(int pageNumber, int pageSize, String themeId, String account_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("pageNumber", pageNumber);
        map.put("pageSize", pageSize);
        map.put("themeId", themeId);
        map.put("type", 4);
        map.put("account_id", account_id);
        Type mType = new TypeToken<Result<List<ResourceVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.GETRECONSULT, map, this);
        view.showLoadingDialog();
    }


    @Override
    public void starConsultActivity(String resourceId) {
        Intent intent = new Intent(context ,ConsultActivity.class);
        intent.putExtra("resourceId",resourceId);
        context.startActivity(intent);
    }
    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.GETRECONSULT)) {
            if (date != null) {
                Result<List<ResourceVo>> result = (Result<List<ResourceVo>>) date;
                List<ResourceVo> resourceVos = result.getData();
                resourceVoList.addAll(resourceVos);
                view.setConsultList(resourceVoList);
                Log.i("TAG",resourceVoList.size()+"-------------------------------------");
                view.setConsultInfoSize(resourceVos.size());
            }
        }

    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
}
