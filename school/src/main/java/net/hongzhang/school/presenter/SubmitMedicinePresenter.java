package net.hongzhang.school.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.MapVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/6/6
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SubmitMedicinePresenter implements SubmitMedicineContract.Presenter, OkHttpListener {
    private Activity context;
    private SubmitMedicineContract.View view;
    private String medicine;
    private String sick;

    public SubmitMedicinePresenter(Activity context, SubmitMedicineContract.View view) {
        this.context = context;
        this.view = view;
        getDrigType();
        getSickenType();
    }

    @Override
    public OptionsPickerView optionsPickerView(Context context, String content, OptionsPickerView.OnOptionsSelectListener listener, List<MapVo> mapList) {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, listener);
        builder.setCancelColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSubmitColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setCyclic(false, false, false)
                .setSelectOptions(1)
                .setLabels("", "", "")
                .setTitleText(content)
                .setContentTextSize(20)
                .setBackgroundId(0x66000000);//设置外部遮罩颜色
        OptionsPickerView optionsPickerView = builder.build();
        optionsPickerView.setPicker(getValuesList(mapList));
        return optionsPickerView;
    }

    public OptionsPickerView getoptionsPickerTime(Context context,OptionsPickerView.OnOptionsSelectListener listener) {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, listener);
        builder.setCancelColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSubmitColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setCyclic(false, false, false)
                .setSelectOptions(1, 1)
                .setLabels("", "", "")
                .setTitleText("选择日期和时间")
                .setContentTextSize(20)
                .setBackgroundId(0x66000000);//设置外部遮罩颜色
        OptionsPickerView optionsPickerView = builder.build();
        optionsPickerView.setNPicker(DateUtil.getDayList(), DateUtil.getWholeTime(),null);
        return optionsPickerView;
    }

    public List<String> getValuesList(List<MapVo> voList) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < voList.size(); i++) {
            values.add(voList.get(i).getValue());
        }
        return values;
    }

    @Override
    public void submitMedicine(String tsId, String sickenType, String drugType, List<String> medicineTime, String medicineNumber, String remark, String medicineDay) {
        Map<String, Object> params = new HashMap<>();
        if (G.isEmteny(sickenType) || G.isEmteny(drugType) || G.isEmteny(remark) || medicineTime.size() == 0) {
            Toast.makeText(context, "必填项不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (G.isAllSpace(remark)) {
            Toast.makeText(context, "留言不能全部为空格！", Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("tsId", tsId);
        params.put("sickenType", sickenType);
        params.put("drugType", drugType);
        params.put("medicineTime", getMedicineTime(medicineTime));
        params.put("medicineNumber", medicineNumber);
        params.put("remark", remark);
        params.put("medicineDay", medicineDay);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_SUBMITMEICINE, params, this);
        view.showLoadingDialog();
    }

    private String getMedicineTime(List<String> medicineTime) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < medicineTime.size(); i++) {
            if (medicineTime.size() - 1 == i) {
                buffer.append(medicineTime.get(i));
            } else {
                buffer.append(medicineTime.get(i)).append(",");
            }
        }
        return buffer.toString();
    }

    @Override
    public void getSickenType() {
        Map<String, Object> params = new HashMap<>();
        Type type = new TypeToken<Result<Map<String, String>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETSICKENTYPE, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void getDrigType() {
        Map<String, Object> params = new HashMap<>();
        Type type = new TypeToken<Result<Map<String, String>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETDRIGTYPE, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void addSickenType(String tsId, String sickenName) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("sickenName", sickenName);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_ADDSICKENTYPE, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void addDrigType(String tsId, String sickenName) {
        Map<String, Object> params = new HashMap<>();
        params.put("tsId", tsId);
        params.put("drugName", sickenName);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_ADDRIGTYPE, params, this);
        view.showLoadingDialog();
    }

    @Override
    public void setMedicineText(String text) {
        this.medicine = text;
    }

    @Override
    public void setSickText(String text) {
        this.sick = text;
    }

    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.SCHOOL_SUBMITMEICINE)) {
            Result<String> result = (Result<String>) date;
            G.showToast(context, result.getData());
            context.finish();
        } else if (uri.equals(Apiurl.SCHOOL_GETSICKENTYPE)) {
            Result<Map<String, String>> result = (Result<Map<String, String>>) date;
            Map<String, String> maps = result.getData();
            view.setSickenType(G.getItemList(maps));
        } else if (uri.equals(Apiurl.SCHOOL_GETDRIGTYPE)) {
            Result<Map<String, String>> result = (Result<Map<String, String>>) date;
            Map<String, String> maps = result.getData();
            view.setDrigType(G.getItemList(maps));
        } else if (uri.equals(Apiurl.SCHOOL_ADDSICKENTYPE)) {
            Result<String> result = (Result<String>) date;
            view.setSickText(sick, result.getData());
            G.showToast(context, result.getData());
        } else if (uri.equals(Apiurl.SCHOOL_ADDRIGTYPE)) {
            Result<String> result = (Result<String>) date;
            view.setMedicineText(medicine, result.getData());
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        DetaiCodeUtil.errorDetail(result, context);
    }
}
