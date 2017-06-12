package net.hongzhang.school.presenter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.MapVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.user.util.BitmapCache;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
public class SubmitRecipesPresenter implements SubmitRecipesContract.Presenter, OkHttpListener {
    private Context context;
    private SubmitRecipesContract.View view;

    public SubmitRecipesPresenter(Context context, SubmitRecipesContract.View view) {
        this.context = context;
        this.view = view;
        getRecipesTime();
    }

    @Override
    public OptionsPickerView optionsPickerView(Context context, OptionsPickerView.OnOptionsSelectListener listener) {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, listener);
        builder.setCancelColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSubmitColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setCyclic(true, false, false)
                .setSelectOptions(1)
                .setLabels("", "", "")
                .setTitleText("请选择时间点")
                .setContentTextSize(20)
                .setBackgroundId(0x66000000);//设置外部遮罩颜色
        OptionsPickerView optionsPickerView = builder.build();
        optionsPickerView.setPicker(getTimeList());
        return optionsPickerView;
    }

    private List<String> getTimeList() {
        List<String> timeList = new ArrayList<>();
        timeList.add("早餐");
        timeList.add("早点");
        timeList.add("午餐");
        timeList.add("午点");
        timeList.add("晚餐");
        return timeList;
    }

    private List<String> getCodeList() {
        List<String> mapVoList = new ArrayList<>();
        mapVoList.add("10001");
        mapVoList.add("10002");
        mapVoList.add("10003");
        mapVoList.add("10004");
        mapVoList.add("10005");
        return mapVoList;
    }

    @Override
    public void getRecipesTime() {
        List<MapVo> mapVoList = new ArrayList<>();
        for (int i = 0; i < getCodeList().size(); i++) {
            MapVo mapVo = new MapVo();
            mapVo.setValue(getTimeList().get(i));
            mapVo.setKey(getCodeList().get(i));
            mapVoList.add(mapVo);
        }
        view.setRecipesTime(mapVoList);
    }

    @Override
    public void submitRecipes(String tsId, String classId, String title, String mType,List<String> imageList) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("classId", classId);
        params.put("title", title);
        params.put("type", mType);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        List<File> files =  BitmapCache.getFileList(imageList);
        OkHttps.sendPost(type, Apiurl.SCHOOL_ISSUECB, params, files,this);
        view.showLoadingDialog();
    }


    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_ISSUECB)) {
            Result<String> result = (Result<String>) date;
            G.showToast(context, result.getData());
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}

