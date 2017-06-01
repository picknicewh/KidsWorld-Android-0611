package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.school.bean.RankingInfoVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称：排行榜
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class RankingListPresenter implements RankingListContract.Presenter, OkHttpListener {
    private Context context;
    private RankingListContract.View view;

    public RankingListPresenter(Context context, RankingListContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getRankingList(String activityId) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityId", activityId);
        Type type = new TypeToken<Result<List<RankingInfoVo>>>() {}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETACTIVERANKINGLIST, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_GETACTIVERANKINGLIST)) {
            Result<List<RankingInfoVo>> result = (Result<List<RankingInfoVo>>) date;
            List<RankingInfoVo> rankingInfoVos = result.getData();
            view.setRankingList(rankingInfoVos);
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
