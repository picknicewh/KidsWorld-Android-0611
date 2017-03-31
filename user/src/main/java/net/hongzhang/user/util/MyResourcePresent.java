package net.hongzhang.user.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.network.ServerConfigManager;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.user.mode.CompilationsVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/12/10
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyResourcePresent implements ResourceContract.Presenter, OkHttpListener {
    public Context context;
    private ResourceContract.View view;
    private List<CompilationsVo> compilationVoList;
    private List<ResourceVo> resourceVoList;
    private int type;
    private static final String url = ServerConfigManager.WEB_IP + "/paradise/index.html";
   private String resourceId;
    public MyResourcePresent(Context context, ResourceContract.View view, int source, int type) {
        this.context = context;
        this.view = view;
        this.type = type;
        Log.i("sssssssssss", type + "=-====0");
        if (type == 3) {
            resourceVoList = new ArrayList<>();
            if (source == 0) {
                getCollectResourceList(1, 10, type);
            } else if (source == 1) {
                getPlayRecordList(1, 10, type);
            }
        } else {
            if (source == 0) {
                compilationVoList = new ArrayList<>();
                getCollectResourceList(1, 10, type);
            } else if (source == 1) {
                resourceVoList = new ArrayList<>();
                getPlayRecordList(1, 10, type);
            }
        }


    }
    /**
     * 根据专辑id获取专辑中的资源列表
     *
     * @param tsId    角色id
     * @param themeId 专辑id
     */
    @Override
    public void getSongList(String tsId, String themeId,String resourceId) {
        this.resourceId = resourceId;
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("pageSize", 999);
        map.put("pageNumber", 1);
        map.put("albumId", themeId);
        map.put("account_id", UserMessage.getInstance(context).getAccount_id());
        Type mType = new TypeToken<Result<List<ResourceVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.USER_GETTHENELIST, map, this);
        view.showLoadingDialog();
    }
    @Override
    public void getCollectResourceList(int pageNumber, int pageSize, int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("pageSize", pageSize);
        params.put("pageNumber", pageNumber);
        params.put("account_id", UserMessage.getInstance(context).getAccount_id());
        Type mType;
        if (type == 3) {
            mType = new TypeToken<Result<List<ResourceVo>>>() {
            }.getType();
        } else {
            mType = new TypeToken<Result<List<CompilationsVo>>>() {
            }.getType();
        }
        OkHttps.sendPost(mType, Apiurl.GETATTENTIONLIST, params, this, 2, "resource");
        view.showLoadingDialog();
    }

    @Override
    public void getPlayRecordList(int pageNumber, int pageSize, int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("pageSize", pageSize);
        params.put("pageNumber", pageNumber);
        Type mType = new TypeToken<Result<List<ResourceVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.GETPLAYRECORDING, params, this, 2, "resource");
        view.showLoadingDialog();
    }

    @Override
    public void starVedioActivity(String themeId) {
//        Intent intent = new Intent(context, HMDroidGap.class);
//        intent.putExtra("loadUrl", url + "?tsId=" + UserMessage.getInstance(context).getTsId() + "#/videoPlay?themeid=" + themeId );
//        context.startActivity(intent);
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow", "net.hongzhang.discovery.activity.PlayVideoListActivity");
        Intent intent = new Intent();
        intent.setComponent(componetName);
        intent.putExtra("themeId", themeId);
        context.startActivity(intent);
    }

    @Override
    public void startMusicActivity(List<ResourceVo> resourceVos, String resourceId) {
        Intent intent = new Intent();
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow", "net.hongzhang.discovery.activity.MainPlayMusicActivity");
        intent.setComponent(componetName);
        intent.putParcelableArrayListExtra("resourceVos", (ArrayList<? extends Parcelable>) resourceVos);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }

    @Override
    public void starConsultActivity(String resourceId) {
//        Intent intent = new Intent(context ,HMDroidGap.class);
//        intent.putExtra("loadUrl", url + "?tsId=" + UserMessage.getInstance(context).getTsId() + "#/eduInformation_Detail?resourceid=" + resourceId);
//        context.startActivity(intent);

        ComponentName componentName = new ComponentName("net.hongzhang.bbhow","net.hongzhang.discovery.activity.ConsultActivity");
        Intent intent=new Intent();
        intent.setComponent(componentName);
        intent.putExtra("resourceId", resourceId);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (type == 1 || type == 2) {
            if (Apiurl.GETATTENTIONLIST.equals(uri)) {
                if (date != null) {
                    Result<List<CompilationsVo>> data = (Result<List<CompilationsVo>>) date;
                    List<CompilationsVo> compilationsVos = data.getData();
                    if (compilationsVos.size() > 0 && compilationsVos != null) {
                        compilationVoList.addAll(compilationsVos);
                        view.setResourceVoList(null, compilationVoList);
                    }
                    view.setResourceSize(compilationsVos.size());
                }
            } else if (Apiurl.GETPLAYRECORDING.equals(uri)) {
                if (date != null) {
                    Result<List<ResourceVo>> data = (Result<List<ResourceVo>>) date;
                    List<ResourceVo> resourceVos = data.getData();
                    if (resourceVos.size() > 0 && resourceVos != null) {
                        resourceVoList.addAll(resourceVos);
                        view.setResourceVoList(resourceVoList, null);
                    }
                    view.setResourceSize(resourceVos.size());
                }
            }else if (uri.equals(Apiurl.USER_GETTHENELIST)) {
                if (date!=null){
                    Result<List<ResourceVo>> result = (Result<List<ResourceVo>>) date;
                    List<ResourceVo> resourceVoList = result.getData();
                    startMusicActivity(resourceVoList,resourceId);
                }
            }
        } else if (type == 3) {
            if (date != null) {
                Result<List<ResourceVo>> data = (Result<List<ResourceVo>>) date;
                List<ResourceVo> resourceVos = data.getData();
                if (resourceVos.size() > 0 && resourceVos != null) {
                    resourceVoList.addAll(resourceVos);
                    view.setResourceVoList(resourceVoList, null);
                }
                view.setResourceSize(resourceVos.size());
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        view.stopLoadingDialog();
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
}
