package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.VacationVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/6/5
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveListPresenter implements LeaveListContract.Presenter, OkHttpListener {
    private Context context;
    private LeaveListContract.View view;
    private String tsId;
    private List<String> titleList;
    private int pageNumber = 1;
    private List<VacationVo> vacationVoList;
    private List<Integer> resIdList;

    public LeaveListPresenter(Context context, LeaveListContract.View view) {
        this.context = context;
        this.view = view;
        vacationVoList = new ArrayList<>();
        titleList = new ArrayList<>();
        resIdList = new ArrayList<>();
        UserMessage userMessage = new UserMessage(context);
        tsId = userMessage.getTsId();
        if (!userMessage.getType().equals("1")) {
            getTodayLeave(tsId);
        } else {
            getLeaveing(tsId, pageNumber, 10);
        }
    }

    @Override
    public void getTodayLeave(String tsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        Type type = new TypeToken<Result<List<VacationVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETTODAYLEAVES, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void getAlreadyLeave(String tsId, int pageNumber, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("pageNumber", pageNumber);
        params.put("pageSize", pageSize);
        Type type = new TypeToken<Result<List<VacationVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETALREADYLEAVES, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void getLeaveing(String tsId, int pageNumber, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("pageNumber", pageNumber);
        params.put("pageSize", pageSize);
        Type type = new TypeToken<Result<List<VacationVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETPATRIARCHLEAVES, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void getHistoryLeaveList(String tsId, int pageNumber, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("pageNumber", pageNumber);
        params.put("pageSize", pageSize);
        Type type = new TypeToken<Result<List<VacationVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETHISTORYLEAVES, params, this);
        view.showLoadingDialog();
    }

    private int beforeSize = 0;
    private int afterSize = 0;
    private void clean() {
        vacationVoList.clear();
        titleList.clear();
        resIdList.clear();
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        Result<List<VacationVo>> result = (Result<List<VacationVo>>) date;
        List<VacationVo> vacationVos = result.getData();
        if (Apiurl.SCHOOL_GETPATRIARCHLEAVES.equals(uri)) {
            if (vacationVos.size() > 0) {
                clean();
                vacationVoList.addAll(vacationVos);
                titleList.add("请假状态");
                resIdList.add(R.mipmap.ic_leave_status);
                beforeSize = vacationVos.size();
            } else {
                beforeSize = 0;
            }
            //view.setLeaveBeforeList(vacationVos, "请假状态", R.mipmap.ic_leave_status);
            getHistoryLeaveList(tsId, pageNumber, 10);
        } else if (Apiurl.SCHOOL_GETHISTORYLEAVES.equals(uri)) {
            if (beforeSize == 0) {
                clean();
            }
            if (vacationVos.size() > 0) {
                vacationVoList.addAll(vacationVos);
                titleList.add("请假历史");
                resIdList.add(R.mipmap.ic_leave_history);
                afterSize =  vacationVos.size();
            }
            view.setLeaveList(vacationVoList, beforeSize, afterSize ,titleList, resIdList);
           // view.setLeaveAfterList(vacationVos, "请假历史", R.mipmap.ic_leave_history);
        } else if (Apiurl.SCHOOL_GETTODAYLEAVES.equals(uri)) {
            if (vacationVos.size() > 0) {
                clean();
                vacationVoList.addAll(vacationVos);
                titleList.add("今日请假");
                resIdList.add(R.mipmap.ic_leave_ing);
                beforeSize = vacationVos.size();
            } else {
                beforeSize = 0;
            }
           // view.setLeaveBeforeList(vacationVos, "今日请假", R.mipmap.ic_leave_ing);
            getAlreadyLeave(tsId, pageNumber, 10);
        } else if (Apiurl.SCHOOL_GETALREADYLEAVES.equals(uri)) {
            if (beforeSize == 0) {
                clean();
            }
            if (vacationVos.size() > 0) {
                vacationVoList.addAll(vacationVos);
                titleList.add("已请假");
                resIdList.add(R.mipmap.ic_leave_already);
                afterSize =  vacationVos.size();
            }
            view.setLeaveList(vacationVoList, beforeSize, afterSize,titleList, resIdList);
          //  view.setLeaveAfterList(vacationVos, "已请假", R.mipmap.ic_leave_already);
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
