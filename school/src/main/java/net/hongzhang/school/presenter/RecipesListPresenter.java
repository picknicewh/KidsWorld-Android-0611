package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.ClassVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.school.bean.RecipesVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/6/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class RecipesListPresenter implements RecipesListContract.Presenter, OkHttpListener {
    private Context context;
    private RecipesListContract.View view;
    public RecipesListPresenter(Context context,RecipesListContract.View view){
        this.context = context;
        this.view = view;
    }

    @Override
    public void getClassList(String tsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId",tsId);
        Type type = new TypeToken<Result<List<ClassVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETUSERCLASS, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void getRecipesList(String classId) {
        Map<String, Object> params = new HashMap<>();
        params.put("classId",classId);
        Type type = new TypeToken<Result<List<RecipesVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETCB, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_GETCB)){
            Result<List<RecipesVo>> result = (Result<List<RecipesVo>>) date;
            List<RecipesVo> recipesVoList = result.getData();
            view.setRecipesVoList(recipesVoList);
        }else if (uri.equals(Apiurl.SCHOOL_GETUSERCLASS)){
            Result<List<ClassVo>> result = (Result<List<ClassVo>>) date;
            List<ClassVo> classVos = result.getData();
            view.setClassList(classVos);
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
