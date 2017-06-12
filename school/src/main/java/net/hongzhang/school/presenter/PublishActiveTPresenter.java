package net.hongzhang.school.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.reflect.TypeToken;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者： wanghua
 * 时间： 2017/5/4
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishActiveTPresenter implements PublishActiveTContract.Presenter, OkHttpListener {
    private Activity context;
    private PublishActiveTContract.View view;

    public PublishActiveTPresenter(Activity context, PublishActiveTContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void publishActive(String tsId, String title, String content, String type, String dimensionality, long postTime, long deadline, int appraise, List<String> itemList) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("title", title);
        params.put("content", content);
        params.put("type", type);
        params.put("dimensionality", dimensionality);
        params.put("postTime", postTime);
        params.put("deadline", deadline);
        params.put("appraise", appraise);
        G.log("-x-x--x-x---x-x" + itemList.size());
        List<File> list = BitmapCache.getFileList(itemList);
        Type mType = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.SCHOOL_PUBLISHACTIVITY, params, list, this);
        view.showLoadingDialog();
    }
    public TimePickerView getTimePickerView(Context context, TimePickerView.OnTimeSelectListener listener) {
        TimePickerView.Builder builder = new TimePickerView.Builder(context, listener);
        builder.setType(getType())
                .setLabel("", "", "", "", "", "")
                .setCancelColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSubmitColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setTitleText("选择日期和时间")
                .isCyclic(true)//是否循环滚动
                .setContentSize(20);
        TimePickerView timePickerView = builder.build();
        timePickerView.setDate(Calendar.getInstance());
        return timePickerView;
    }



    private static boolean[] getType() {
        boolean[] type = new boolean[6];
        for (int i = 0; i < 6; i++) {
            if (i == 5) {
                type[i] = false;
            } else {
                type[i] = true;
            }
        }
        return type;
    }
    @Override
    public void getActivityType() {
        Map<String, Object> params = new HashMap<>();
        Type mType = new TypeToken<Result<Map<String, String>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.SCHOOL_GRTACTIVITYTYPE, params, this);
    }

    @Override
    public void getDimensionality() {
        Map<String, Object> params = new HashMap<>();
        Type mType = new TypeToken<Result<Map<String, String>>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.SCHOOL_GETDIMENDIONAL, params, this);
    }

    public void setBackRepAnimation(ImageView textView) {
        /**
         *
         float fromXDelta 动画开始的点离当前View X坐标上的差值
         float toXDelta 动画结束的点离当前View X坐标上的差值
         float fromYDelta 动画开始的点离当前View Y坐标上的差值
         float toYDelta 动画开始的点离当前View Y坐标上的差值
         */
        TranslateAnimation animation = new TranslateAnimation(0,100,0,0);
        animation.setDuration(100);//设置动画持续时间
        animation.setRepeatCount(3);//设置重复次数
        animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
        textView.startAnimation(animation);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_PUBLISHACTIVITY)) {
            Result<String> result = (Result<String>) date;
            G.showToast(context, result.getData());
            context.finish();
        } else if (uri.equals(Apiurl.SCHOOL_GRTACTIVITYTYPE)) {
            Result<Map<String, String>> result = (Result<Map<String, String>>) date;
            view.setActivityType(result.getData());
        } else if (uri.equals(Apiurl.SCHOOL_GETDIMENDIONAL)) {
            Result<Map<String, String>> result = (Result<Map<String, String>>) date;
            view.setDimensionality(result.getData());
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
