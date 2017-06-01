package net.hongzhang.school.presenter;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.user.util.BitmapCache;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：学生提交活动作业
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitTaskSPresenter implements SubmitTaskSContract.Presenter, OkHttpListener {
    private Activity context;
    private SubmitTaskSContract.View view;

    public SubmitTaskSPresenter(Activity context, SubmitTaskSContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void submitTask(String activityId, String tsId, String content, int publicity, List<String> imageList) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityId", activityId);
        params.put("tsId", tsId);
        params.put("content", content);
        params.put("publicity", publicity);
        Type type = new TypeToken<Result<String>>() {}.getType();
        List<String> imageFiles = new ArrayList<>();
        String videoPath = "";
        for (int i = 0; i < imageList.size(); i++) {
            if (i != imageList.size()-1) {
                imageFiles.add(imageList.get(i));
            } else {
                videoPath = imageList.get(i);
            }
        }
        List<File> files = BitmapCache.getFileList(imageFiles);
        File file = new File(videoPath);
        files.add(file);
        OkHttps.sendPost(type, Apiurl.SCHOOL_COMMENTACTIVE, params, files, this);
        view.showLoadingDialog();
    }

    @Override
    public List<String> getImageList(List<String> itemList, String videoPath) {
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.size()-1 != i) {
                imageList.add(itemList.get(i));
            } else {
                imageList.add(videoPath);
            }
        }
        return imageList;
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_COMMENTACTIVE)) {
            Result<String> result = (Result<String>) date;
            G.showToast(context, result.getData());
            context.finish();
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
