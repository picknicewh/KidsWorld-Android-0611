package net.hongzhang.school.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者： wanghua
 * 时间： 2017/6/5
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveAskPresenter implements LeaveAskContract.Presenter, OkHttpListener {
    private Activity context;
    private LeaveAskContract.View view;

    public LeaveAskPresenter(Activity context, LeaveAskContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void publishLeave(String tsId, String vaContent, String startDateTime, String endDateTime) {
        Map<String, Object> params = new HashMap<>();
        if (G.isEmteny(vaContent)) {
            Toast.makeText(context, "请选择请假事由！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (G.isEmteny(endDateTime)) {
            Toast.makeText(context, "结束时间不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endDateTime.compareTo(startDateTime) <= 0) {
            Toast.makeText(context, "请假开始时间不能比结束时间晚哦！", Toast.LENGTH_SHORT).show();
            return;
        }
        //提交角色ID
        UserMessage userMessage = UserMessage.getInstance(context);
        params.put("tsId", userMessage.getTsId());
        params.put("endDateTime", endDateTime);
        params.put("startDateTime", startDateTime);
        params.put("vaContent", vaContent);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_PUBLISHLEAVES, params, this);
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

    public OptionsPickerView getOptionsPickerView(Context context, OptionsPickerView.OnOptionsSelectListener listener, List<String> mapList) {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, listener);
        builder.setCancelColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSubmitColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setCyclic(true, false, false)
                .setSelectOptions(1)
                .setLabels("", "", "")
                .setTitleText("选择请求事由")
                .setContentTextSize(20)
                .setBackgroundId(0x66000000);//设置外部遮罩颜色
        OptionsPickerView optionsPickerView = builder.build();
        optionsPickerView.setPicker(mapList);
        return optionsPickerView;
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

    /**
     * 获取请假是由列表
     */
    public void getVacationContent() {
        Map<String, Object> params = new HashMap<>();
        Type type = new TypeToken<Result<Map<String, String>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_LEAVECOURSE, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_PUBLISHLEAVES)) {
            Result<String> result = (Result<String>) date;
            G.showToast(context, result.getData());
            context.finish();
        } else if (uri.equals(Apiurl.SCHOOL_LEAVECOURSE)) {
            Result<Map<String, String>> result = (Result<Map<String, String>>) date;
            Map<String, String> vo = result.getData();
            view.setCourseList(getItemList(vo));
        }
    }

    public List<Map<String, String>> getItemList(Map<String, String> itemList) {
        List<Map<String, String>> params = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = itemList.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            Map<String, String> dataList = new HashMap<>();
            dataList.put("code", entry.getKey());
            dataList.put("course", entry.getValue());
            params.add(dataList);
        }
        return params;
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
