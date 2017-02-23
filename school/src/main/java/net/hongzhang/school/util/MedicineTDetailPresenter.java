package net.hongzhang.school.util;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.school.bean.MedicineVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/10/31
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineTDetailPresenter implements MedicineTDetail.Presenter,OkHttpListener {
    private MedicineTDetail.View view;
    private Activity context;
    private int isFeed;
    public  MedicineTDetailPresenter(MedicineTDetail.View view, Activity context,String medicineId,String tsId,int isFeed){
        this.view = view;
        this.context = context;
        this.isFeed = isFeed;
        getMedicineDetils(tsId,medicineId);
    }
    @Override
    public void getMedicineDetils(String tsId, String medicineId) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("medicineId",medicineId);
        Type type=new TypeToken<Result<MedicineVo>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_MEDICINETDETAIL,map,this);
    }

    @Override
    public void finishFeedMedicine(String tsId, String medicineId) {
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",tsId);
        map.put("medicineId",medicineId);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_MEDICINETFINSH,map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
     if (uri.equals(Apiurl.SCHOOL_MEDICINETDETAIL)){
         Result<MedicineVo> data = (Result<MedicineVo>) date;
         if (data!=null){
             MedicineVo medicineVo = data.getData();
             view.setHeadImageView(medicineVo.getImgUrl());
             view.setMedicineDosage(medicineVo.getMedicine_dosage());
             view.setRemark(medicineVo.getMedicine_doc());
             view.setTsName(medicineVo.getTs_name());
             view.setIsFeed(isFeed);
             view.setMedicineName(medicineVo.getMedicine_name());
             view.setLaunchTime(medicineVo.getMeal_before_or_after());
             StringBuffer buffer = new StringBuffer();
             for (int i = 0 ; i<medicineVo.getMedicineStatusList().size();i++){
                 buffer.append(medicineVo.getMedicineStatusList().get(i).getCreate_time().substring(0,11)).append(";");
             }
             view.setFeedDate(buffer.toString());
         }
     }else if (uri.equals(Apiurl.SCHOOL_MEDICINETFINSH)){
         Result<String> data = (Result<String>) date;
         if (data!=null){
             String result =data.getData();
             if (result.contains("成功")){
                 view.setIsFeed(1);
                 context.finish();
             }
         }
     }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
}
