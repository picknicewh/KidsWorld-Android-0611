package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.school.bean.TaskCommentDetailVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
public class CommentTaskSPresenter implements CommentTaskSContract.Presenter, OkHttpListener {
    private CommentTaskSContract.View view;
    private Context context;

    public CommentTaskSPresenter(CommentTaskSContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getAppraiseDetail(String appraiseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("appraiseId", appraiseId);
        Type type = new TypeToken<Result<TaskCommentDetailVo>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETCOMMENTEDETAIL, params, this);
        view.showLoadingDialog();
    }

    @Override
    public  List<Map<String, Object>> getParentList(List<TaskCommentDetailVo.ParentVo> tagList) {
        List<Map<String, Object>> parentList = new ArrayList<>();
        for (int i = 0; i < tagList.size(); i++) {
            TaskCommentDetailVo.ParentVo tag = tagList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("tag", tag.getParentName());
            map.put("size", tag.getTags().size());
            parentList.add(map);
        }
        return parentList;
    }

    @Override
    public List<String> getChildList(List<TaskCommentDetailVo.ParentVo> tagList) {
        List<String> itemList = new ArrayList<>();
        float size;
        for (int i = 0; i < tagList.size(); i++) {
            List<TaskCommentDetailVo.ParentVo.ChildVo> childTags = tagList.get(i).getTags();
            size = (float) childTags.size() / (float) 3;
            if (size <= 1) {
                for (int k = 0; k < 3; k++) {
                    if (k > childTags.size() - 1) {
                        itemList.add("");
                    } else {
                        itemList.add(childTags.get(k).getTagName());
                    }
                }
            } else {
                for (int k = 0; k < 6; k++) {
                    if (k > childTags.size() - 1) {
                        itemList.add("");
                    } else {
                        itemList.add(childTags.get(k).getTagName());
                    }
                }
            }
        }
        return itemList;
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_GETCOMMENTEDETAIL)) {
            Result<TaskCommentDetailVo> result = (Result<TaskCommentDetailVo>) date;
            TaskCommentDetailVo vo = result.getData();
            view.setDetailInfo(vo);
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
