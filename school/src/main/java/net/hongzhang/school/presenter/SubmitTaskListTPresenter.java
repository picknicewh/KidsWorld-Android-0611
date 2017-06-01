package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.school.bean.TaskInfoVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitTaskListTPresenter implements SubmitTaskListTContract.Presenter, OkHttpListener {
    private Context context;
    private SubmitTaskListTContract.View view;

    public SubmitTaskListTPresenter(Context context, SubmitTaskListTContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getTaskList(String activityId, String tsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityId", activityId);
        params.put("tsId", tsId);
        Type mType = new TypeToken<Result<List<TaskInfoVo>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.SCHOOL_COMMENTACTIVELIST, params, this);
        view.showLoadingDialog();
    }
    @Override
    public boolean isAllAppraise(List<TaskInfoVo> taskListInfoVos) {
        boolean isAppraise=false;
        for (TaskInfoVo taskInfoVo : taskListInfoVos) {
            if (taskInfoVo.getAppraiseId()==null){
                isAppraise = false;
                return isAppraise;
            }else {
                isAppraise = true;
            }
        }
        return isAppraise;
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_COMMENTACTIVELIST)) {
            Result<List<TaskInfoVo>> result = (Result<List<TaskInfoVo>>) date;
            List<TaskInfoVo> taskListInfoVos = result.getData();
            view.setTaskList(taskListInfoVos);
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
