package net.hunme.school.util;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.school.bean.MedicineVo;

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
    private Context context;
    private int isFeed;
    public  MedicineTDetailPresenter(MedicineTDetail.View view, Context context,String medicineId,String tsId,int isFeed){
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
            // view.setFeedDate(medicineVo.);
         }
     }else if (uri.equals(Apiurl.SCHOOL_MEDICINETFINSH)){
         Result<String> data = (Result<String>) date;
         if (data!=null){
             String result =data.getData();
             if (result.contains("成功")){
                 view.setIsFeed(1);
             }
         }
     }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
}
