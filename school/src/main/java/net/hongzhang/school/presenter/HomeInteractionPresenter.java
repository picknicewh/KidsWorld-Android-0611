package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.activity.HomeInteractionActivityT;
import net.hongzhang.school.bean.ActivityInfoVo;
import net.hongzhang.school.bean.ActivityInfoVos;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/3
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class HomeInteractionPresenter implements HomeInteractionContract.Presenter, OkHttpListener {
    private Context context;
    private HomeInteractionContract.View view;

    public HomeInteractionPresenter(Context context, HomeInteractionContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getActivityList(String tsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        Type type = new TypeToken<Result<List<ActivityInfoVos>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_THOMEINTRRACTION, params, this);
        view.showLoadingDialog();
    }
    @Override
     public int getStatus( ActivityInfoVo activityInfoVo ){
       int status;
       long currentTime= System.currentTimeMillis();
       if (activityInfoVo.getPostTime()<=currentTime){
           if (activityInfoVo.getDeadline()<=currentTime){
               status = HomeInteractionActivityT.DONE;
           }else {
               status = HomeInteractionActivityT.DOING;
           }
       }else{
           status = HomeInteractionActivityT.NOBEGIN;
       }
       return status;
   }
    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_THOMEINTRRACTION)) {
            Result<List<ActivityInfoVos>> result = (Result<List<ActivityInfoVos>>) date;
            List<ActivityInfoVos> data = result.getData();
            view.setActivityList(data);
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
