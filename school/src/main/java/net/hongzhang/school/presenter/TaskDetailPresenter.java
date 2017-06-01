package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.school.bean.ActivityDetailVo;

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
public class TaskDetailPresenter implements TaskDetailContract.Presenter, OkHttpListener {
    private Context context;
    private TaskDetailContract.View view;

    public TaskDetailPresenter(Context context, TaskDetailContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getTaskDetail(String activityId) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityId", activityId);
        Type type = new TypeToken<Result<ActivityDetailVo>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETACTIVEDETAIL, params, this);
        view.showLoadingDialog();
    }
    /**
     * 获取选择的维度
     */
    @Override
    public String getChecks(List<String> values) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < values.size(); i++) {
            if (i == values.size() - 1) {
                buffer.append(values.get(i));
            } else {
                buffer.append(values.get(i)).append(" | ");
            }
        }
        return buffer.toString();
    }
    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_GETACTIVEDETAIL)) {
            Result<ActivityDetailVo> result = (Result<ActivityDetailVo>) date;
            view.setDetailInfo(result.getData());
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
