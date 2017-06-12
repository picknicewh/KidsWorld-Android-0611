package net.hongzhang.school.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.school.bean.MyMedicineVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/6/7
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineListPresenter implements MedicineListContract.Presenter, OkHttpListener {
    private Context context;
    private MedicineListContract.View view;

    public MedicineListPresenter(Context context, MedicineListContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getMedicineDate(String tsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        Type type = new TypeToken<Result<List<String>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETMEDICINEDATE, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void getMedicine(String tsId, String date) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("date", date);
        Type type = new TypeToken<Result<List<MyMedicineVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETMEDICINE, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_GETMEDICINEDATE)) {
            Result<List<String>> result = (Result<List<String>>) date;
            List<String> dateList = result.getData();
            view.setMedicineDate(dateList);
        } else if (uri.equals(Apiurl.SCHOOL_GETMEDICINE)) {
            Result<List<MyMedicineVo>> result = (Result<List<MyMedicineVo>>) date;
            List<MyMedicineVo> medicineVos = result.getData();
            view.setMedicine(medicineVos);
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
