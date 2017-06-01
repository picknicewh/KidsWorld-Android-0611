package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.school.bean.TaskDetailInfoVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：学生作品详情
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitActiveDetailPresenter implements SubmitActiveDetailContract.Presenter, OkHttpListener {
    private SubmitActiveDetailContract.View view;
    private Context context;
    public SubmitActiveDetailPresenter(Context context, SubmitActiveDetailContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getActiveDetail(String activityWorksId) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityWorksId", activityWorksId);
        Type type = new TypeToken<Result<TaskDetailInfoVo>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETTEAKDETAIL, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_GETTEAKDETAIL)){
            Result<TaskDetailInfoVo> result = (Result<TaskDetailInfoVo>) date;
            TaskDetailInfoVo infoVo = result.getData();
            view.setActiveDetailInfo(infoVo);
        }
    }

    @Override
    public void onError(String uri, Result result) {
       view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result,context);
    }
}
